package com.cloudpioneers.common;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.cloudpioneers.analyser.AnalysisSystem;
import com.cloudpioneers.dataaccess.ProductDataAccess;

public class CloudPioneer implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		System.out.println("Calling contextInitialized!!!!!!!!!!!!!");
		ProductDataAccess.getInst().init();
		AnalysisSystem.getInst().init();
		
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		System.out.println("Calling contextDestroyed -  UnInitialized!!!!!!!!!!!!!");
		AnalysisSystem.getInst().unInit();
	}

}
//public class CloudPioneer  {
//
//	static{
//		//AnalysisSystem.getInst().Init();
//	}
//	
//}