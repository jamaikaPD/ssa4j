Using an instance of [MockScrapeSessionManager](SSA4JAPI#MockScrapeSessionManager.md) you can create XML files that contain "scenarios" of possible responses from screen-scraper.

## Example: Mocking the "Shopping Site" Session ##

Here is an example of how we would mock 3 possible responses from screen-scraper for the example outlined in the [Shopping Site Tutorial](SSA4JTutorial.md).

```
<?xml version="1.0" encoding="UTF-8"?>
<ssa4j:mocksession xmlns:ssa4j="http://schemas.mobuser.com/ssa4jmock" 
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
    xsi:schemaLocation="http://ssa4j.googlecode.com/files/ssa4jmock-0.7.0.xsd" id="Shopping Site">
    
    <script lang="groovy">
    switch(session.page) {
    case 1:
        return "firstPage";
    case 2:
        return "secondPage";
    case 3:
        return "thirdPage";
    }
    </script>
    
    <scenario id="firstPage">
        
        <variables>
            <variable name="zenid">2DFS3423SEAAFE3487</variable>
        </variables>

        <dataset id="PRODUCTS">
            <datarecord>
                <field name="TITLE">A Bug's Life</field>
                <field name="MODEL">DVD-ABUG</field>
                <field name="SHIPPING_WEIGHT">7.00</field>
                <field name="MANUFACTURED_BY">Warner</field>
            </datarecord> 
            <datarecord>
                <field name="TITLE">A Bug's Life "Multi Pak"</field>
                <field name="MODEL">DVD-ABUG</field>
                <field name="SHIPPING_WEIGHT">7.00</field>
                <field name="MANUFACTURED_BY">Warner</field>
            </datarecord> 
            <datarecord>
                <field name="TITLE">Beloved</field>
                <field name="MODEL">DVD-BELOVED</field>
                <field name="SHIPPING_WEIGHT">7.00</field>
                <field name="MANUFACTURED_BY">Warner</field>
            </datarecord> 
            <datarecord>
                <field name="TITLE">Blade Runner - Director's Cut</field>
                <field name="MODEL">DVD-BLDRNDC</field>
                <field name="SHIPPING_WEIGHT">7.00</field>
                <field name="MANUFACTURED_BY">Warner</field>
            </datarecord> 
            <datarecord>
                <field name="TITLE">Courage Under Fire</field>
                <field name="MODEL">DVD-CUFI</field>
                <field name="SHIPPING_WEIGHT">7.00</field>
                <field name="MANUFACTURED_BY">Fox</field>
            </datarecord> 
            <datarecord>
                <field name="TITLE">Die Hard With A Vengeance</field>
                <field name="MODEL">DVD-DHWV</field>
                <field name="SHIPPING_WEIGHT">7.00</field>
                <field name="MANUFACTURED_BY">Fox</field>
            </datarecord>
        </dataset>

    </scenario>
    <scenario id="secondPage">
        
        <variables>
            <variable name="zenid">2DFS3423SEAAFE3487</variable>
        </variables>

        <dataset id="PRODUCTS"> 
            <datarecord>
                <field name="TITLE">Fire Down Below</field>
                <field name="MODEL">DVD-FDBL</field>
                <field name="SHIPPING_WEIGHT">7.00</field>
                <field name="MANUFACTURED_BY">Warner</field>
            </datarecord> 
            <datarecord>
                <field name="TITLE">Frantic</field>
                <field name="MODEL">DVD-FRAN</field>
                <field name="SHIPPING_WEIGHT">7.00</field>
                <field name="MANUFACTURED_BY">Warner</field>
            </datarecord> 
        </dataset>

    </scenario>
    <scenario id="thirdPage">

        <variables>
            <variable name="zenid">2DFS3423SEAAFE3487</variable>
        </variables>

    </scenario>

</ssa4j:mocksession>
```