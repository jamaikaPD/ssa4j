# What's in the Distribution? #

The distribution comes with the core SSA4J libraries and extensions for Professional and Enterprise editions of Screen-Scraper.  This distribution also comes bundled with all 3rd party dependencies.

**SSA4J Core** (and 3rd party dependencies)

/ssa4j-core.jar

/lib/core/**.jar**

**SSA4J Professional Extensions**

/ssa4j-pro.jar

**SSA4J Enterprise Extensions** (and 3rd party dependencies)

/ssa4j-ent.jar

/lib/enterprise/**.jar**

**SSA4J Mock Extensions** (and 3rd party dependencies)

/ssa4j-mock.jar

/lib/mock/**.jar**

# Installing SSA4J #

There isn't much to installing SSA4J.  All that is required is that you place all the JARs in your classpath.  If you do not own the Enterprise edition then you can exclude any JAR related to the Enterprise Extensions.  Likewise, if you aren't using the Mock Extensions, then leave out those JARs as well.

**NOTE** SSA4J assumes that you already have an installation of either [Screen-Scraper Professional](http://www.screen-scraper.com/download/pro_choose_platform.php) or [Screen-Scraper Enterprise](http://www.screen-scraper.com/download/enterprise_choose_platform.php).

**IMPORTANT** SSA4J **WILL NOT WORK** with the [Screen-Scraper Basic Edition](http://www.screen-scraper.com/download/basic_choose_platform.php).

# Configuration Properties #

Depending on which SSA4J Extension you choose (Professional, Enterprise, or Mock) there are certain
System properties that can be set to configure various settings for each Extension.

Here is a complete list:

