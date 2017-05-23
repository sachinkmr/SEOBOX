package sachin.seobox.seo;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.fluent.Response;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.jdom2.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import sachin.seobox.crawler.CrawlerConstants;
import sachin.seobox.helpers.HelperUtils;
import sachin.seobox.helpers.NetUtils;
import sachin.seobox.helpers.SiteMapUtils;
import sachin.seobox.helpers.StreamUtils;
import sachin.seobox.parameters.SEOPage;
import sachin.seobox.reporter.ComplexReportFactory;

public class SiteLevel {
	static final Logger logger = LoggerFactory.getLogger(SiteLevel.class);
	private StreamUtils streamUtils;

	@BeforeClass
	public void getPages() {
		streamUtils = new StreamUtils();
	}

	@AfterClass
	public void afterClass() {
		streamUtils = null;
	}

	@Test(testName = "Verify Robot.txt", description = "Verify that site does have robot.txt file and its encoding is gzip", groups = { "Robots.txt" }, enabled = true)
	public void verifyRobotsTXT() {
		Method caller = new Object() {
		}.getClass().getEnclosingMethod();
		ExtentTest test = HelperUtils.getTestLogger(caller);
		try {
			String add = HelperUtils.getSiteAddress(CrawlerConstants.SITE) + "robots.txt";
			if (CrawlerConstants.PROPERTIES.getProperty("seo.robotFile") != null && !CrawlerConstants.PROPERTIES.getProperty("seo.robotFile").isEmpty()) {
				add = CrawlerConstants.PROPERTIES.getProperty("seo.robotFile");
			}
			// test.log(LogStatus.INFO, "Robot.txt Location: "+ add);
			Response response = HelperUtils.getLinkResponse(add, CrawlerConstants.USERNAME, CrawlerConstants.PASSWORD);
			HttpResponse res = response.returnResponse();
			int code = res.getStatusLine().getStatusCode();
			String str = EntityUtils.toString(res.getEntity()).toLowerCase();
			if (code == 200 && null != str && str.contains("user-agent")) {
				test.log(LogStatus.PASS, "Robots.txt file found.", "Robot.txt Location: " + add);
			} else {
				test.log(LogStatus.FAIL, "Robots.txt file not found.", "Robot.txt Location: " + add);
			}
			// Content-Encoding
			// Header[] headers = res.getAllHeaders();
			// boolean head = res.containsHeader("Content-Encoding");
			// if (head) {
			// test.log(LogStatus.PASS, "Content-Encoding header is present in
			// response.", "");
			// try {
			// if (this.getFirstHeader(headers,
			// "Content-Encoding").getValue().contains("gzip")) {
			// test.log(LogStatus.PASS, "Content-Encoding header value is
			// gzip.", "");
			// }
			// } catch (Exception ex) {
			// test.log(LogStatus.FAIL, "Content-Encoding header value is not
			// gzip.", "");
			// }
			// } else {
			// test.log(LogStatus.FAIL, "Content-Encoding header is not present
			// in response.", "");
			// }
		} catch (ParseException | IOException e) {
			logger.debug("Error in fatching response. " + e);
			test.log(LogStatus.FAIL, "Unable to read robots.txt", "");
		} catch (Exception e) {
			logger.debug("Error " + e);
			test.log(LogStatus.FAIL, "Test Step Failed", "");
		}
		ComplexReportFactory.getInstance().closeTest(test);
	}

	// private Header getFirstHeader(Header[] headers, String string) {
	// for (Header header : headers) {
	// if (header.getName().equals(string)) {
	// return header;
	// }
	// }
	// return null;
	// }

	@Test(priority = 0, testName = "Verify Sitemap.xml", description = "Verify that site does have Sitemap.xml file", groups = { "SiteMap.xml" }, enabled = true)
	public void verifySitemapXML() {
		Method caller = new Object() {
		}.getClass().getEnclosingMethod();
		ExtentTest test = HelperUtils.getTestLogger(caller);
		try {
			String add = HelperUtils.getSiteAddress(CrawlerConstants.SITE) + "sitemap.xml";
			if (CrawlerConstants.PROPERTIES.getProperty("seo.sitemapFile") != null && !CrawlerConstants.PROPERTIES.getProperty("seo.sitemapFile").isEmpty()) {
				add = CrawlerConstants.PROPERTIES.getProperty("seo.sitemapFile");
			}
			// test.log(LogStatus.INFO, "Sitemap Location", add);
			Response response = HelperUtils.getLinkResponse(add, CrawlerConstants.USERNAME, CrawlerConstants.PASSWORD);
			HttpResponse res = response.returnResponse();
			int code = res.getStatusLine().getStatusCode();
			String str = EntityUtils.toString(res.getEntity());
			if (code == 200 && null != str && str.contains("<urlset")) {
				test.log(LogStatus.PASS, "Sitemap.xml file found.", "Sitemap Location: " + add);
			} else {
				test.log(LogStatus.FAIL, "Sitemap.xml file not found.", "Sitemap Location: " + add);
			}
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
			test.log(LogStatus.FAIL, "Unable to read data from sitemap.xml", "");
		} catch (Exception e) {
			logger.debug("Error " + e);
			test.log(LogStatus.FAIL, "Test Step Failed", "");
		}
		ComplexReportFactory.getInstance().closeTest(test);
	}

	@Test(testName = "Broken Links in SiteMap.xml", description = "Verify that Sitemap.xml file does not have broken links. This method depends on <b>'verifySitemapXML'</b> method.", groups = { "SiteMap.xml" }, dependsOnMethods = { "verifySitemapXML" }, enabled = true)
	public void brokenLinksSitemapXML() {
		Method caller = new Object() {
		}.getClass().getEnclosingMethod();
		ExtentTest test = HelperUtils.getTestLogger(caller);
		Set<String> urls = new HashSet<>();
		try {
			String add = HelperUtils.getSiteAddress(CrawlerConstants.SITE) + "sitemap.xml";
			if (CrawlerConstants.PROPERTIES.getProperty("seo.sitemapFile") != null && !CrawlerConstants.PROPERTIES.getProperty("seo.sitemapFile").isEmpty()) {
				add = CrawlerConstants.PROPERTIES.getProperty("seo.sitemapFile");
			}
			test.log(LogStatus.INFO, "<b>SiteMap Location: </b> " + add, "");
			try {
				urls = SiteMapUtils.getAllURLsWithAltAndImage(CrawlerConstants.SITE, CrawlerConstants.USERNAME, CrawlerConstants.PASSWORD);
			} catch (Exception e1) {
				logger.debug("Error ", e1);
				test.log(LogStatus.FAIL, "Error in fatching urls from sitemap.xml", e1.getMessage());
			}
			for (String url : urls) {
				int responseCode = 0;
				File file = new File(CrawlerConstants.DATA_LOCATION, url.hashCode() + ".webUrl");
				if (file.exists()) {
					try {
						SEOPage page = streamUtils.readFile(file);
						responseCode = page.getPage().getStatusCode();
						if (responseCode == 200) {
							test.log(LogStatus.PASS, "<b>URL: </b> <a href='" + url + "' target='_blank' >" + url + "</a>", "StatusCode: " + responseCode);
						} else {
							test.log(LogStatus.FAIL, "<b>URL: </b> <a href='" + url + "' target='_blank' >" + url + "</a>", "StatusCode: " + responseCode);
						}
					} catch (Exception e) {
						logger.debug("Error " + e);
					}
				} else {
					try {
						CloseableHttpResponse res = NetUtils.getUrlResponse(url, CrawlerConstants.USERNAME, CrawlerConstants.PASSWORD);
						responseCode = res.getStatusLine().getStatusCode();
						EntityUtils.consume(res.getEntity());
						res.close();
						if (responseCode == 200) {
							test.log(LogStatus.PASS, "<b>URL: </b><a href='" + url + "' target='_blank' >" + url + "</a>", "StatusCode: " + responseCode);
						} else {
							test.log(LogStatus.FAIL, "<b>URL: </b> <a href='" + url + "' target='_blank' >" + url + "</a>", "StatusCode: " + responseCode);
						}
					} catch (Exception e) {
						test.log(LogStatus.FAIL, "<b>URL: </b> <a href='" + url + "' target='_blank' >" + url + "</a>", "Unable to fetch response");
					}
				}
			}
		} catch (Exception e) {
			logger.debug("Error " + e);
			test.log(LogStatus.FAIL, "Test Step Failed", "");
		}
		ComplexReportFactory.getInstance().closeTest(test);
	}

