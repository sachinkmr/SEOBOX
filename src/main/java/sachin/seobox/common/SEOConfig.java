package sachin.seobox.common;

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

import sachin.seobox.helpers.HelperUtils;

public class SEOConfig {

	public static final int URL_CHARACTERS_LIMIT;
	public static final int H1_CHARACTERS_LIMIT;
	public static final int H2_CHARACTERS_LIMIT;
	public static final int TITLE_CHARACTERS_LIMIT;
	public static final int TITLE_CHARACTERS_LIMIT_MIN;
	public static final int META_DESCRIPTION_CHARACTERS_LIMIT;
	public static final int META_KEYWORDS_CHARACTERS_LIMIT;
	public static final int IMAGE_ALT_TEXT_CHARACTERS_LIMIT;
	public static final int CANONICAL_URL_CHARACTERS_LIMIT;
	public static final int MAXIMUM_LINKS_COUNTS;
	public static final int MAXIMUM_EXTERNAL_LINKS_COUNTS;
	public static final byte CONTENT_HTML_RATIO;
	public static final int MAXIMUM_RESPONSE_TIME;
	public static final int MAXIMUM_IMAGE_SIZE;
	public static final boolean MULTI_LINGUAL;
	public static String site;
	public static String user;
	public static String pass;
	public static final Properties PROPERTIES;
	public static final List<String> SKIPPED_URLS;
	public static final String crawlStorageFolder;
	public static final String dataLocation;
	public static final Pattern pattern;
	public static final Pattern shouldVisitPattern;
	public static final boolean caseSensitive;
	public static String reportPath;
	public static final Pattern IMAGE_PATTERN = Pattern.compile("([^\\s]+(\\.(?i)(jpg|jpeg|png|gif|bmp)))",
			Pattern.CASE_INSENSITIVE);
	public static final Pattern ASSETS_PATTERN = Pattern.compile("([^\\s]+(\\.(?i)(jpg|jpeg|png|gif|bmp|js|css)))",
			Pattern.CASE_INSENSITIVE);
	public static final String PAGE_STRUCTURE_URL, PAGE_SPEED_URL;
	public static final String USER_AGENT;
	public static final String PAGE_SPEED_KEY;
	static {

		String outputDirectory = new File(System.getProperty("user.dir") + File.separator + "Reports" + File.separator
				+ HelperUtils.generateUniqueString()).getAbsolutePath();
		if (null != System.getenv("JENKINS_URL") && !System.getenv("JENKINS_URL").isEmpty()) {
			outputDirectory = outputDirectory.substring(0, outputDirectory.indexOf("jenkins")) + File.separator
					+ "SEOBOX" + File.separator + "Reports";
		}
		reportPath = outputDirectory + File.separator + "SEOBOX_Report.html";
		File storage = new File(System.getProperty("user.dir") + File.separator + "temp");
		storage.mkdirs();
		crawlStorageFolder = storage.getAbsolutePath();
		PROPERTIES = new Properties();
		SKIPPED_URLS = new ArrayList<>();
		String PROPERTIES_LOC = "CrawlerConfigFile";
		try {
			if (!new File(PROPERTIES_LOC).exists()) {
				LoggerFactory.getLogger(SEOConfig.class).info("Loading default config file");
				PROPERTIES_LOC = HelperUtils.getResourceFile("Config.properties");
			} else {
				LoggerFactory.getLogger(SEOConfig.class).info("Loading user's config file");
				PROPERTIES_LOC = HelperUtils.getResourceFile("Config.properties", PROPERTIES_LOC);
			}
			FileInputStream in = new FileInputStream(new File(PROPERTIES_LOC));
			PROPERTIES.load(in);
			in.close();
			String[] sites = PROPERTIES.getProperty("crawler.skipDomains").split(",");
			for (String site : sites) {
				SKIPPED_URLS.add(new URL(site).getHost().replaceAll("www.", ""));
			}
		} catch (IOException e) {
			LoggerFactory.getLogger(SEOConfig.class).debug("Error in loading config file", e);
		}
		site = System.getProperty("SiteAddress");
		user = System.getProperty("Username");
		pass = System.getProperty("Password");

		String host = "";
		try {
			host = new URL(site).getHost().replaceAll("www.", "");
		} catch (MalformedURLException e) {
			LoggerFactory.getLogger(SEOConfig.class).debug("Error in loading config file", e);
		}
		dataLocation = crawlStorageFolder + File.separator + host;
		new File(dataLocation).mkdirs();
		pattern = Pattern.compile(PROPERTIES.getProperty("crawler.domainRegex", "."), Pattern.CASE_INSENSITIVE);
		shouldVisitPattern = Pattern.compile(PROPERTIES.getProperty("crawler.linksToVisit", "."),
				Pattern.CASE_INSENSITIVE);
		USER_AGENT = SEOConfig.PROPERTIES.getProperty("crawler.userAgentString",
				"Mozilla/5.0 (Windows NT 10.0; WOW64; rv:48.0) Gecko/20100101 Firefox/48.0");
		PAGE_SPEED_KEY = PROPERTIES.getProperty("page.speed.key", "AIzaSyAwlPiPJIkTejgqqH01v9DmtPoPeOPXDUQ");
		PAGE_STRUCTURE_URL = PROPERTIES.getProperty("page.structure.validation.url",
				"https://search.google.com/structured-data/testing-tool/validate");
		PAGE_SPEED_URL = PROPERTIES.getProperty("page.speed.url",
				"https://www.googleapis.com/pagespeedonline/v3beta1/runPagespeed");
		caseSensitive = Boolean.parseBoolean(PROPERTIES.getProperty("crawler.caseSensitiveUrl", "false"));
		URL_CHARACTERS_LIMIT = Integer.parseInt(PROPERTIES.getProperty("page.url.word.count", "115"));
		H1_CHARACTERS_LIMIT = Integer.parseInt(PROPERTIES.getProperty("page.h1.word.count", "70"));
		H2_CHARACTERS_LIMIT = Integer.parseInt(PROPERTIES.getProperty("page.h2.word.count", "70"));
		TITLE_CHARACTERS_LIMIT = Integer.parseInt(PROPERTIES.getProperty("page.title.word.count", "65"));
		TITLE_CHARACTERS_LIMIT_MIN = Integer.parseInt(PROPERTIES.getProperty("page.title.word.countMin", "65"));
		META_DESCRIPTION_CHARACTERS_LIMIT = Integer
				.parseInt(PROPERTIES.getProperty("page.meta.description.word.count", "156"));
		META_KEYWORDS_CHARACTERS_LIMIT = Integer
				.parseInt(PROPERTIES.getProperty("page.meta.keywords.word.count", "156"));
		IMAGE_ALT_TEXT_CHARACTERS_LIMIT = Integer
				.parseInt(PROPERTIES.getProperty("page.image.alt.text.word.count", "100"));
		CANONICAL_URL_CHARACTERS_LIMIT = Integer
				.parseInt(PROPERTIES.getProperty("page.canonical.url.word.count", "115"));
		MAXIMUM_LINKS_COUNTS = Integer.parseInt(PROPERTIES.getProperty("page.maximum.links.count", "1000"));
		MAXIMUM_EXTERNAL_LINKS_COUNTS = Integer
				.parseInt(PROPERTIES.getProperty("page.maximum.externalLink.count", "100"));
		MAXIMUM_RESPONSE_TIME = Integer.parseInt(PROPERTIES.getProperty("link.maximumLoadTime", "2000"));
		MAXIMUM_IMAGE_SIZE = Integer.parseInt(PROPERTIES.getProperty("link.image.maxSize", "2000"));
		CONTENT_HTML_RATIO = Byte.parseByte(PROPERTIES.getProperty("page.contentAndHTML.ratio", "80"));
		MULTI_LINGUAL = Boolean.parseBoolean(PROPERTIES.getProperty("site.multilingual", "false"));
	}
}
