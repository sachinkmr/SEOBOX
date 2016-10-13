package sachin.seobox.common;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import sachin.seobox.crawler.Crawler;
import sachin.seobox.crawler.CrawlerConfig;

public class TestInitializer {
    protected static final Logger logger = LoggerFactory.getLogger(TestInitializer.class);

    @BeforeSuite(enabled = true)
    public void init() {
	int numberOfCrawlers = Integer.parseInt(SEOConfig.PROPERTIES.getProperty("crawler.numberOfCrawlers", "30"));
	CrawlerConfig control = new CrawlerConfig();
	CrawlConfig config = control.getConfig();
	PageFetcher pageFetcher = new PageFetcher(config);
	RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
	robotstxtConfig.setEnabled(false);
	RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
	try {
	    System.out.println("Please wait crawling site");
	    CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
	    controller.start(Crawler.class, numberOfCrawlers);
	} catch (Exception e) {
	    logger.debug("Error in controller", e);
	    System.out.println("Error in application: " + e);

	} finally {
	    System.gc();
	}
	System.out.println("\nExecuting Test Cases");
	System.out.println("---------------------------------------");
    }

    @AfterSuite(enabled = false)
    public void afterSuite() {
	FileUtils.deleteQuietly(new File("CrawlerConfigFile"));
	FileUtils.deleteQuietly(new File(SEOConfig.crawlStorageFolder));
	System.gc();
    }
}
