package sachin.seobox.seo;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.List;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import sachin.seobox.common.SEOConfig;
import sachin.seobox.helpers.HttpRequestUtils;

public class SEOPage implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -1220222940314333382L;
    private final Page page;
    private byte[] html;
    private JSONObject pageSpeedDesktop;
    private JSONObject pageSpeedMobile;
    private JSONObject pageStructure;

    public SEOPage(Page page) {
	this.page = page;
	String str = (page.getParseData() instanceof HtmlParseData) ? ((HtmlParseData) page.getParseData()).getHtml()
		: null;
	if (str != null) {
	    html = str.getBytes(Charset.forName("UTF-8"));
	}
	if (!page.getWebURL().isInternalLink()) {
	    html = null;
	}
	if (getHtml().contains("itemtype") && getHtml().contains("http://schema.org/")) {
	    pageStructure = HttpRequestUtils.getStructuredData(getHtml());
	}
	if (SEOConfig.user != null && !SEOConfig.user.isEmpty() && getPage().getWebURL().isInternalLink()
		&& getPage().getStatusCode() == 200 && getPage().getContentType().contains("text/html")) {
	    pageSpeedMobile = HttpRequestUtils.getPageSpeedData(getPage().getWebURL().getURL(), "mobile");
	    pageSpeedDesktop = HttpRequestUtils.getPageSpeedData(getPage().getWebURL().getURL(), "desktop");
	}
    }

    public JSONObject getPageStructure() {
	return this.pageStructure;
    }

    public JSONObject getPageSpeedDesktop() {
	return this.pageSpeedDesktop;
    }

    public JSONObject getPageSpeedMobile() {
	return this.pageSpeedMobile;
    }

    public String getHtml() {
	return new String(html, Charset.forName("UTF-8"));

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
