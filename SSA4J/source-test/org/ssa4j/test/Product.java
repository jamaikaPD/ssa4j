package org.ssa4j.test;

import org.ssa4j.ScrapeDataRecord;
import org.ssa4j.ScrapeDataRecordField;

@ScrapeDataRecord
public class Product {

	private String title;
	private String model;
	private String weight;
	private String manufacturer;
	
	public String getTitle() {
		return title;
	}
	
	@ScrapeDataRecordField(name="TITLE")
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getModel() {
		return model;
	}
	
	@ScrapeDataRecordField(name="MODEL")
	public void setModel(String model) {
		this.model = model;
	}
	
	public String getWeight() {
		return weight;
	}
	
	@ScrapeDataRecordField(name="SHIPPING_WEIGHT")
	public void setWeight(String weight) {
		this.weight = weight;
	}
	
	public String getManufacturer() {
		return manufacturer;
	}
	
	@ScrapeDataRecordField(name="MANUFACTURED_BY")
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	
	
}
