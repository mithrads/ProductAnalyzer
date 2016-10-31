package com.cloudpioneers.entity;

import java.util.ArrayList;
import java.util.List;

public class ProductLaunchDetails {

	Product product;
	List<String> Top10Cities = new ArrayList<String>();
	
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public List<String> getTop10Cities() {
		return Top10Cities;
	}
	public void setTop10Cities(List<String> top10Cities) {
		Top10Cities = top10Cities;
	}
	
}
