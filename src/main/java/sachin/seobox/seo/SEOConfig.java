package sachin.seobox.seo;

import java.util.regex.Pattern;

import sachin.seobox.crawler.CrawlerConfig;

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
	public static final Pattern IMAGE_PATTERN = Pattern.compile("([^\\s]+(\\.(?i)(jpg|png|gif|bmp))$)");

	static {
		URL_CHARACTERS_LIMIT = Integer.parseInt(CrawlerConfig.PROPERTIES.getProperty("page.url.word.count", "115"));
		H1_CHARACTERS_LIMIT = Integer.parseInt(CrawlerConfig.PROPERTIES.getProperty("page.h1.word.count", "70"));
		H2_CHARACTERS_LIMIT = Integer.parseInt(CrawlerConfig.PROPERTIES.getProperty("page.h2.word.count", "70"));
		TITLE_CHARACTERS_LIMIT = Integer.parseInt(CrawlerConfig.PROPERTIES.getProperty("page.title.word.count", "65"));
		TITLE_CHARACTERS_LIMIT_MIN = Integer
				.parseInt(CrawlerConfig.PROPERTIES.getProperty("page.title.word.countMin", "65"));
		META_DESCRIPTION_CHARACTERS_LIMIT = Integer
				.parseInt(CrawlerConfig.PROPERTIES.getProperty("page.meta.description.word.count", "156"));
		META_KEYWORDS_CHARACTERS_LIMIT = Integer
				.parseInt(CrawlerConfig.PROPERTIES.getProperty("page.meta.keywords.word.count", "156"));
		IMAGE_ALT_TEXT_CHARACTERS_LIMIT = Integer
				.parseInt(CrawlerConfig.PROPERTIES.getProperty("page.image.alt.text.word.count", "100"));
		CANONICAL_URL_CHARACTERS_LIMIT = Integer
				.parseInt(CrawlerConfig.PROPERTIES.getProperty("page.canonical.url.word.count", "115"));
		MAXIMUM_LINKS_COUNTS = Integer
				.parseInt(CrawlerConfig.PROPERTIES.getProperty("page.maximum.links.count", "1000"));
		MAXIMUM_EXTERNAL_LINKS_COUNTS = Integer
				.parseInt(CrawlerConfig.PROPERTIES.getProperty("page.maximum.externalLink.count", "100"));
		MAXIMUM_RESPONSE_TIME = Integer.parseInt(CrawlerConfig.PROPERTIES.getProperty("link.maximumLoadTime", "2000"));
		MAXIMUM_IMAGE_SIZE = Integer.parseInt(CrawlerConfig.PROPERTIES.getProperty("link.image.maxSize", "2000"));
		CONTENT_HTML_RATIO = Byte.parseByte(CrawlerConfig.PROPERTIES.getProperty("page.contentAndHTML.ratio", "80"));
		MULTI_LINGUAL = Boolean.parseBoolean(CrawlerConfig.PROPERTIES.getProperty("site.multilingual", "false"));
	}
}
