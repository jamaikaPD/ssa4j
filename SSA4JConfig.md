

# Introduction #

SSA4J Extensions (Professional, Enterprise, and Mock) all have configuration properties that can be set to override the default values.


# System Properties #

## SSA4J General Properties ##
| **Property**   | **Default** | **Description** |
|:---------------|:------------|:----------------|
| ssa4j.manager  | n/a         | The class name for the ScrapeSessionManager implementation to create when calls to ScrapeSessionManager.createScraper() are called. |

## SSA4J Professional Extension Properties ##
| **Property**   | **Default** | **Description** |
|:---------------|:------------|:----------------|
| ssa4j.host     | localhost   | The hostname of the screen-scraper server. |
| ssa4j.port     | 8778        | The port that the screen-scraper server is running on. |
| ssa4j.timeout  | 1           | The timeout value in minutes.  This will cause the ScrapeManager to stop waiting for a response from a Screen-Scraper server after a certain amount of time. |
| ssa4j.screenscraper.home | n/a         | The location of the screen-scraper installation.  **NOTE** this is only required if you want to use the ProfessionalScrapeScriptDeployer |

## SSA4J Enterprise Extension Properties ##
| **Property**   | **Default** | **Description** |
|:---------------|:------------|:----------------|
| ssa4j.host     | localhost   | The hostname of the screen-scraper server. |
| ssa4j.port     | 8779        | The port that the screen-scraper server is running on. |
| ssa4j.timeout  | 1           | The timeout value in minutes.  This will cause the ScrapeManager to stop waiting for a response from a Screen-Scraper server after a certain amount of time. |

## SSA4J Mock Extension Properties ##
| **Property**   | **Default** | **Description** |
|:---------------|:------------|:----------------|
| ssa4j.mockdir  | n/a         | The directory within which the MockScrapeSessionManager will try to find MockSession files. |