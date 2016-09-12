package sachin.seobox.crawler;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;

import org.slf4j.LoggerFactory;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.url.WebURL;
import sachin.seobox.helpers.StreamUtils;
import sachin.seobox.seo.SEOPage;

public class Crawler extends WebCrawler {
	StreamUtils stream;

	@Override
	public void onBeforeExit() {
		try {
			stream.closeStreams();
		} catch (IOException e) {
			LoggerFactory.getLogger(Crawler.class).debug("Unable to close streams " + e);
		}
	}

	@Override
	public void onStart() {
		stream = new StreamUtils();
	}

	@Override
	public boolean shouldVisit(Page referringPage, WebURL url) {
		Matcher m = CrawlerConfig.pattern.matcher(url.getURL());
		return !CrawlerConfig.SKIPPED_URLS.contains(url.getModifiedHost()) && m.find();
	}

	@Override
	public void visit(Page page) {
		if (page.getWebURL().isInternalLink()) {
			File file = new File(CrawlerConfig.dataLocation, page.getWebURL().hashCode() + ".webUrl");
			try {
				SEOPage seoPage = new SEOPage(page);
				stream.writeFile(file, seoPage);
			} catch (IOException e) {
				LoggerFactory.getLogger(Crawler.class).debug("Unable to write data for " + page.getWebURL().getURL(),
						e);
			}
		}
	}

}
