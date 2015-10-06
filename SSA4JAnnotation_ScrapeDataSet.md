This annotation can be used on either a field or setter method to bind to a [DataSet](http://community.screen-scraper.com/API/DataSet) in a scrape session.

**IMPORTANT** This annotation MUST be used with an Array of Classes that are also annotated with the [@ScrapeDataRecord](SSA4JAnnotation_ScrapeDataRecord.md) annotation.

## Example 1: Basic Usage on a Member Field ##

```
@ScrapeSession("Shopping Site")
public class ShoppingSiteScrapeSession {

   @ScrapeDataSet("PRODUCTS")
   public Product[] products;

}

```

## Example 2: Basic Usage on a Setter Method ##

```
@ScrapeSession("Shopping Site")
public class ShoppingSiteScrapeSession {

   
   protected Product[] products;

   @ScrapeDataSet("PRODUCTS")
   public void setProducts(Product[] products) {
      this.products = products;
   }

}

```