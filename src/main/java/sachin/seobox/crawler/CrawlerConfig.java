package sachin.seobox.crawler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

import org.slf4j.LoggerFactory;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.authentication.BasicAuthInfo;
import sachin.seobox.helpers.HelperUtils;

public class CrawlerConfig {
	public static String site;
	public static String user;
	public static String pass;
	public static final Properties PROPERTIES;
	public static final List<String> SKIPPED_URLS;
	public static final String crawlStorageFolder;
	public static final String dataLocation;
	public static final Pattern pattern;
	public static final boolean caseSensitive;
	public static String PROPERTIES_LOC = "";
	public static String reportPath;

	static {
		PROPERTIES = new Properties();
		SKIPPED_URLS = new ArrayList<>();
		PROPERTIES_LOC = System.getProperty("CrawlerConfigFile");
		try {
			if (PROPERTIES_LOC == null || PROPERTIES_LOC.isEmpty() || !PROPERTIES_LOC.contains(".properties")
					|| !new File(PROPERTIES_LOC).exists()) {
				System.out.println("Loading default config file");
				PROPERTIES_LOC = HelperUtils.getResourceFile("Config.properties");
			}
			FileInputStream in = new FileInputStream(new File(PROPERTIES_LOC));
			PROPERTIES.load(in);
			in.close();
			String[] sites = CrawlerConfig.PROPERTIES.getProperty("crawler.skipDomains").split(",");
			for (String site : sites) {
				SKIPPED_URLS.add(new URL(site).getHost().replaceAll("www.", ""));
			}
		} catch (IOException e) {
			LoggerFactory.getLogger(CrawlerConfig.class).debug("Error in loading config file", e);
		}
		site = System.getProperty("SiteAddress");
		user = System.getProperty("Username");
		pass = System.getProperty("Password");
		File storage = null;
		if (PROPERTIES.getProperty("crawler.storageFolder", System.getProperty("user.dir") + File.separator + "temp")
				.isEmpty()) {
			storage = new File(System.getProperty("user.dir") + File.separator + "temp");
		} else {
			storage = new File(PROPERTIES.getProperty("crawler.storageFolder"));
		}
		storage.mkdirs();
		crawlStorageFolder = storage.getAbsolutePath();
		String host = "";
		try {
			host = new URL(site).getHost().replaceAll("www.", "");
		} catch (MalformedURLException e) {
			LoggerFactory.getLogger(CrawlerConfig.class).debug("Error in loading config file", e);
		}
		dataLocation = crawlStorageFolder + File.separator + host;
		new File(dataLocation).mkdirs();
		pattern = Pattern.compile(PROPERTIES.getProperty("crawler.urlRegex", "."), Pattern.CASE_INSENSITIVE);
		caseSensitive = Boolean.parseBoolean(PROPERTIES.getProperty("crawler.caseSensitiveUrl", "false"));
	}

	public CrawlConfig getConfig() {
		CrawlConfig config = new CrawlConfig();
		config.setSiteUrl(site);
		if (null != user && !user.isEmpty()) {
			try {
				config.addAuthInfo(new BasicAuthInfo(user, pass, site));
			} catch (MalformedURLException e) {
				LoggerFactory.getLogger(CrawlerConfig.class).debug("Error in controller", e);
			}
		}
		config.setUserAgentString(PROPERTIES.getProperty("crawler.userAgentString",
				"Mozilla/5.0 (Windows NT 10.0; WOW64; rv:48.0) Gecko/20100101 Firefox/48.0"));
		config.setCrawlStorageFolder(crawlStorageFolder);
		config.setConnectionTimeout(Integer.parseInt(PROPERTIES.getProperty("crawler.connectionTimeout", "20000")));
		config.setSocketTimeout(Integer.parseInt(PROPERTIES.getProperty("crawler.connectionTimeout", "20000")));
		config.setFollowRedirects(Boolean.parseBoolean(PROPERTIES.getProperty("crawler.followRedirects", "true")));
		config.setPolitenessDelay(Integer.parseInt(PROPERTIES.getProperty("crawler.URLHitDelay", "200")));
		config.setProcessBinaryContentInCrawling(
				Boolean.parseBoolean(PROPERTIES.getProperty("crawler.binaryContent", "true")));
		config.setMaxDownloadSize(Integer
				.parseInt(PROPERTIES.getProperty("crawler.maxDownloadSize", Integer.toString(Integer.MAX_VALUE))));
		// config.setResumableCrawling(true);
		return config;

	}

}
