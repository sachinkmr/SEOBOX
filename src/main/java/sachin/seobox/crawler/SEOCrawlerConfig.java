package sachin.seobox.crawler;

import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.message.BasicHeader;
import org.slf4j.LoggerFactory;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.authentication.BasicAuthInfo;

public class SEOCrawlerConfig extends CrawlConfig {
	private ExecutorService executor;

	public SEOCrawlerConfig getConfig() {
		this.setSiteUrl(CrawlerConstants.SITE);
		if (null != CrawlerConstants.USERNAME && !CrawlerConstants.USERNAME.isEmpty()) {
			try {
				this.addAuthInfo(new BasicAuthInfo(CrawlerConstants.USERNAME, CrawlerConstants.PASSWORD, CrawlerConstants.SITE));
			} catch (MalformedURLException e) {
				LoggerFactory.getLogger(SEOCrawlerConfig.class).debug("Error in controller", e);
			}
		}
		this.setUserAgentString(CrawlerConstants.USER_AGENT);
		this.setUserAgentString(CrawlerConstants.PROPERTIES.getProperty("crawler.userAgentString", "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:48.0) Gecko/20100101 Firefox/48.0"));
		this.setCrawlStorageFolder(CrawlerConstants.CRAWL_STORAGE_FOLDER);
		this.setConnectionTimeout(Integer.parseInt(CrawlerConstants.PROPERTIES.getProperty("crawler.connectionTimeout", "120000")));
		this.setSocketTimeout(Integer.parseInt(CrawlerConstants.PROPERTIES.getProperty("crawler.connectionTimeout", "120000")));
		this.setFollowRedirects(Boolean.parseBoolean(CrawlerConstants.PROPERTIES.getProperty("crawler.followRedirects", "true")));
		this.setPolitenessDelay(Integer.parseInt(CrawlerConstants.PROPERTIES.getProperty("crawler.URLHitDelay", "200")));
		this.setProcessBinaryContentInCrawling(false);
		this.setIncludeBinaryContentInCrawling(Boolean.parseBoolean(CrawlerConstants.PROPERTIES.getProperty("crawler.binaryContent", "true")));
		this.setIncludeHttpsPages(true);
		this.setMaxDownloadSize(Integer.parseInt(CrawlerConstants.PROPERTIES.getProperty("crawler.maxDownloadSize", Integer.toString(Integer.MAX_VALUE))));
		Set<BasicHeader> headers = new HashSet<>(this.getDefaultHeaders());
		// headers.add(new BasicHeader("Accept-Encoding", "gzip, deflate"));
		headers.add(new BasicHeader("Accept-Encoding", "gzip, compress, deflate, br, identity, exi, pack200-gzip, bzip2, lzma, peerdist, sdch, xpress, xz"));
		this.setDefaultHeaders(headers);
		executor = Executors.newFixedThreadPool(50);
		this.setMaxPagesToFetch(Integer.parseInt(CrawlerConstants.PROPERTIES.getProperty("crawler.maxLinksToFetch", "-1")));
		return this;
	}

	public ExecutorService getExecutor() {
		return this.executor;
	}

}
