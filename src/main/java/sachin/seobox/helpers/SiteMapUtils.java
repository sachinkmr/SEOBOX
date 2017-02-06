package sachin.seobox.helpers;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.util.EntityUtils;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;

import sachin.seobox.crawler.CrawlerConstants;

public class SiteMapUtils {

    public static Response getSiteMapXMLResponse(String... data)
	    throws ParseException, ClientProtocolException, IOException {
	String add = HelperUtils.getSiteAddress(data[0]) + "sitemap.xml";
	if (CrawlerConstants.PROPERTIES.getProperty("seo.sitemapFile") != null
		&& !CrawlerConstants.PROPERTIES.getProperty("seo.sitemapFile").isEmpty()) {
	    add = CrawlerConstants.PROPERTIES.getProperty("seo.sitemapFile");
	}
	Request request = Request.Get(add)
		.addHeader("user-agent",
			CrawlerConstants.PROPERTIES.getProperty("crawler.userAgentString",
				"Mozilla/5.0 (Windows NT 10.0; WOW64; rv:48.0) Gecko/20100101 Firefox/48.0"))
		.connectTimeout(
			Integer.parseInt(CrawlerConstants.PROPERTIES.getProperty("crawler.connectionTimeout", "120000")))
		.socketTimeout(
			Integer.parseInt(CrawlerConstants.PROPERTIES.getProperty("crawler.connectionTimeout", "120000")));
	if (data.length > 1 && null != data[1] && !data[1].trim().isEmpty()) {
	    String login = data[1] + ":" + data[2];
	    String base64login = new String(Base64.encodeBase64(login.getBytes()));
	    request.addHeader("Authorization", "Basic " + base64login);
	}
	return request.execute();
    }

    /**
     * Method to read and fetch All URLs from sitemap XML based on regex
     * 
     * @return set containing links from sitemap
     */
    public static Set<String> getURLFromSitemapXML(String... data)
	    throws ParseException, ClientProtocolException, IOException, JDOMException {
	String xml = EntityUtils.toString(getSiteMapXMLResponse(data).returnResponse().getEntity());
	String str1 = xml.substring(xml.indexOf(">") + 1, xml.length());
	String str2 = str1.substring(str1.indexOf(">") + 1, str1.length());
	return extractUrls(str2);
    }

    /**
     * Returns a list with all links contained in the input
     */
    private static Set<String> extractUrls(String text) {
	Set<String> containedUrls = new HashSet<>();
	String urlRegex = "((https?|ftp|gopher|telnet|file|http):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
	Pattern pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);
	Matcher urlMatcher = pattern.matcher(text);
	while (urlMatcher.find()) {
	    String url = text.substring(urlMatcher.start(0), urlMatcher.end(0));
	    containedUrls.add(url.contains("<") ? url.substring(0, url.indexOf("<")) : url);
	}
	return containedUrls;
    }

    /**
     * Method to read and fetch All URLs within loc tag from sitemap XML suing
     * SAX Parsing
     * 
     */
    public static Set<String> getLocURLsFromSitemapXML(String... data)
	    throws ParseException, ClientProtocolException, IOException, JDOMException {
	String xml = EntityUtils.toString(getSiteMapXMLResponse(data).returnResponse().getEntity());
	StringReader sr = new StringReader(xml);
	SAXBuilder saxReader = new SAXBuilder();
	Document doc = saxReader.build(sr);
	Element root = doc.getRootElement();
	Set<String> list = new HashSet<>();
	for (Element url : root.getChildren("url", root.getNamespace())) {
	    list.add(url.getChildText("loc", root.getNamespace()));
	}
	return list;
    }

    /**
     * Method to read and fetch All URLs within loc tag and their alternative
     * urls from sitemap XML suing SAX Parsing
     * 
     */
    public static Set<String> getLocURLsWithAltUrlsFromSitemapXML(String... data)
	    throws ParseException, ClientProtocolException, IOException, JDOMException {
	String xml = EntityUtils.toString(getSiteMapXMLResponse(data).returnResponse().getEntity());
	StringReader sr = new StringReader(xml);
	SAXBuilder saxReader = new SAXBuilder();
	Document doc = saxReader.build(sr);
	Element root = doc.getRootElement();
	Set<String> list = new HashSet<>();
	Namespace ns = Namespace.getNamespace("xhtml", "http://www.w3.org/1999/xhtml");
	for (Element url : root.getChildren("url", root.getNamespace())) {
	    list.add(url.getChildText("loc", url.getNamespace()));
	    if (!url.getChildren("link", ns).isEmpty()) {
		for (Element link : url.getChildren("link", ns)) {
		    list.add(link.getAttributeValue("href"));
		}
	    }
	}
	return list;
    }
}
