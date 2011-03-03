package org.ssa4j.example.annotatted;

public class Manufacturer {

	private String value;
	
	public Manufacturer(String value) {
		this.value = value;
	}
	
	public String toString() {
		return String.format("Made by %s", this.value);
	}
}
