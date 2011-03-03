package org.ssa4j.example.traditional;

import com.screenscraper.common.DataRecord;

public class Product {

	private DataRecord rec;
	
	public Product(DataRecord rec) {
		this.rec = rec;
	}

	public String getTitle() {
		return (String) rec.get( "TITLE" );
	}
	
	public String getModel() {
		return (String) rec.get( "MODEL" );
	}
	
	public String getWeight() {
		return (String) rec.get( "SHIPPING_WEIGHT" );
	}
	
	public String getManufacturer() {
		return (String) rec.get( "MANUFACTURED_BY" );
	}

}
