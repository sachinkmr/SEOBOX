package sachin.seobox.crawler;

import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.Set;

import org.apache.http.message.BasicHeader;
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

		config.setUserAgentString(SEOConfig.USER_AGENT);
		config.setCrawlStorageFolder(SEOConfig.crawlStorageFolder);
		config.setConnectionTimeout(
				Integer.parseInt(SEOConfig.PROPERTIES.getProperty("crawler.connectionTimeout", "120000")));
		config.setSocketTimeout(
				Integer.parseInt(SEOConfig.PROPERTIES.getProperty("crawler.connectionTimeout", "120000")));
		config.setFollowRedirects(
				Boolean.parseBoolean(SEOConfig.PROPERTIES.getProperty("crawler.followRedirects", "true")));
		config.setPolitenessDelay(Integer.parseInt(SEOConfig.PROPERTIES.getProperty("crawler.URLHitDelay", "200")));
		config.setProcessBinaryContentInCrawling(false);
		config.setIncludeBinaryContentInCrawling(
				Boolean.parseBoolean(SEOConfig.PROPERTIES.getProperty("crawler.binaryContent", "true")));
		config.setIncludeHttpsPages(true);
		config.setMaxDownloadSize(Integer.parseInt(
				SEOConfig.PROPERTIES.getProperty("crawler.maxDownloadSize", Integer.toString(Integer.MAX_VALUE))));
		Set<BasicHeader> headers = new HashSet<>(config.getDefaultHeaders());
		headers.add(new BasicHeader("Accept-Encoding", "*"));
		config.setDefaultHeaders(headers);
		// config.setResumableCrawling(true);
		return config;
	}
}
