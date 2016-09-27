package sachin.seobox.seo.test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
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
import sachin.seobox.helpers.HelperUtils;
import sachin.seobox.seo.SEOPage;

public class TestInitializer {
	public static List<SEOPage> internalPages = new ArrayList<>();

	@BeforeSuite(enabled = true)
	public void init() {
		try {
			int code = HelperUtils.getUrlResponse(CrawlerConfig.site, CrawlerConfig.user, CrawlerConfig.pass)
					.returnResponse().getStatusLine().getStatusCode();
			if (code != 200) {
				LoggerFactory.getLogger(CrawlerConfig.class).error("\n\nSite is giving " + code + " status code.\n\n");
				System.exit(1);
			}
		} catch (ParseException e1) {
			LoggerFactory.getLogger(CrawlerConfig.class).debug("Site is not running", e1);
		} catch (ClientProtocolException e1) {
			LoggerFactory.getLogger(CrawlerConfig.class).debug("Site is not running", e1);
		} catch (IOException e1) {
			LoggerFactory.getLogger(CrawlerConfig.class).debug("Site is not running", e1);
		}
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
