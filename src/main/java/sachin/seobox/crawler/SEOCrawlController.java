package sachin.seobox.crawler;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class SEOCrawlController extends CrawlController {

	public SEOCrawlController(CrawlConfig config, PageFetcher pageFetcher, RobotstxtServer robotstxtServer)
			throws Exception {
		super(config, pageFetcher, robotstxtServer);

	}

}
