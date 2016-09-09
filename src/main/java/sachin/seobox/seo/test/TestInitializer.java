package sachin.seobox.seo.test;

import java.io.File;

import org.apache.commons.io.FileUtils;
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

	@BeforeSuite(enabled = true)
	public void init() {
		int numberOfCrawlers = Integer.parseInt(CrawlerConfig.PROPERTIES.getProperty("crawler.numberOfCrawlers", "30"));
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
			LoggerFactory.getLogger(CrawlerConfig.class).debug("Error in controller", e);
			e.printStackTrace();
			System.exit(1);
		}
	}

	@AfterSuite(enabled = true)
	public void afterSuite() {
		FileUtils.deleteQuietly(new File(CrawlerConfig.crawlStorageFolder));
	}
}
