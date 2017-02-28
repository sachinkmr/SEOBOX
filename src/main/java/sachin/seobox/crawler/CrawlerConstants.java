package sachin.seobox.crawler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.slf4j.LoggerFactory;

import sachin.seobox.helpers.HelperUtils;

public class CrawlerConstants {

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
	public static String SITE;
	public static final String USERNAME;
	public static final String PASSWORD;
	public static final Properties PROPERTIES;
	public static final List<String> SKIPPED_URLS;
	public static final String CRAWL_STORAGE_FOLDER;
	public static final String DATA_LOCATION;
	public static final Pattern PATTERN;
	public static final Pattern SHOULD_VISIT_PATTERN;
	public static final boolean CASE_SENSITIVE, HAS_JENKINS, HAS_WEBAPP;
	public static final String REPORT_PATH;
	public static final Pattern IMAGE_PATTERN;
	public static final Pattern ASSETS_PATTERN;
	public static final String PAGE_STRUCTURE_URL, PAGE_SPEED_URL;
	public static final String USER_AGENT;
	public static final String PAGE_SPEED_KEY;
	public static long CRAWLING_TIME;
	public static String ERROR_TEXT;
	public static boolean ERROR;
	public static final String REPORT_TIME_STAMP;
	public static final int PAGE_SPEED_PASS_POINTS;
	public static final String DB_HOST, DB_USERNAME, DB_PASSWORD, DB_NAME = "SEOBOX";
	public static final int DB_PORT, SERVICE_PORT;
	public static final Set<String> TESTS;
	public static final String SERVICE_HOST, SERVICE_NAME;
	static {
		SITE = System.getProperty("SiteAddress");
		USERNAME = System.getProperty("Username");
		PASSWORD = System.getProperty("Password");

		String host = "";
		try {
			host = new URL(SITE).getHost().replaceAll("www.", "");
		} catch (MalformedURLException e) {
			LoggerFactory.getLogger(CrawlerConstants.class).debug("Error in loading config file", e);
		}
		REPORT_TIME_STAMP = HelperUtils.generateUniqueString();
		// REPORT_TIME_STAMP = "25-February-2017_04-17-07-762PM";
		String OUTPUT_DIRECTORY = new File(System.getProperty("user.dir") + File.separator + "Reports" + File.separator
				+ host + File.separator + REPORT_TIME_STAMP).getAbsolutePath();
		if ((null != System.getProperty("JENKINS_URL") && !System.getProperty("JENKINS_URL").isEmpty())
				|| (null != System.getProperty("WEB") && !System.getProperty("WEB").isEmpty())) {
			OUTPUT_DIRECTORY = OUTPUT_DIRECTORY.substring(0, OUTPUT_DIRECTORY.indexOf("jenkins")) + File.separator
					+ "SEOBOX" + File.separator + "Reports" + File.separator + host + File.separator
					+ REPORT_TIME_STAMP;
		}
		REPORT_PATH = OUTPUT_DIRECTORY + File.separator + "Report.html";
		File storage = new File(System.getProperty("user.dir") + File.separator + "temp" + File.separator + host);
		storage.mkdirs();
		CRAWL_STORAGE_FOLDER = storage.getAbsolutePath();
		DATA_LOCATION = CRAWL_STORAGE_FOLDER + File.separator + "urls";
		new File(DATA_LOCATION).mkdirs();
		PROPERTIES = new Properties();
		SKIPPED_URLS = new ArrayList<>();
		String PROPERTIES_LOC = "";
		boolean deletePropFile = false;
		if (System.getProperty("CrawlerConfigFileLocation") != null
				&& !System.getProperty("CrawlerConfigFileLocation").isEmpty()) {
			LoggerFactory.getLogger(CrawlerConstants.class)
					.info("Loading user's config file provided from web interface");
			PROPERTIES_LOC = System.getProperty("CrawlerConfigFileLocation");
			deletePropFile = true;
		} else if (System.getProperty("CrawlerConfig") != null && !System.getProperty("CrawlerConfig").isEmpty()) {
			LoggerFactory.getLogger(CrawlerConstants.class)
					.info("Loading user's config file provided from jenkins interface");
			PROPERTIES_LOC = HelperUtils.getResourceFile("Config.properties", "CrawlerConfig");
		} else {
			LoggerFactory.getLogger(CrawlerConstants.class).info("Loading default config file");
			PROPERTIES_LOC = HelperUtils.getResourceFile("Config.properties");
		}

		// Loading crawler Config file
		LoggerFactory.getLogger(CrawlerConstants.class).info(PROPERTIES_LOC);
		try {
			Scanner in = new Scanner(new FileInputStream(new File(PROPERTIES_LOC)));
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			while (in.hasNext()) {
				out.write(in.nextLine().replace("\\", "\\\\").getBytes());
				out.write("\n".getBytes());
			}
			InputStream is = new ByteArrayInputStream(out.toByteArray());
			PROPERTIES.load(is);
			in.close();
			String[] sites = PROPERTIES.getProperty("crawler.skipDomains").trim().split(",");
			for (String site : sites) {
				SKIPPED_URLS.add(new URL(site).getHost().replaceAll("www.", ""));
			}
		} catch (IOException e) {
			LoggerFactory.getLogger(CrawlerConstants.class).error("Error in loading config file", e);
		}

		ASSETS_PATTERN = Pattern.compile("([^\\s]+(\\.(?i)(jpg|jpeg|png|gif|bmp|js|css)))", Pattern.CASE_INSENSITIVE);
		IMAGE_PATTERN = Pattern.compile("([^\\s]+(\\.(?i)(jpg|jpeg|png|gif|bmp)))", Pattern.CASE_INSENSITIVE);
		PATTERN = Pattern.compile(PROPERTIES.getProperty("crawler.domainRegex", ".").trim(), Pattern.CASE_INSENSITIVE);
		SHOULD_VISIT_PATTERN = Pattern.compile(PROPERTIES.getProperty("crawler.linksToVisit", ".").trim(),
				Pattern.CASE_INSENSITIVE);
		USER_AGENT = CrawlerConstants.PROPERTIES.getProperty("crawler.userAgentString",
				"Mozilla/5.0 (Windows NT 10.0; WOW64; rv:48.0) Gecko/20100101 Firefox/48.0");
		PAGE_SPEED_KEY = PROPERTIES.getProperty("page.speed.key", "AIzaSyAwlPiPJIkTejgqqH01v9DmtPoPeOPXDUQ");
		PAGE_STRUCTURE_URL = PROPERTIES.getProperty("page.structure.validation.url",
				"https://search.google.com/structured-data/testing-tool/validate");
		PAGE_SPEED_URL = PROPERTIES.getProperty("page.speed.url",
				"https://www.googleapis.com/pagespeedonline/v3beta1/runPagespeed");
		PAGE_SPEED_PASS_POINTS = Integer.parseInt(PROPERTIES.getProperty("page.speed.pass.points", "80"));

		CASE_SENSITIVE = Boolean.parseBoolean(PROPERTIES.getProperty("crawler.caseSensitiveUrl", "false"));
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
		DB_HOST = PROPERTIES.getProperty("mongo.db.host", "localhost");
		DB_PORT = Integer.parseInt(PROPERTIES.getProperty("mongo.db.port", "27017"));
		DB_USERNAME = PROPERTIES.getProperty("mongo.db.username", "");
		DB_PASSWORD = PROPERTIES.getProperty("mongo.db.password", "");
		SERVICE_NAME = PROPERTIES.getProperty("web.app.name", "SEOBOX");
		SERVICE_HOST = PROPERTIES.getProperty("web.app.host", "10.207.16.9");
		SERVICE_PORT = Integer.parseInt(PROPERTIES.getProperty("web.app.port", "80"));
		HAS_JENKINS = Boolean.parseBoolean(PROPERTIES.getProperty("seobox.jenkins", "false"));
		HAS_WEBAPP = Boolean.parseBoolean(PROPERTIES.getProperty("seobox.web.app", "false"));
		// Test Cases
		TESTS = HelperUtils.getTestCasesNames();
		if (System.getProperty("TestCases") != null && !System.getProperty("TestCases").isEmpty()) {
			LoggerFactory.getLogger(CrawlerConstants.class).info("Loading user's test cases....");
			String testsFile;
			testsFile = System.getProperty("TestCases");
			try {
				Set<String> set = new HashSet<>(FileUtils.readLines(new File(testsFile), "utf-8"));
				TESTS.removeAll(set);
			} catch (IOException e) {
				LoggerFactory.getLogger(CrawlerConstants.class).error("Error in loading test cases file", e);
			}
		}
		if (deletePropFile) {
			FileUtils.deleteQuietly(new File(PROPERTIES_LOC).getParentFile());
		}
	}

}
