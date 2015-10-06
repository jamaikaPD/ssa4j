

# Introduction #
This tutorial assumes that you already have a working knowledge of the core concepts of [screen-scraper](http://community.screen-scraper.com/documentation).  It expands on [Tutorial 4: Scraping an E-commerce Site from External Programs](http://community.screen-scraper.com/Tutorial_4_Page_1) to demonstrate how SSA4J is used to create an API that is easier to use and more consistent with Java.

## Scraping From an External Java Program the "Old Way" ##

Before proceeding it would be a good idea to go through [Tutorial 2](http://community.screen-scraper.com/Tutorial_2_page_1), if you haven't done so already.

**NOTE** If you haven't gone through [Tutorial 2](http://community.screen-scraper.com/Tutorial_2_page_1), or don't still have the scraping session you created in it, you can download it [here](http://community.screen-scraper.com/files/Shopping%20Site%20(Scraping%20Session).sss) and [import](http://community.screen-scraper.com/importing_and_exporting_objects) it into screen-scraper.

... ok, now let's review how to [invoke screen-scraper from Java](http://community.screen-scraper.com/invoking_screen-scraper_from_Java) in a world without SSA4J.

```
RemoteScrapingSession remoteScrapingSession = new RemoteScrapingSession("Shopping Site");
remoteScrapingSession.setVariable( "EMAIL_ADDRESS", "test@test.com" );
remoteScrapingSession.setVariable( "PASSWORD", "testing" );
remoteScrapingSession.setVariable( "SEARCH","dvd");
remoteScrapingSession.setVariable( "PAGE", "1" );

remoteScrapingSession.scrape();

DataSet products = ( DataSet )remoteScrapingSession.getVariable( "PRODUCTS" );

for( int i = 0; i < products.getNumDataRecords(); i++ )
{
    DataRecord product = products.getDataRecord( i );
    System.out.println( "==============" );
    System.out.println( "Product #" + i );
    System.out.println( "Title: " + product.get( "TITLE" ) );
    System.out.println( "Model: " + product.get( "MODEL" ) );
    System.out.println( "Shipping Weight: " + product.get( "SHIPPING_WEIGHT" ) );
    System.out.println( "Manufactured By: " + product.get( "MANUFACTURED_BY" ) );
    System.out.println( "==============" );
}

// Be sure to disconnect from the server.
remoteScrapingSession.disconnect();

```

## Why the Old Way is "Bad" ##
Although the above code doesn't appear all that bad; there are some serious drawbacks to working directly with the `RemoteScrapingSession` class:

  1. Code is not self-documenting
  1. Impossible for someone that isn't familiar with the scrape code to know what the session variables are or what content is returned from the scrape
  1. Impossible to "mock" or in someway unit test without calling screen-scraper every time the code is invoked
  1. Extremely brittle, as the names of variables, datasets, and sessions change over time, the calling code "breaks"


# Creating a Java API for the Shopping Site WITHOUT using SSA4J #
It is important to understand what the alternative to SSA4J is to fully appreciate how much it simplifies the process of creating Java APIs around the `RemoteScrapingSession` libraries.

Below is a good example of all that you would have to do to create a Java API around `RemoteScrapingSession` for the example above.

## ShoppingSiteScrapingSession Class ##
```
// This is a subclass of RemoteScrapingSession that abstracts the caller
// from having to know anything about the underlying RemoteScrapingSession
// and exposes a "typed" interface for setting session variables and 
// retrieving product content that was scraped from the site.
public class ShoppingSiteScrapingSession {

    RemoteScrapingSession session;
    
    // This is a simple constructor that hides the string literal "Shopping Site"
    // from the caller.  This is useful in the event that this string changes
    // later during development it won't impact anyone creating this class.
    public ShoppingSiteScrapingSession() 
      throws UnknownHostException, RemoteScrapingSessionException, IOException
    {
        session = new RemoteScrapingSession("Shopping Site");
    }
  
    // Now we create a setter method for every session variable that we want
    // the caller to provide.  For each, we call this.setVariable(..)
    // with the String literal required for each session variable.  
    public void setEmailAddress(String emailAddress) throws RemoteScrapingSessionException {
        session.setVariable( "EMAIL_ADDRESS", emailAddress );
    }
    public void setPassword(String password) throws RemoteScrapingSessionException {
        session.setVariable( "PASSWORD",  password );
    }
    public void setSearchKeyWord(String searchKeyWord) throws RemoteScrapingSessionException {
        session.setVariable( "SEARCH", searchKeyWord );
    }
    public void setPage(int page) throws RemoteScrapingSessionException {
        session.setVariable( "PAGE", Integer.toString(page));
    }
    
    // Because we don't want to expose the raw the DataSet to the caller, we create
    // a method that converts the DataSet into an array of Product objects
    public Product[] getProducts() throws RemoteScrapingSessionException {
        DataSet dset = ( DataSet )session.getVariable( "PRODUCTS" );
        Product[] products = new Product[dset.getNumDataRecords()];
        for( int i = 0; i < products.length; i++ )
        {
            DataRecord rec = dset.getDataRecord( i );
            products[i] = new Product(rec);
        }
        return products;
    }

    public void scrape() throws RemoteScrapingSessionException {
        session.scrape();
    }
}
```

## Product Class ##
```
// This is a wrapper around DataRecord that abstracts the caller
// from having to know anything about the underlying DataRecord.
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
```

## Using the API ##
Finally we have a clean Java API that hides all the String literals and `RemoteScrapingSession` scaffolding.  Using it is simple and is very clear to anyone looking at the Java what properties to set and what values can be read from the session.

```
ShoppingSiteScrapingSession session = new ShoppingSiteScrapingSession();
session.setEmailAddress("test@test.com");
session.setPassword("testing");
session.setSearchKeyWord("dvd");
session.setPage(1);

session.scrape();

for(Product product : session.getProducts())
{
    System.out.println( "==============" );
    System.out.println( "Title: " + product.getTitle() );
    System.out.println( "Model: " + product.getModel() );
    System.out.println( "Shipping Weight: " + product.getWeight() );
    System.out.println( "Manufactured By: " + product.getManufacturer() );
    System.out.println( "==============" );
}
```

# Create a Java API for the Shopping Site using SSA4J #
Now that you have seen everything that you would have to do normally to create a Java API around a the `RemoteScrapingSession` libraries. Lets take the same example, but this time using SSA4J.

## ShoppingSiteScrapingSession Class (Annotated) ##
Notice right away how much less code is required.  In fact, this object looks like a basic POJO.  You don't even see a single reference to the `RemoteScrapingSession` libraries anywhere.

```
@ScrapeSession(name="Shopping Site")
public class ShoppingSiteScrapingSession {
    
    @ScrapeSessionVariable(name = "EMAIL_ADDRESS")
    protected String email;

    @ScrapeSessionVariable(name = "PASSWORD")
    protected String password;
    
    @ScrapeSessionVariable(name = "SEARCH")
    protected String searchKeyWord;
    
    @ScrapeSessionVariable(name = "PAGE")
    protected Integer page;

    @ScrapeDataSet("PRODUCTS")
    protected Product[] products;
    
    
    public void setEmailAddress(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public void setSearchKeyWord(String searchKeyWord) {
        this.searchKeyWord = searchKeyWord;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public Product[] getProducts() {
        return products;
    }
}
```

## Product Class (Annotated) ##
Notice again the complete absence of any reference to the `RemoteScrapingSession` libraries.  In particular there is no trace of `DataRecord` anywhere.
```
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
```

## Using the Annotated API ##
Using these annotated classes, kicking off a scrape session requires an instance of a [ScrapeSessionManager](SSA4JAPI#ScrapeSessionManager.md).  Below is an example using the [ProfessionalScrapeSessionManager](SSA4JAPI#ProfessionalScrapeSessionManager.md) but there is also a [EnterpriseScrapeSessionManager](SSA4JAPI#EnterpriseScrapeSessionManager.md) and a [MockScrapeSessionManager](SSA4JAPI#MockScrapeSessionManager.md).

```
ShoppingSiteScrapingSession session = new ShoppingSiteScrapingSession();
session.setEmailAddress("test@test.com");
session.setPassword("testing");
session.setSearchKeyWord("dvd");
session.setPage(1);

ScrapeSessionManager manager = new ProfessionalScrapeSessionManager();
manager.scrape(session);

for (Product product : session.getProducts()) {
    System.out.println("==============");
    System.out.println("Title: " + product.getTitle());
    System.out.println("Model: " + product.getModel());
    System.out.println("Shipping Weight: " + product.getWeight());
    System.out.println("Manufactured By: "+ product.getManufacturer());
    System.out.println("==============");
}      
```