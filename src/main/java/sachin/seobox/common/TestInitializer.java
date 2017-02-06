package sachin.seobox.common;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import sachin.seobox.crawler.CrawlerConstants;
import sachin.seobox.crawler.SEOCrawler;
import sachin.seobox.crawler.SEOCrawlerConfig;
import sachin.seobox.exception.SEOException;

public class TestInitializer {
    protected static final Logger logger = LoggerFactory.getLogger(TestInitializer.class);

    @BeforeSuite(enabled = true)
    public void init() throws SEOException {
	int numberOfCrawlers = Integer
		.parseInt(CrawlerConstants.PROPERTIES.getProperty("crawler.numberOfCrawlers", "30"));
	SEOCrawlerConfig config = new SEOCrawlerConfig().getConfig();
	PageFetcher pageFetcher = new PageFetcher(config);
	RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
	robotstxtConfig.setEnabled(false);
	RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
	long start = System.currentTimeMillis();
	try {
	    System.out.println("Please wait crawling site....");
	    CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
	    controller.start(SEOCrawler.class, numberOfCrawlers);
	    config.getExecutor().shutdown();
	    while (!config.getExecutor().isTerminated()) {
		logger.info("Waiting 5 seconds for 3rd party API requests to finish...");
		Thread.sleep(5000);
	    }
	    CrawlerConstants.CRAWLING_TIME = System.currentTimeMillis() - start;
	} catch (Exception e) {
	    logger.debug("Error in controller", e);
	    System.out.println("Error in application: " + e);
	    CrawlerConstants.ERROR = true;
	    CrawlerConstants.ERROR_TEXT = "URL is down, something went wrong or there is some error in faching URL data. Please review log for more detail. <br/> Error: "
		    + e.getMessage();
	}
	System.out.println("\nExecuting Test Cases");
	System.out.println("---------------------------------------");
    }

    @AfterSuite(enabled = true)
    public void afterSuite() {
	FileUtils.deleteQuietly(new File("CrawlerConfigFile"));
	FileUtils.deleteQuietly(new File(CrawlerConstants.CRAWL_STORAGE_FOLDER));
    }
}
