package com.cloudpioneers.entity;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Product")
public class Product {

	int productId;
	String productName;
	String productCategoryName;
	String productLanchDate;
	
	
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductCategoryName() {
		return productCategoryName;
	}
	public void setProductCategoryName(String productCategoryName) {
		this.productCategoryName = productCategoryName;
	}
	public String getProductLanchDate() {
		return productLanchDate;
	}
	public void setProductLanchDate(String productLanchDate) {
		this.productLanchDate = productLanchDate;
	}
	
}
