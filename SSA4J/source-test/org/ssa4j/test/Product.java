package org.ssa4j.test;

import org.ssa4j.ScrapeDataRecord;
import org.ssa4j.ScrapeDataRecordField;

@ScrapeDataRecord
public class Product {

	@ScrapeDataRecordField(name="TITLE")
	public String title;
	@ScrapeDataRecordField(name="MODEL")
	public String model;
	@ScrapeDataRecordField(name="SHIPPING_WEIGHT")
	public String weight;
	@ScrapeDataRecordField(name="MANUFACTURED_BY")
	public String manufacturer;

}
