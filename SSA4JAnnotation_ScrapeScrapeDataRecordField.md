This annotation is used to bind a member variable or setter method to a field of a DataRecord.

## Example: Annotating a Member Variable ##
```
package org.ssa4j.example.annotatted;

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
```

## Example: Annotating a Setter Method ##
```
package org.ssa4j.example.annotatted;

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
  public String getModel() {
    return model;
  }
  
  public String getWeight() {
    return weight;
  }
  
  public String getManufacturer() {
    return manufacturer;
  }

  @ScrapeDataRecordField(name="TITLE")
  public void setTitle(String title) {
    this.title = title;
  }
  
  @ScrapeDataRecordField(name="MODEL")
  public void setModel(String model) {
    this.model = model;
  }
  
  @ScrapeDataRecordField(name="SHIPPING_WEIGHT")
  public void setWeight(String weight) {
    this.weight = weight;
  }
  
  @ScrapeDataRecordField(name="MANUFACTURED_BY")
  public void setManufacturer(String manufacturer) {
    this.manufacturer = manufacturer;
  }
}
```