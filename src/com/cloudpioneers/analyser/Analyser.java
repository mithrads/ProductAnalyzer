package com.cloudpioneers.analyser;

import java.util.List;

import com.cloudpioneers.dataaccess.ProductDataAccess;
import com.cloudpioneers.entity.Product;
import com.cloudpioneers.entity.ProductLaunchDetails;

public class Analyser implements Runnable {

	private Thread t;
	private String ThreadName = "Analyser";
	boolean isRunning = true;
	boolean isAnalyzing = false;
	
	public synchronized boolean isRunning() {
		return isRunning;
	}
	
	public synchronized void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}

	public synchronized void setAnalyzing(boolean isAnalyzing) {
		this.isAnalyzing = isAnalyzing;
	}
	
	
	public void run() {
		
			ProcessJob();
	}
	
	public void start(){
		
	      if (t == null)
	      {
	    	  System.out.println("Starting " + ThreadName );
	         t = new Thread (this, ThreadName);
	         t.start ();
	      }
	}
	
	private void ProcessJob(){
		
		while(isRunning()){ 
			for(Job job : AnalysisSystem.getInst().getJobList()){
				if(isAnalyzing==false){
					if(job.getJobStatus()==JobStatus.Initiated){
						
						Product product = ProductDataAccess.getInst().getProduct(job.getProductId());
						job.setJobStatus(JobStatus.Running);
						this.setAnalyzing(true);
						System.out.println("Job for Product "+ product.getProductId() +":" + product.getProductName() + " moved from Initiated to Running");
						//Start the Job in Hadoop.
						String arg3 = "";
						if(product.getProductCategoryName().equals("Toys") || product.getProductCategoryName().equals("Comic Books")){
							arg3="Kids";
						}else if(product.getProductCategoryName().equals("Sports") || product.getProductCategoryName().equals("Video Games")){
							arg3="Teens";
						}
						
						String appId = YarnClientApp.getInst().postSubmitHadoopJob(arg3);
						if(appId!=null){
							System.out.println("Hadoop Job Started: App ID:"+appId);
							job.setAppId(appId);
						}else{
							System.out.println("Hadoop Job Unable to Start. ERROR Failed ");
							job.setJobStatus(JobStatus.Failed);
						}
					}
				}else{
					
					//FAILED, FINISHED
					
					if(job.getJobStatus()==JobStatus.Running){
						
						//Wait for Hadoop to complete the job and  if results are ready. if so populated the details
						String state = YarnClientApp.getInst().getStatusforHadoopJob(job.getAppId());
						
						
						//Though complete successfully, Hadoop return state finished or failed if called from API. -TODO
						if(state.equals("FINISHED") || state.equals("FAILED")){
							
							try {
								Thread.sleep(20000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							List<String> result = YarnClientApp.getInst().ReadHDFSFileData(job.getAppId());
							Product product = ProductDataAccess.getInst().getProduct(job.getProductId());
							ProductLaunchDetails productLaunchDetails = new ProductLaunchDetails();
							productLaunchDetails.setProduct(product);

							for(String str :result){
								productLaunchDetails.getTop10Cities().add(str);
							}
							
							ProductDataAccess.getInst().CreateProductLaunchDetails(productLaunchDetails);
							job.setJobStatus(JobStatus.Completed);
							this.setAnalyzing(false);
							System.out.println("Job for Product "+ product.getProductId() +":" + product.getProductName() + " moved from Running to Completed");							
							
						}else{
							try {
								Thread.sleep(5000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						
//						Product product = ProductDataAccess.getInst().getProduct(job.getProductId());
//						ProductLaunchDetails productLaunchDetails = new ProductLaunchDetails();
//						productLaunchDetails.setProduct(product);
//						//Autauga County, AL
//						productLaunchDetails.getTop10Cities().add("San Jose, CA");
//						productLaunchDetails.getTop10Cities().add("San Francisco, CA");
//						productLaunchDetails.getTop10Cities().add("New York, CA");
//						productLaunchDetails.getTop10Cities().add("Boston, MA");
//						productLaunchDetails.getTop10Cities().add("Houston, TX");
//						productLaunchDetails.getTop10Cities().add("Dallas, TX");
//						productLaunchDetails.getTop10Cities().add("Seattle, WA");
//						productLaunchDetails.getTop10Cities().add("Portland, OR");
//						productLaunchDetails.getTop10Cities().add("Chicago, IL");
//						productLaunchDetails.getTop10Cities().add("Los Angeles, CA");
//						ProductDataAccess.getInst().CreateProductLaunchDetails(productLaunchDetails);
//						job.setJobStatus(JobStatus.Completed);
//						this.setAnalyzing(false);
//						System.out.println("Job for Product "+ product.getProductId() +":" + product.getProductName() + " moved from Running to Completed");
					}
				}
				
			}
		}
	}
}
