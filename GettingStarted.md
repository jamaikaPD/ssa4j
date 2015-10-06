#Quick guide that will be you started in minutes

# Introduction #

Using the ssa4j could not be simpler. Once you've annotated your POJO, kicking off a scraping session is done in two lines of code ...

```
// Create a POJO that is properly annotated using ScreenScraper Annotations
FlightStatusScraper pojo = ...

// Create an instance of a ScrapeSessionManager ...
ScrapeSessionManager manager = ScrapeSessionManager.createScrapeSessionManager();

// And now start the scrape!
manager.scrape(pojo);

```