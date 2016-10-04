package sachin.seobox.crawler;

import java.net.MalformedURLException;

import org.slf4j.LoggerFactory;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.authentication.BasicAuthInfo;
import sachin.seobox.common.SEOConfig;

public class CrawlerConfig {

	public CrawlConfig getConfig() {
		CrawlConfig config = new CrawlConfig();
		config.setSiteUrl(SEOConfig.site);
		if (null != SEOConfig.user && !SEOConfig.user.isEmpty()) {
			try {
				config.addAuthInfo(new BasicAuthInfo(SEOConfig.user, SEOConfig.pass, SEOConfig.site));
			} catch (MalformedURLException e) {
				LoggerFactory.getLogger(CrawlerConfig.class).debug("Error in controller", e);
			}
		}
		config.setUserAgentString(SEOConfig.PROPERTIES.getProperty("crawler.userAgentString",
				"Mozilla/5.0 (Windows NT 10.0; WOW64; rv:48.0) Gecko/20100101 Firefox/48.0"));
		config.setCrawlStorageFolder(SEOConfig.crawlStorageFolder);
		config.setConnectionTimeout(
				Integer.parseInt(SEOConfig.PROPERTIES.getProperty("crawler.connectionTimeout", "20000")));
		config.setSocketTimeout(
				Integer.parseInt(SEOConfig.PROPERTIES.getProperty("crawler.connectionTimeout", "20000")));
		config.setFollowRedirects(
				Boolean.parseBoolean(SEOConfig.PROPERTIES.getProperty("crawler.followRedirects", "true")));
		config.setPolitenessDelay(Integer.parseInt(SEOConfig.PROPERTIES.getProperty("crawler.URLHitDelay", "200")));
		config.setProcessBinaryContentInCrawling(
				Boolean.parseBoolean(SEOConfig.PROPERTIES.getProperty("crawler.binaryContent", "true")));
		config.setIncludeBinaryContentInCrawling(
				Boolean.parseBoolean(SEOConfig.PROPERTIES.getProperty("crawler.binaryContent", "true")));
		config.setIncludeHttpsPages(true);
		config.setMaxDownloadSize(Integer.parseInt(
				SEOConfig.PROPERTIES.getProperty("crawler.maxDownloadSize", Integer.toString(Integer.MAX_VALUE))));
		// config.setResumableCrawling(true);
		return config;
	}
}
