package com.cloudpioneers.analyser;

public class Job {

	int jobId;
	int productId;
	String appId;
	JobStatus jobStatus;
	
	
	public int getJobId() {
		return jobId;
	}
	
	public void setJobId(int jobId) {
		this.jobId = jobId;
	}
	
	public int getProductId() {
		return productId;
	}
	
	public void setProductId(int productId) {
		this.productId = productId;
	}
	
	public JobStatus getJobStatus() {
		return jobStatus;
	}
	
	public void setJobStatus(JobStatus jobStatus) {
		this.jobStatus = jobStatus;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}
	
	
	
}
