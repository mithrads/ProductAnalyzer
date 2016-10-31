package com.cloudpioneers.productservice;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONException;
import org.json.JSONObject;

import com.cloudpioneers.analyser.AnalysisSystem;
import com.cloudpioneers.analyser.Job;
import com.cloudpioneers.dataaccess.ProductDataAccess;
import com.cloudpioneers.entity.*;

/**
 * HOW TO ACCESS
 * http://localhost:8080/cloudpioneers/Product
 * 
 * @author makuland
 *
 */

@Path("/Product")
public class ProductResource {

	
	  /*
	   * 
	    POST /cloudpioneers/Product/Create HTTP/1.1
		Host: localhost:8080
		Accept: application/json
		Content-Type: application/json
		Cache-Control: no-cache

		{"productName":"Barbie","productCategoryName":"Toys","productLanchDate":"Tue Oct 20 11:54:57 PDT 2016"}
	   * 
	   */
	  
	  @POST
	  @Path("/Create")
	  @Consumes(MediaType.APPLICATION_JSON)
	  @Produces(MediaType.APPLICATION_JSON)
	  public Product createProduct(Product product){
		  
		  
		  System.out.println("Create Request Received for Product Name:" + product.getProductName());
		  Product prod = ProductDataAccess.getInst().CreateProduct(product);
		  
		  AnalysisSystem.getInst().CreateJob(prod.getProductId());
		  
		  return prod;
		  
	  }
	
	  /*
	   * http://localhost:8080/cloudpioneers/Product
	   */
	  @GET
	  @Produces(MediaType.APPLICATION_JSON)
	  public List<Product> getAllProducts() throws JSONException {

//		  Product product = new Product();
//		  product.setProductId(1);
//		  product.setProductName("Transformer");
//		  product.setProductCategoryName("Toys");
//		  DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//		  //get current date time with Date()
//		  Date date = new Date();
//		  product.setProductLanchDate(dateFormat.format(date).toString());
//		  
//		  return product;
//		  //return Response.status(200).entity(event).build();
		
		  return ProductDataAccess.getInst().getAllProducts();
		   
	  }
	  
	  
	  /*
	   * REST API Call:
	   * http://localhost:8080/cloudpioneers/Product/1
	   * 
	   * Output:
	   * {"productId":1,"productName":"Transformer  X","productCategoryName":"Toys","productLanchDate":"Tue Oct 04 11:54:57 PDT 2016"}
	   */
	  @GET
	  @Path("{product_id}")
	  @Produces(MediaType.APPLICATION_JSON)
	  public Product getProduct( @PathParam("product_id") int product_id) throws JSONException {
		  
		  System.out.println("Get Product for product_id:" + product_id);
		  
		  Product prod = ProductDataAccess.getInst().getProduct(product_id);
		  
		  if(prod==null){
			  System.out.println("ERROR: Could not find Product for product_id:" + product_id);
		  }
		  
		  return prod;
		
	  }

	  
	  /*
	   * REST API Call:
	   * http://localhost:8080/cloudpioneers/Product/ProductLaunch/1
	   * 
	   * OUPUT:
	   *{"product":{"productId":1,"productName":"Transformer  X","productCategoryName":"Toys","productLanchDate":"Tue Oct 04 11:51:45 PDT 2016"},"top10Cities":["San Jose","San Francisco","New York","Boston","Houston","Dallas","Seattle","Washington","Chicage","Mineapolis"]}
	   */
	  
	  @GET
	  @Path("/ProductLaunch/{product_id}")
	  @Produces(MediaType.APPLICATION_JSON)
	  public ProductLaunchDetails getProductLaunchDetails(@PathParam("product_id") int product_id ){
		  
		  System.out.println("Get ProductLaunch for product_id:" + product_id);
		  
		  //Product prod = ProductDataAccess.getInst().getProduct(product_id);
		  
		  ProductLaunchDetails prodLaunch = null;
		  
		  prodLaunch = ProductDataAccess.getInst().GetProductLaunchDetails(product_id);
		  
		  if(prodLaunch==null){
			  System.out.println("ERROR: Could not find Product Launch for product_id:" + product_id);
		  }

		  return prodLaunch;
		  
	  }
	
}
