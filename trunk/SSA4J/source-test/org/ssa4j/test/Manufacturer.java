package org.ssa4j.test;

public class Manufacturer {

	private String value;
	
	public Manufacturer(String value) {
		this.value = value;
	}
	
	public String toString() {
		return String.format("Made by %s", this.value);
	}
}
