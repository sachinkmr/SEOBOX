package sachin.seobox.seo.test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.jdom2.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;

import edu.uci.ics.crawler4j.crawler.Page;
import sachin.seobox.crawler.CrawlerConfig;
import sachin.seobox.helpers.HelperUtils;
import sachin.seobox.helpers.SiteMapUtils;
import sachin.seobox.helpers.StreamUtils;
import sachin.seobox.reporter.BaseReporting;
import sachin.seobox.seo.SEOPage;

public class SiteLevel extends BaseReporting {
    private static final Logger logger = LoggerFactory.getLogger(SiteLevel.class);
    StreamUtils stream;

    @BeforeClass
    public void initStreams() {
	stream = new StreamUtils();
    }

    @AfterClass
    public void closeResources() {
	try {
	    stream.closeStreams();
	} catch (IOException e) {
	    logger.debug("Unable to close streams. " + e);
	}
    }

    @Test(description = "Verify that site does have robot.txt file and its encoding is gzip", groups = {
	    "Robots.txt" }, enabled = true)
    public void verifyRobotsTXT() {
	try {
	    HttpResponse response = HelperUtils
		    .getRobotFileResponse(CrawlerConfig.site, CrawlerConfig.user, CrawlerConfig.pass).returnResponse();
	    if (response.getStatusLine().getStatusCode() == 200) {
		test.log(LogStatus.PASS, "Robots.txt file found.");
	    } else {
		test.log(LogStatus.FAIL, "Robots.txt file not found.");
	    }
	    // Content-Encoding
	    if (response.containsHeader("Content-Encoding")) {
		test.log(LogStatus.PASS, "Content-Encoding header is present in response.");
		if (response.getFirstHeader("Content-Encoding").getValue().contains("gzip")) {
		    test.log(LogStatus.PASS, "Content-Encoding header value is gzip.");
		} else {
		    test.log(LogStatus.FAIL, "Content-Encoding header value is not gzip.");
		}
	    } else {
		test.log(LogStatus.FAIL, "Content-Encoding header is not present in response.");
	    }
	} catch (ParseException | IOException e) {
	    logger.debug("Error in fatching response. " + e);
	    test.log(LogStatus.FAIL, "Unable to read robots.txt");
	}
    }

    @Test(priority = 0, description = "Verify that site does have Sitemap.xml file", groups = {
	    "SiteMap.xml" }, enabled = true)
    public void verifySitemapXML() {
	try {
	    HttpResponse response = SiteMapUtils
		    .getSiteMapXMLResponse(CrawlerConfig.site, CrawlerConfig.user, CrawlerConfig.pass).returnResponse();
	    Assert.assertEquals(response.getStatusLine().getStatusCode(), 200,
		    "Sitemap.xml file not found. Status code, ");
	    test.log(LogStatus.PASS, "Sitemap.xml file found.");
	    // if (response.getStatusLine().getStatusCode() == 200) {
	    // test.log(LogStatus.PASS, "Sitemap.xml file found.");
	    // } else {
	    // test.log(LogStatus.FAIL, "Sitemap.xml file not found.");
	    // }
	    // // Content-Encoding
	    // if (response.containsHeader("Content-Encoding")) {
	    // test.log(LogStatus.PASS, "Content-Encoding header is present in
	    // response.");
	    // if
	    // (response.getFirstHeader("Content-Encoding").getValue().contains("gzip"))
	    // {
	    // test.log(LogStatus.PASS, "Content-Encoding header value is
	    // gzip.");
	    // } else {
	    // test.log(LogStatus.FAIL, "Content-Encoding header value is not
	    // gzip.");
	    // }
	    // } else {
	    // test.log(LogStatus.FAIL, "Content-Encoding header is not present
	    // in response.");
	    // }
	} catch (ParseException | IOException e) {
	    logger.debug("Error in fatching response. " + e);
	    test.log(LogStatus.FAIL, "Unable to read data from sitemap.xml");
	}
    }

    @Test(description = "Verify that Sitemap.xml file does not have broken links. This method depends on <b>'verifySitemapXML'</b> method.", groups = {
	    "SiteMap.xml" }, dependsOnMethods = { "verifySitemapXML" }, enabled = false)
    public void brokenLinksSitemapXML() {
	StreamUtils stream = new StreamUtils();
	Set<String> urls = null;
	;
	try {
	    urls = SiteMapUtils.getLocURLsWithAltUrlsFromSitemapXML(CrawlerConfig.site, CrawlerConfig.user,
		    CrawlerConfig.pass);
	} catch (ParseException | IOException | JDOMException e1) {
	    logger.debug("Error in fatching response. " + e1);
	    test.log(LogStatus.FAIL, "Error in fatching urls from sitemap.xml", e1.getMessage());
	}
	File urlsDirectory = new File(CrawlerConfig.dataLocation);

	for (String url : urls) {
	    int responseCode = 0;
	    File file = new File(urlsDirectory, url.hashCode() + ".webUrl");
	    if (file.exists()) {
		try {
		    responseCode = stream.readFile(file).getPage().getStatusCode();
		    if (responseCode == 200) {
			test.log(LogStatus.PASS, "<b>URL: </b> " + url, "StatusCode: " + responseCode);
		    } else {
			test.log(LogStatus.FAIL, "<b>URL: </b> " + url, "StatusCode: " + responseCode);
		    }
		} catch (ClassNotFoundException | IOException e) {
		    logger.debug("Error in fetching response from file. " + e);
		    test.log(LogStatus.FAIL, "Unable to fetch response from saved location");
		}
	    } else {
		try {
		    responseCode = HelperUtils.getUrlResponse(url, CrawlerConfig.user, CrawlerConfig.pass)
			    .returnResponse().getStatusLine().getStatusCode();
		    if (responseCode == 200) {
			test.log(LogStatus.PASS, "<b>URL: </b>" + url, "StatusCode: " + responseCode);
		    } else {
			test.log(LogStatus.FAIL, "<b>URL: </b> " + url, "StatusCode: " + responseCode);
		    }
		} catch (Exception e) {
		    test.log(LogStatus.FAIL, "<b>URL: </b> " + url, "Unable to fetch response");
		}
	    }
	}
    }