	@Test(testName = "Missing Links in Sitemap.xml", description = "Verify that Sitemap.xml file does not miss any link. This method depends on <b>'verifySitemapXML'</b> method.", groups = { "SiteMap.xml" }, dependsOnMethods = { "verifySitemapXML" })
	public void missingLinksInSitemapXML() {
		Method caller = new Object() {
		}.getClass().getEnclosingMethod();
		ExtentTest test = HelperUtils.getTestLogger(caller);
		try {
			String add = HelperUtils.getSiteAddress(CrawlerConstants.SITE) + "sitemap.xml";
			if (CrawlerConstants.PROPERTIES.getProperty("seo.sitemapFile") != null && !CrawlerConstants.PROPERTIES.getProperty("seo.sitemapFile").isEmpty()) {
				add = CrawlerConstants.PROPERTIES.getProperty("seo.sitemapFile");
			}
			test.log(LogStatus.INFO, "<b>SiteMap Location: </b> " + add, "");
			Set<String> urlsInSiteMap = SiteMapUtils.getLocURLsWithAltUrlsFromSitemapXML(CrawlerConstants.SITE, CrawlerConstants.USERNAME, CrawlerConstants.PASSWORD);
			for (File file : new File(CrawlerConstants.DATA_LOCATION).listFiles()) {
				SEOPage page = streamUtils.readFile(file);
				try {
					// logger.debug("Verifying for: ",
					// page.getPage().getWebURL());
					if (page.getPage().getWebURL().isInternalLink() && page.getPage().getStatusCode() == 200 && page.getPage().getContentType().contains("text/html")) {
						String url = page.getPage().getWebURL().getURL();
						if (urlsInSiteMap.contains(url)) {
							test.log(LogStatus.PASS, "<b>URL: </b><a href='" + url + "' target='_blank' >" + url + "</a>", "URL found in sitemap.xml");
						} else {
							test.log(LogStatus.FAIL, "<b>URL: </b> <a href='" + url + "' target='_blank' >" + url + "</a>", "URL not found in sitemap.xml");
						}
					}
				} catch (Exception e) {
					logger.debug("Error " + e);
					test.log(LogStatus.FAIL, "URL: " + page.getPage().getWebURL().getURL());
				}
			}
		} catch (ParseException | IOException | JDOMException e) {
			logger.debug("error in reading URLs from sitemap xml", e);
			test.log(LogStatus.FAIL, "Unable to read data from sitemap.xml", e.getMessage());
		} catch (Exception e) {
			logger.debug("Error " + e);
			test.log(LogStatus.FAIL, "Test Step Failed");
		}
		ComplexReportFactory.getInstance().closeTest(test);
	}

	@Test(testName = "Custom Error Pages", description = "Verify that site has custom error pages enabled", groups = { "Custom Error Pages" }, enabled = false)
	public void verifyCutomErrorPages() {
		Method caller = new Object() {
		}.getClass().getEnclosingMethod();
		ExtentTest test = HelperUtils.getTestLogger(caller);
		test.log(LogStatus.INFO, "Not implemented yet");
		ComplexReportFactory.getInstance().closeTest(test);
	}

}
