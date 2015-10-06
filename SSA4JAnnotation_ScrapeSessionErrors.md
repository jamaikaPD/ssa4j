This annotation enables the caller program to map session variables as error messages scraped from the page.  If present in the session, the variables are collected and thrown as a single [ScrapeSessionException](SSA4JAPI#ScrapeSessionException.md) when a [ScrapeSessionManager](SSA4JAPI#ScrapeSessionManager.md) completes its scrape.

| Attribute | Description |
|:----------|:------------|
| value     | An array of [@ScrapeSessionError](#@ScrapeSessionError.md) |

## @ScrapeSessionError ##
| Attribute | Description |
|:----------|:------------|
| code      | An error code, can be anything |
| name      | Session Variable name that, if present, signals the presence of an error |


## Example Basic Usage ##
```
@ScrapeSession(name="Shopping Site")
@ScrapeSessionErrors({
    @ScrapeSessionError(code=100,name="INVALID_LOGIN"),
    @ScrapeSessionError(code=200,name="SESSION_EXPIRED")
})
public class ShoppingSiteScrapeSession {
    ...
}
```