    @Test(description = "Verify that Sitemap.xml file does not miss any link. This method depends on <b>'verifySitemapXML'</b> method.", groups = {
	    "SiteMap.xml" }, dependsOnMethods = { "verifySitemapXML" })
    public void missingLinksInSitemapXML() {
	try {
	    Set<String> urlsInSiteMap = SiteMapUtils.getLocURLsWithAltUrlsFromSitemapXML(CrawlerConfig.site,
		    CrawlerConfig.user, CrawlerConfig.pass);
	    File[] urlFiles = new File(CrawlerConfig.dataLocation).listFiles();
	    for (File file : urlFiles) {
		Page page = stream.readFile(file).getPage();
		if (page.getStatusCode() == 200 && page.getContentType().contains("text/html")) {
		    String url = page.getWebURL().getURL();
		    if (urlsInSiteMap.contains(url)) {
			test.log(LogStatus.PASS, "<b>URL: </b>" + url, "URL found in sitemap.xml");
		    } else {
			test.log(LogStatus.FAIL, "<b>URL: </b> " + url, "URL not found in sitemap.xml");
		    }
		}
	    }
	} catch (ParseException | IOException | JDOMException | ClassNotFoundException e) {
	    logger.error("error in reading URLs from sitemap xml", e);
	    test.log(LogStatus.FAIL, "Unable to read data from sitemap.xml", e.getMessage());
	}
    }

    @Test(description = "Verify that site has custom error pages enabled", groups = { "Custom Error Pages" })
    public void verifyCutomErrorPages() {
	test.log(LogStatus.INFO, "Not implemented yet");
    }

    @Test(description = "Verify that site does not have duplicate meta description values", groups = {
	    "Meta Description" })
    public void verifyDuplicateDescription() {
	File[] urlFiles = new File(CrawlerConfig.dataLocation).listFiles();
	Map<String, String> map = new HashMap<>();
	boolean flag = true;
	for (File file : urlFiles) {
	    try {
		SEOPage page = stream.readFile(file);
		if (page.getPage().getStatusCode() == 200 && page.getPage().getContentType().contains("text/html")) {
		    String key = page.getMetaDescription().get(0).attr("content");
		    String value = page.getPage().getWebURL().getURL();
		    if (map.containsKey(key)) {
			map.put(key, map.get(key) + "<br/>" + value);
			flag = false;
		    } else {
			map.put(key, value);
		    }
		}
	    } catch (ClassNotFoundException | IOException e) {
		logger.error("error in reading file", e);
	    }
	}
	for (String key : map.keySet()) {
	    if (map.get(key).split("<br/>").length > 2) {
		test.log(LogStatus.FAIL, map.get(key), "<b>Description: </b>" + key);
	    }
	}
	if (flag) {
	    test.log(LogStatus.PASS, "No duplicate descriptions found,");
	}
    }

    @Test(description = "Verify that site does not have duplicate title values", groups = { "Title Tag" })
    public void verifyDuplicateTitle() {
	File[] urlFiles = new File(CrawlerConfig.dataLocation).listFiles();
	Map<String, String> map = new HashMap<>();
	boolean flag = true;
	for (File file : urlFiles) {
	    try {
		SEOPage page = stream.readFile(file);
		if (page.getPage().getStatusCode() == 200 && page.getPage().getContentType().contains("text/html")) {
		    String key = page.getTitle().get(0).text();
		    String value = page.getPage().getWebURL().getURL();
		    if (map.containsKey(key)) {
			map.put(key, map.get(key) + "<br/>" + value);
			flag = false;
		    } else {
			map.put(key, value);
		    }
		}
	    } catch (ClassNotFoundException | IOException e) {
		logger.error("error in reading file", e);
	    }
	}
	for (String key : map.keySet()) {
	    if (map.get(key).split("<br/>").length > 2) {
		test.log(LogStatus.FAIL, map.get(key), "<b>Title: </b>" + key);
	    }
	}

	if (flag) {
	    test.log(LogStatus.PASS, "No duplicate titles found,");
	}
    }
}
