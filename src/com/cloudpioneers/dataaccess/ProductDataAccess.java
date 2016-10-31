package com.cloudpioneers.dataaccess;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.cloudpioneers.analyser.AnalysisSystem;
import com.cloudpioneers.entity.Product;
import com.cloudpioneers.entity.ProductLaunchDetails;

public class ProductDataAccess {

	private static ProductDataAccess productDataAccess=null;
	
	private ConcurrentLinkedQueue<Product> productList= new ConcurrentLinkedQueue<Product>();
	private ConcurrentLinkedQueue<ProductLaunchDetails> ProductLaunchList= new ConcurrentLinkedQueue<ProductLaunchDetails>();
	
	private int nextProdId=1;
	
	private ProductDataAccess(){
		
	}

	public void init(){
		//Adding some static products
		Product product1 = new Product();
		product1.setProductName("Transformer X");
		product1.setProductCategoryName("Toys");
		product1.setProductLanchDate(new Date().toString());
		product1 = CreateProduct(product1);
		AnalysisSystem.getInst().CreateJob(product1.getProductId());
		
		
		Product product2 = new Product();
		product2.setProductName("Ice Hockey Kit");
		product2.setProductCategoryName("Sports");
		product2.setProductLanchDate(new Date().toString());
		product2 = CreateProduct(product2);	
		AnalysisSystem.getInst().CreateJob(product2.getProductId());
	}
	
	public static ProductDataAccess getInst(){
		if(productDataAccess==null){
			productDataAccess = new ProductDataAccess();
		}
		return productDataAccess;
	}
	
	//Product
	public Product CreateProduct(Product product){
		
		product.setProductId(nextProdId++);
		productList.add(product);
		System.out.println("Created new Product:"+ product.getProductId());
		return product;
		
	}

	public Product getProduct(int productId) {
		
		Product product = null;
		for(Product prod : productList){
			if(prod.getProductId() == productId ){
				product = prod;
			}
		}
		return product;
	}
	
	public List<Product> getAllProducts(){
		
		List<Product> products = new ArrayList<Product>();
		
		for(Product prod : productList){
			products.add(prod);
		}
		
		return products;
	}
	
	
	//ProductLaunchDetails
	public ProductLaunchDetails CreateProductLaunchDetails(ProductLaunchDetails productLaunchDetails){
		ProductLaunchList.add(productLaunchDetails);
		return productLaunchDetails;
	}
	
	
	public ProductLaunchDetails GetProductLaunchDetails(int productId) {
	
		ProductLaunchDetails productLaunch = null;
		System.out.println("Size of ProductLaunchList:" + ProductLaunchList.size());
		for(ProductLaunchDetails pl : ProductLaunchList){
			if(pl.getProduct().getProductId() == productId){
				productLaunch = pl;
				System.out.println("Found ProductLaunchDetails for product id:"+pl.getProduct().getProductId());
				break;
			}
		}
		
		return productLaunch;
	}
	
	
}
