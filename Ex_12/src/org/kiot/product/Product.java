package org.kiot.product;

public class Product {
	private int productCode, productPrice, quantityOnHand;
	private String productName;
	public Product() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Product(int productCode, int productPrice, int quantityOnHand, 
			String productName) {
		super();
		this.productCode = productCode;
		this.productPrice = productPrice;
		this.quantityOnHand = quantityOnHand;
		this.productName = productName;
	}
	public int getProductCode() {
		return productCode;
	}
	public void setProductCode(int productCode) {
		this.productCode = productCode;
	}
	public int getProductPrice() {
		return productPrice;
	}
	public void setProductPrice(int productPrice) {
		this.productPrice = productPrice;
	}
	public int getQuantityOnHand() {
		return quantityOnHand;
	}
	public void setQuantityOnHand(int quantityOnHand) {
		this.quantityOnHand = quantityOnHand;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + productCode;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Product other = (Product) obj;
		if (productCode != other.productCode)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "product [productCode=" + productCode + ", productPrice=" 
	        + productPrice + ", quantityOnHand=" + quantityOnHand
			+ ", productName=" + productName + "]";
	}
}
