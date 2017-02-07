package sachin.seobox.crawler;

import java.io.File;
import java.util.regex.Matcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.url.WebURL;
import sachin.seobox.helpers.StreamUtils;
import sachin.seobox.parameters.PageSpeed;
import sachin.seobox.parameters.SEOPage;
import sachin.seobox.parameters.StructuredData;

public class SEOCrawler extends WebCrawler {
	private StreamUtils streamUtils = new StreamUtils();
	protected static final Logger logger = LoggerFactory.getLogger(SEOCrawler.class);

	@Override
	public boolean shouldVisit(Page referringPage, WebURL url) {
		// return url.getURL().equals("http://www.liptontea.com");
		Matcher m = CrawlerConstants.SHOULD_VISIT_PATTERN.matcher(url.getURL());
		return !CrawlerConstants.SKIPPED_URLS.contains(url.getModifiedHost())
				&& (m.find() || CrawlerConstants.ASSETS_PATTERN.matcher(url.getURL()).find());
	}

	@Override
	public void visit(Page page) {
		File file = new File(CrawlerConstants.DATA_LOCATION, page.getWebURL().hashCode() + ".webUrl");
		SEOPage seoPage = new SEOPage(page);
		streamUtils.writeFile(file, seoPage);
		try {
			if (page.getWebURL().isInternalLink() && page.getStatusCode() == 200
					&& page.getContentType().contains("text/html")) {

				SEOCrawlerConfig config = (SEOCrawlerConfig) this.getMyController().getConfig();
				if (CrawlerConstants.TESTS.contains("pageSpeed")) {
					config.getExecutor().execute(new PageSpeed(page.getWebURL().getURL()));
				}
				if (CrawlerConstants.TESTS.contains("structuredData")) {
					config.getExecutor().execute(new StructuredData(page.getWebURL().getURL(), seoPage.getHtml()));
				}
			}
		} catch (Exception ex) {
			logger.debug("3rd party api error for url: " + page.getWebURL().getURL(), ex);
		}
	}

}
