package sachin.seobox.seo;

import java.io.Serializable;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import sachin.seobox.common.SEOConfig;

public class SEOPage implements Serializable {

	private static final long serialVersionUID = -12L;
	// private int htmlDocID;
	// private final int statusCode;
	// private List<Element> h1Tags;
	// private List<Element> h2Tags;
	// private List<Element> metaDescription;
	// private List<Element> title;
	// private List<Element> metaKeywords;
	// private List<Element> canonical;
	// private List<Element> robotsTags;
	// private List<Element> ogTags;
	// private List<Element> images;
	// private List<Element> linkDirectives;
	// private URI uri;
	// private String url;
	private final Page page;

	public SEOPage(Page page) {
		this.page = page;
		// url = page.getWebURL().getURL();
		// statusCode = page.getStatusCode();
		// h1Tags = new ArrayList<>();
		// h2Tags = new ArrayList<>();
		// metaDescription = new ArrayList<>();
		// title = new ArrayList<>();
		// metaKeywords = new ArrayList<>();
		// canonical = new ArrayList<>();
		// robotsTags = new ArrayList<>();
		// ogTags = new ArrayList<>();
		// images = new ArrayList<>();
		// linkDirectives = new ArrayList<>();
		if (!page.getWebURL().isInternalLink() && !SEOConfig.ASSETS_PATTERN.matcher(page.getWebURL().getURL()).find()) {
			page.setContentData(null);
			page.setParseData(null);
		}

	}

	public String getHtml() {
		return page.getParseData() instanceof HtmlParseData ? ((HtmlParseData) page.getParseData()).getHtml() : null;

	}

	public List<Element> getH1Tags() {
		Document document = Jsoup.parse(getHtml(), SEOConfig.site);
		return document.select("h1");
	}

	public List<Element> getH2Tags() {
		Document document = Jsoup.parse(getHtml(), SEOConfig.site);
		return document.select("h2");
	}

	public List<Element> getMetaDescription() {
		Document document = Jsoup.parse(getHtml(), SEOConfig.site);
		return document.select("meta[name=description]");
	}

	public List<Element> getTitle() {
		Document document = Jsoup.parse(getHtml(), SEOConfig.site);
		return document.select("title");
	}

	public List<Element> getMetaKeywords() {
		Document document = Jsoup.parse(getHtml(), SEOConfig.site);
		return document.select("meta[name=keywords]");
	}

	public List<Element> getCanonical() {
		Document document = Jsoup.parse(getHtml(), SEOConfig.site);
		return document.select("link[rel=canonical]");
	}

	public List<Element> getRobotsTags() {
		Document document = Jsoup.parse(getHtml(), SEOConfig.site);
		return document.select("meta[name=ROBOTS]");
	}

	public List<Element> getOgTags() {
		Document document = Jsoup.parse(getHtml(), SEOConfig.site);
		return document.select("meta[property^=og]");
	}

	public List<Element> getImages() {
		Document document = Jsoup.parse(getHtml(), SEOConfig.site);
		return document.select("img");
	}

	public Page getPage() {
		return this.page;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.getPage().getWebURL().getURL();
	}

}
