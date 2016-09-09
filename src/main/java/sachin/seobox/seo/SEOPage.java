package sachin.seobox.seo;

import java.io.Serializable;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import sachin.seobox.crawler.CrawlerConfig;

public class SEOPage implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -1220222940314333382L;
    private final Page page;

    public SEOPage(Page page) {
	this.page = page;
    }

    public String getHtml() {
	return (page.getParseData() instanceof HtmlParseData) ? ((HtmlParseData) page.getParseData()).getHtml() : null;

    }

    public List<Element> getH1Tags() {
	Document document = Jsoup.parse(getHtml(), CrawlerConfig.site);
	return document.select("h1");
    }

    public List<Element> getH2Tags() {
	Document document = Jsoup.parse(getHtml(), CrawlerConfig.site);
	return document.select("h2");
    }

    public List<Element> getMetaDescription() {
	Document document = Jsoup.parse(getHtml(), CrawlerConfig.site);
	return document.select("meta[name=description]");
    }

    public List<Element> getTitle() {
	Document document = Jsoup.parse(getHtml(), CrawlerConfig.site);
	return document.select("title");
    }

    public List<Element> getMetaKeywords() {
	Document document = Jsoup.parse(getHtml(), CrawlerConfig.site);
	return document.select("meta[name=keywords]");
    }

    public List<Element> getCanonical() {
	Document document = Jsoup.parse(getHtml(), CrawlerConfig.site);
	return document.select("link[rel=canonical]");
    }

    public List<Element> getRobotsTags() {
	Document document = Jsoup.parse(getHtml(), CrawlerConfig.site);
	return document.select("meta[name=ROBOTS]");
    }

    public List<Element> getOgTags() {
	Document document = Jsoup.parse(getHtml(), CrawlerConfig.site);
	return document.select("meta[property^=og]");
    }
    
    public List<Element> getImages() {
	Document document = Jsoup.parse(getHtml(), CrawlerConfig.site);
	return document.select("img");
    }


    public Page getPage() {
	return this.page;
    }

}
