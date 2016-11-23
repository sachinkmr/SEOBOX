package sachin.seobox.crawler;

import java.io.File;
import java.util.regex.Matcher;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.url.WebURL;
import sachin.seobox.common.SEOConfig;
import sachin.seobox.helpers.StreamUtils;
import sachin.seobox.seo.SEOPage;

public class Crawler extends WebCrawler {
    private StreamUtils streamUtils = new StreamUtils();

    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
	Matcher m = SEOConfig.shouldVisitPattern.matcher(url.getURL());
	return !SEOConfig.SKIPPED_URLS.contains(url.getModifiedHost())
		&& (m.find() || SEOConfig.ASSETS_PATTERN.matcher(url.getURL()).find());
    }

    @Override
    public void visit(Page page) {
	// System.out.println("Visiting: " + page.getWebURL().getURL());
	File file = new File(SEOConfig.dataLocation, page.getWebURL().hashCode() + ".webUrl");
	SEOPage seoPage = new SEOPage(page);
	streamUtils.writeFile(file, seoPage);
    }

}
