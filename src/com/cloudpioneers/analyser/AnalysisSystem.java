package com.cloudpioneers.analyser;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.cloudpioneers.entity.ProductLaunchDetails;

public class AnalysisSystem {
	
	public static AnalysisSystem analysisSystem=null;
	//List<Job> jobList = new ArrayList<Job>();
	ConcurrentLinkedQueue<Job> jobList = new ConcurrentLinkedQueue<Job>();
	int nextJobId=1;
	
	//List<ProductLaunchDetails> ProductLanchList = new ArrayList<ProductLaunchDetails>();
	ConcurrentLinkedQueue<ProductLaunchDetails> ProductLanchList = new ConcurrentLinkedQueue<ProductLaunchDetails>();
	
	private Analyser analyser = new Analyser();
	
	private AnalysisSystem(){
		
	}	
	
	public static AnalysisSystem getInst(){
		if(analysisSystem==null){
			analysisSystem = new AnalysisSystem();
		}
		return analysisSystem;
	}

	public void init(){
		analyser.start();
	}
	
	public void unInit(){
		analyser.setRunning(false);
	}
	
	public ConcurrentLinkedQueue<Job> getJobList() {
		return jobList;
	}

	public void setJobList(ConcurrentLinkedQueue<Job> jobList) {
		this.jobList = jobList;
	}
	
	public Job CreateJob(int productId){
		
		Job job = new Job();
		job.setJobId(nextJobId++);
		job.setJobStatus(JobStatus.Initiated);
		job.setProductId(productId);
		getJobList().add(job);
		System.out.println("Created new Job:" + job.getJobId() + " for product id:" + productId );
		return job;
		
	}


}
