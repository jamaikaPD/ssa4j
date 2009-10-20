package org.ssa4j.example.annotatted;

import org.ssa4j.ScrapeDataRecord;
import org.ssa4j.ScrapeDataRecordField;

@ScrapeDataRecord
public class Product {

	@ScrapeDataRecordField(name="TITLE")
	private String title;
	public String getTitle() {
		return title;
	}
	
	@ScrapeDataRecordField(name="MODEL")
	private String model;
	public String getModel() {
		return model;
	}
	
	@ScrapeDataRecordField(name="SHIPPING_WEIGHT")
	private String weight;
	public String getWeight() {
		return weight;
	}
	
	@ScrapeDataRecordField(name="MANUFACTURED_BY")
	private String manufacturer;
	public String getManufacturer() {
		return manufacturer;
	}

}
