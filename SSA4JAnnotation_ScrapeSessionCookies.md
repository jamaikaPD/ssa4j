This annotation enables the caller program to selectively save a given list of session variables as a persistent value that is automatically saved in a "cookie-jar" and reflected back as session variables in future calls.

**NOTE:** These annotations are designed to work in concert with the [ScrapeSessionManager.scrape(source, listener, cookiejar)](SSA4JAPI#ScrapeSessionManager.md)

## Example: Basic Usage ##
```
@ScrapeSessionCookies({ 
    @ScrapeSessionCookie("zenid") 
})
public class ShoppingSiteScrapingSession {
    ...
}
```