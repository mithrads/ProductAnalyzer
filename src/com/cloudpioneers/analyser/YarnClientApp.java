package com.cloudpioneers.analyser;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;


public class YarnClientApp {

		private static YarnClientApp yarnClientApp= null;
		
		private YarnClientApp(){
			
		}
		
		public static YarnClientApp getInst(){
			if(yarnClientApp==null){
				yarnClientApp = new YarnClientApp();
			}
			return yarnClientApp;
		}
	
	   public String postSubmitHadoopJob(String arg3){
	    	
		   
		    String appIdNew = null;
	    	try{
	    		
			Client client = Client.create();
	
			WebResource webResource = client
			   .resource("http://localhost:8088/ws/v1/cluster/apps/new-application");
	
	
			ClientResponse response = webResource.type("application/json").post(ClientResponse.class);
	
			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
				     + response.getStatus());
			}
	
			System.out.println("Output from Server for new-application .... \n");
			String output = response.getEntity(String.class);
			System.out.println(output);    	
	    	
	    	//String output = "{\"application-id\":\"application_1475638974154_0028\",\"maximum-resource-capability\":{\"memory\":2250,\"vCores\":8}}";	
	    	
			JSONObject obj = new JSONObject(output);
				
				if(obj.has("application-id")){
					appIdNew = obj.getString("application-id");
					System.out.println("application-id:"+ appIdNew);
							

					
					String jsonString = "{ \"application-id\":\"" + appIdNew.trim() + "\", \"application-name\":\"ProductAnalyser\","
						    + "\"am-container-spec\": {  \"commands\":  {"
						   + "\"command\":\"/usr/bin/yarn jar /usr/lib/hue/ProductAnalyser4.jar ProductAnalyser.ProductAnalyserAppn /user/maria_dev/ProductData" + arg3 + " /user/maria_dev/Prod" + appIdNew + "\"  },  \"environment\":"
						  + " { \"entry\": [  { \"key\": \"CLASSPATH\", \"value\": \"{{CLASSPATH}}<CPS>./*<CPS>{{HADOOP_CONF_DIR}}<CPS>{{HADOOP_COMMON_HOME}}/share/hadoop/common/*<CPS>{{HADOOP_COMMON_HOME}}/share/hadoop/common/lib/*<CPS>{{HADOOP_HDFS_HOME}}/share/hadoop/hdfs/*<CPS>{{HADOOP_HDFS_HOME}}/share/hadoop/hdfs/lib/*<CPS>{{HADOOP_YARN_HOME}}/share/hadoop/yarn/*<CPS>{{HADOOP_YARN_HOME}}/share/hadoop/yarn/lib/*<CPS>./log4j.properties\""
						      + "}  ]  } },\"unmanaged-AM\":false, \"max-app-attempts\":1, \"resource\": { \"memory\":1024, \"vCores\":1 }, \"application-type\":\"MAPREDUCE\", \"keep-containers-across-application-attempts\":false }";
					
					
				System.out.println(jsonString);
				
				Client client1 = Client.create();
				
//				WebResource webResource1 = client1
//						   .resource("http://localhost:8088/ws/v1/cluster/apps?user.name=maria_dev");
				WebResource webResource1 = client1
						   .resource("http://localhost:8088/ws/v1/cluster/apps?user.name=maria_dev");

						ClientResponse response1 = webResource1.type("application/json")
						   .post(ClientResponse.class, jsonString);

						System.out.println("Status Code:" + response1.getStatus());
						
						if (response1.getStatus() != 202) {
							throw new RuntimeException("Failed : HTTP error code : "
							     + response1.getStatus());
						}

						System.out.println("Output from Server for POST or submit the application.... \n");
						String output1 = response1.getEntity(String.class);
						System.out.println(output1);
				
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch(Exception ex){
	    		System.out.println(ex.getMessage());
	    		ex.printStackTrace();
	    	}
	    	
	    	
	    	 return appIdNew;
	    		
	    }	
	
	
    public String getStatusforHadoopJob(String appId){
    	
    	
    	String finalstate = null;
		Client client = Client.create();

		WebResource webResource = client
		   .resource("http://localhost:8088/ws/v1/cluster/apps/" + appId.trim() );

		ClientResponse response = webResource.accept("application/json")
                   .get(ClientResponse.class);

		if (response.getStatus() != 200) {
		   throw new RuntimeException("Failed : HTTP error code : "
			+ response.getStatus());
		}

		String output = response.getEntity(String.class);

		
		System.out.println("Output from Server for Get Status .... \n");
		System.out.println(output);
		output = "["+output+"]";
        
		try {
			JSONArray jarray = new JSONArray(output);
			
			for (int count = 0; count < jarray.length(); count++) {
				
				
			    JSONObject obj = jarray.getJSONObject(count);
			    System.out.println("JSON:" + obj.toString());
			    
		    	//JSONArray jarrayapp = obj.getJSONArray("app");
		    	if(obj.has("app")){
			    JSONObject jsonobj = obj.getJSONObject("app");
			    System.out.println("JSON app:" + jsonobj.toString());
			    
			    String applicationId = jsonobj.getString("id");
			    System.out.println("JSON id:" + applicationId.toString());
			    
			    String user = jsonobj.getString("user");
			    System.out.println("JSON id:" + user.toString());	
			    
			    String state = jsonobj.getString("state");
			    System.out.println("JSON id:" + state.toString());		
			    finalstate = state;
			    
		    	}
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return finalstate;
    }	
	
	
    public List<String> ReadHDFSFileData(String appId){
    	
    	
    	appId = "Prod" + appId;
    	    	
    	//curl -i -L "http://localhost:50070/webhdfs/v1/user/maria_dev/Prodapplication_1475638974154_0028/part-r-00000?op=OPEN"
    	
    	List<String> listOfCities = new ArrayList<String>();
    	
    	try {

    		Client client = Client.create();
    		
    		client.setFollowRedirects(true);

    		WebResource webResource = client
    		   .resource("http://localhost:50070/webhdfs/v1/user/maria_dev/" + appId.trim() + "/part-r-00000?op=OPEN");

    		    		
    		ClientResponse response = webResource.accept("application/octet-stream")
                       .get(ClientResponse.class);

    		
    		if (response.getStatus() != 200) {
    		   throw new RuntimeException("Failed : HTTP error code : "
    			+ response.getStatus());
    		}
    		
    		

    		String output = response.getEntity(String.class);

    		System.out.println("Output from Server for Read from HDFS File.... \n");
    		System.out.println(output);

    		String[] stringarr = output.split("\n");
    		
    		System.out.println("size:" + stringarr.length);
    		
    		HashMap<String, Integer> map = new HashMap<String, Integer>();
    		
    		for(int i=0; i<stringarr.length;i++){
    			System.out.println(i +":" +stringarr[i]);
    			String CountyName = stringarr[i];
    			String weight = stringarr[i];
    			int length = stringarr[i].length();
    			CountyName = CountyName.substring(0, length-3);
    			weight = weight.substring(length-3,length);
    			System.out.println(CountyName.trim() + "::::" + weight.trim());
    			int value = Integer.parseInt(weight.trim());
    			map.put(CountyName, value);
    			
    		}

    		TreeMap<String, Integer> sortedMap = sortMapByValue(map);  
    		
    		System.out.println("TOP 10 Cities for appId" + appId);
    		for(Map.Entry<String,Integer> entry : sortedMap.entrySet()) {
    			
    			  String key = entry.getKey();
    			  Integer value = entry.getValue();
    			  System.out.println("City:"+key + " Value:"+value);
    			  listOfCities.add(key);
    			  if(listOfCities.size()>=10){
    				  break;
    			  }
    		}  		
    		//System.out.println(sortedMap);
    		
    	  } catch (Exception e) {

    		e.printStackTrace();

    	  }   	
    	
    	return listOfCities;
    	
    }
	
	public static TreeMap<String, Integer> sortMapByValue(HashMap<String, Integer> map){
		Comparator<String> comparator = new ValueComparator(map);
		//TreeMap is a map sorted by its keys. 
		//The comparator is used to sort the TreeMap by keys. 
		TreeMap<String, Integer> result = new TreeMap<String, Integer>(comparator);
		result.putAll(map);
		return result;
	}
	
}

class ValueComparator implements Comparator<String>{
	 
	HashMap<String, Integer> map = new HashMap<String, Integer>();
 
	public ValueComparator(HashMap<String, Integer> map){
		this.map.putAll(map);
	}
 
	public int compare(String s1, String s2) {
		if(map.get(s1) >= map.get(s2)){
			return -1;
		}else{
			return 1;
		}	
	}
	
	//curl -i -X POST -H 'Accept: application/json' -H 'Content-Type: application/json' http://localhost:8088/ws/v1/cluster/apps?user.name=maria_dev -d @product-json.txt
//	HTTP/1.1 202 Accepted
//	Cache-Control: no-cache
//	Expires: Sat, 08 Oct 2016 00:37:10 GMT
//	Date: Sat, 08 Oct 2016 00:37:10 GMT
//	Pragma: no-cache
//	Expires: Sat, 08 Oct 2016 00:37:10 GMT
//	Date: Sat, 08 Oct 2016 00:37:10 GMT
//	Pragma: no-cache
//	Content-Type: application/json
//	Set-Cookie: hadoop.auth="u=maria_dev&p=maria_dev&t=simple&e=1475923030496&s=51Mel7Hh5Noc5dUOr28WQVQOgdI="; Path=/; HttpOnly
//	X-Frame-Options: SAMEORIGIN
//	Location: http://localhost:8088/ws/v1/cluster/apps/application_1475638974154_0021
//	Content-Length: 0
//	Server: Jetty(6.1.26.hwx)
	
	
//String jsonString = "{ \"application-id\":\"" + appIdNew.trim() + "\", \"application-name\":\"ProductAnalyser\","
//+ "\"am-container-spec\": {  \"commands\":  {"
//+ "\"command\":\"/usr/bin/yarn jar /usr/lib/hue/ProductAnalyser.jar ProductAnalyser.ProductAnalyserAppn /user/maria_dev/ProductData /user/maria_dev/Prod" + appIdNew + " " + arg3 + "\"  },  \"environment\":"
//+ " { \"entry\": [  { \"key\": \"CLASSPATH\", \"value\": \"{{CLASSPATH}}<CPS>./*<CPS>{{HADOOP_CONF_DIR}}<CPS>{{HADOOP_COMMON_HOME}}/share/hadoop/common/*<CPS>{{HADOOP_COMMON_HOME}}/share/hadoop/common/lib/*<CPS>{{HADOOP_HDFS_HOME}}/share/hadoop/hdfs/*<CPS>{{HADOOP_HDFS_HOME}}/share/hadoop/hdfs/lib/*<CPS>{{HADOOP_YARN_HOME}}/share/hadoop/yarn/*<CPS>{{HADOOP_YARN_HOME}}/share/hadoop/yarn/lib/*<CPS>./log4j.properties\""
//+ "}  ]  } },\"unmanaged-AM\":false, \"max-app-attempts\":2, \"resource\": { \"memory\":1024, \"vCores\":1 }, \"application-type\":\"YARN\", \"keep-containers-across-application-attempts\":false }";

	
}

