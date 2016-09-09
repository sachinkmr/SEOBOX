package sachin.seobox.helpers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
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

import edu.uci.ics.crawler4j.url.URLCanonicalizer;
import sachin.seobox.crawler.CrawlerConfig;

public class SiteMapUtils {
    public static Response getSiteMapXMLResponse(String... data)
	    throws ParseException, ClientProtocolException, IOException {
	String add = URLCanonicalizer.getCanonicalURL(data[0] + "/sitemap.xml");
	Response response = null;
	if (data.length == 1 || null == data[1] || data[1].trim().isEmpty()) {
	    response = Request.Get(add)
		    .connectTimeout(Integer
			    .parseInt(CrawlerConfig.PROPERTIES.getProperty("crawler.connectionTimeout", "20000")))
		    .socketTimeout(Integer
			    .parseInt(CrawlerConfig.PROPERTIES.getProperty("crawler.connectionTimeout", "20000")))
		    .execute();

	} else {
	    String login = data[1] + ":" + data[2];
	    String base64login = new String(Base64.encodeBase64(login.getBytes()));
	    response = Request.Get(add).addHeader("Authorization", "Basic " + base64login)
		    .connectTimeout(Integer
			    .parseInt(CrawlerConfig.PROPERTIES.getProperty("crawler.connectionTimeout", "20000")))
		    .socketTimeout(Integer
			    .parseInt(CrawlerConfig.PROPERTIES.getProperty("crawler.connectionTimeout", "20000")))
		    .execute();
	}
	return response;
    }

    /**
     * Method to read and fetch All URLs from sitemap XML based on regex
     * 
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
	InputStream stream = new ByteArrayInputStream(xml.getBytes("UTF-8"));
	SAXBuilder builder = new SAXBuilder();
	Document doc = builder.build(stream);
	Element root = doc.getRootElement();
	Set<String> list = new HashSet<>();
	for (Element url : root.getChildren("url",root.getNamespace())) {
	    list.add(url.getChildText("loc",root.getNamespace()));
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
	InputStream stream = new ByteArrayInputStream(xml.getBytes("UTF-8"));
	SAXBuilder builder = new SAXBuilder();
	Document doc = builder.build(stream);
	Element root = doc.getRootElement();
	Set<String> list = new HashSet<>();
	Namespace ns = Namespace.getNamespace("xhtml", "http://www.w3.org/1999/xhtml");
	for (Element url : root.getChildren("url",root.getNamespace())) {
	    list.add(url.getChildText("loc",url.getNamespace()));
	    if (!url.getChildren("link", ns).isEmpty()) {
		for (Element link : url.getChildren("link", ns)) {
		    list.add(link.getAttributeValue("href"));
		}
	    }
	}
	return list;
    }
}
