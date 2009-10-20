package org.ssa4j.example.annotatted;

import org.ssa4j.ScrapeDataRecord;
import org.ssa4j.ScrapeDataRecordField;

@ScrapeDataRecord
public class Product {

	@ScrapeDataRecordField(name="TITLE")
	private String title;
	
	@ScrapeDataRecordField(name="MODEL")
	private String model;
	
	@ScrapeDataRecordField(name="SHIPPING_WEIGHT")
	private String weight;

	@ScrapeDataRecordField(name="MANUFACTURED_BY")
	private String manufacturer;

	public String getTitle() {
		return title;
	}
	public String getModel() {
		return model;
	}
	
	public String getWeight() {
		return weight;
	}
	
	public String getManufacturer() {
		return manufacturer;
	}

}
