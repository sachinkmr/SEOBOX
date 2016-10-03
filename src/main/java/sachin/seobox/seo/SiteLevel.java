package sachin.seobox.seo;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.fluent.Response;
import org.apache.http.util.EntityUtils;
import org.jdom2.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;

import sachin.seobox.crawler.CrawlerConfig;
import sachin.seobox.helpers.HelperUtils;
import sachin.seobox.helpers.HttpRequestUtils;
import sachin.seobox.helpers.SiteMapUtils;
import sachin.seobox.helpers.StreamUtils;
import sachin.seobox.reporter.BaseReporting;

public class SiteLevel extends BaseReporting {
	private static final Logger logger = LoggerFactory.getLogger(SiteLevel.class);
	private StreamUtils stream;

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
			Response response = HelperUtils.getRobotFileResponse(CrawlerConfig.site, CrawlerConfig.user,
					CrawlerConfig.pass);
			HttpResponse res = response.returnResponse();
			int code = res.getStatusLine().getStatusCode();
			Header[] headers = res.getAllHeaders();
			boolean head = res.containsHeader("Content-Encoding");
			String str = EntityUtils.toString(res.getEntity()).toLowerCase();
			if (code == 200 && null != str && str.contains("user-agent")) {
				test.log(LogStatus.PASS, "Robots.txt file found.");
			} else {
				test.log(LogStatus.FAIL, "Robots.txt file not found.");
			}
			// Content-Encoding
			if (head) {
				test.log(LogStatus.PASS, "Content-Encoding header is present in response.");
				try {
					if (this.getFirstHeader(headers, "Content-Encoding").getValue().contains("gzip")) {
						test.log(LogStatus.PASS, "Content-Encoding header value is gzip.");
					}
				} catch (Exception ex) {
					test.log(LogStatus.FAIL, "Content-Encoding header value is not gzip.");
				}
			} else {
				test.log(LogStatus.FAIL, "Content-Encoding header is not present in response.");
			}
		} catch (ParseException | IOException e) {
			logger.debug("Error in fatching response. " + e);
			test.log(LogStatus.FAIL, "Unable to read robots.txt");
		} catch (Exception e) {
			logger.debug("Error " + e);
			test.log(LogStatus.FAIL, "Test case failed");
		}
	}

	private Header getFirstHeader(Header[] headers, String string) {
		for (Header header : headers) {
			if (header.getName().equals(string)) {
				return header;
			}
		}
		return null;
	}

	@Test(priority = 0, description = "Verify that site does have Sitemap.xml file", groups = {
			"SiteMap.xml" }, enabled = true)
	public void verifySitemapXML() {
		try {
			Response response = SiteMapUtils.getSiteMapXMLResponse(CrawlerConfig.site, CrawlerConfig.user,
					CrawlerConfig.pass);
			HttpResponse res = response.returnResponse();
			int code = res.getStatusLine().getStatusCode();
			String str = EntityUtils.toString(res.getEntity());
			if (code == 200 && null != str && str.contains("<urlset")) {
				test.log(LogStatus.PASS, "Sitemap.xml file found.");
			} else {
				test.log(LogStatus.FAIL, "Sitemap.xml file not found.");
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
			test.log(LogStatus.FAIL, "Unable to read data from sitemap.xml");
		} catch (Exception e) {
			logger.debug("Error " + e);
			test.log(LogStatus.FAIL, "Test case failed");
		}
	}

	@Test(description = "Verify that Sitemap.xml file does not have broken links. This method depends on <b>'verifySitemapXML'</b> method.", groups = {
			"SiteMap.xml" }, dependsOnMethods = { "verifySitemapXML" }, enabled = true)
	public void brokenLinksSitemapXML() {
		StreamUtils stream = new StreamUtils();
		Set<String> urls = null;
		try {
			try {
				urls = SiteMapUtils.getLocURLsWithAltUrlsFromSitemapXML(CrawlerConfig.site, CrawlerConfig.user,
						CrawlerConfig.pass);
			} catch (Exception e1) {
				logger.debug("Error " + e1);
				test.log(LogStatus.FAIL, "Error in fatching urls from sitemap.xml", e1.getMessage());
			}
			File urlsDirectory = new File(CrawlerConfig.dataLocation);
			SEOPage page = null;
			for (String url : urls) {
				int responseCode = 0;
				File file = new File(urlsDirectory, url.hashCode() + ".webUrl");
				if (file.exists()) {
					try {
						page = stream.readFile(file);
						responseCode = page.getPage().getStatusCode();
						if (responseCode == 200) {
							test.log(LogStatus.PASS, "<b>URL: </b> " + url, "StatusCode: " + responseCode);
						} else {
							test.log(LogStatus.FAIL, "<b>URL: </b> " + url, "StatusCode: " + responseCode);
						}
					} catch (ClassNotFoundException | IOException e) {
						logger.debug("Error in fetching response from file. " + e);
						test.log(LogStatus.FAIL, "Unable to fetch response from saved location");
					} catch (Exception e) {
						logger.debug("Error " + e);
						test.log(LogStatus.FAIL, "URL: " + page.getPage().getWebURL().getURL());
					}
				} else {
					try {
						responseCode = HttpRequestUtils.getUrlResponse(url, CrawlerConfig.user, CrawlerConfig.pass)
								.getStatusLine().getStatusCode();
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
		} catch (Exception e) {
			logger.debug("Error " + e);
			test.log(LogStatus.FAIL, "Test case failed");
		}
	}

	@Test(description = "Verify that Sitemap.xml file does not miss any link. This method depends on <b>'verifySitemapXML'</b> method.", groups = {
			"SiteMap.xml" })
	public void missingLinksInSitemapXML() {
		try {
			Set<String> urlsInSiteMap = SiteMapUtils.getLocURLsWithAltUrlsFromSitemapXML(CrawlerConfig.site,
					CrawlerConfig.user, CrawlerConfig.pass);
			File[] urlFiles = new File(CrawlerConfig.dataLocation).listFiles();
			SEOPage page = null;
			for (File file : urlFiles) {
				try {
					page = stream.readFile(file);
					logger.debug("Verifying for: ", page.getPage().getWebURL());
					if (page.getPage().getWebURL().isInternalLink() && page.getPage().getStatusCode() == 200
							&& page.getPage().getContentType().contains("text/html")) {
						String url = page.getPage().getWebURL().getURL();
						if (urlsInSiteMap.contains(url)) {
							test.log(LogStatus.PASS, "<b>URL: </b>" + url, "URL found in sitemap.xml");
						} else {
							test.log(LogStatus.FAIL, "<b>URL: </b> " + url, "URL not found in sitemap.xml");
						}
					}
				} catch (Exception e) {
					logger.debug("Error " + e);
					test.log(LogStatus.FAIL, "URL: " + page.getPage().getWebURL().getURL());
				}
			}
		} catch (ParseException | IOException | JDOMException e) {
			logger.error("error in reading URLs from sitemap xml", e);
			test.log(LogStatus.FAIL, "Unable to read data from sitemap.xml", e.getMessage());
		} catch (Exception e) {
			logger.debug("Error " + e);
			test.log(LogStatus.FAIL, "Test case failed");
		}
	}

	@Test(description = "Verify that site has custom error pages enabled", groups = {
			"Custom Error Pages" }, enabled = false)
	public void verifyCutomErrorPages() {
		test.log(LogStatus.INFO, "Not implemented yet");
	}

	@Test(description = "Verify that site does not have duplicate meta description values", groups = {
			"Meta Description" })
	public void verifyDuplicateDescription() {
		try {
			File[] urlFiles = new File(CrawlerConfig.dataLocation).listFiles();
			Map<String, String> map = new HashMap<>();
			boolean flag = true;
			SEOPage page = null;
			for (File file : urlFiles) {
				try {
					page = stream.readFile(file);
					logger.debug("Verifying for: ", page.getPage().getWebURL());
					if (page.getPage().getWebURL().isInternalLink() && page.getPage().getStatusCode() == 200
							&& page.getPage().getContentType().contains("text/html")) {
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
				} catch (Exception e) {
					logger.debug("Error " + e);
					test.log(LogStatus.FAIL, "URL: " + page.getPage().getWebURL().getURL());
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
		} catch (Exception e) {
			logger.debug("Error " + e);
			test.log(LogStatus.FAIL, "Test case failed");
		}
	}

	@Test(description = "Verify that site does not have duplicate title values", groups = { "Title Tag" })
	public void verifyDuplicateTitle() {
		try {
			File[] urlFiles = new File(CrawlerConfig.dataLocation).listFiles();
			Map<String, String> map = new HashMap<>();
			boolean flag = true;
			SEOPage page = null;
			for (File file : urlFiles) {
				try {
					page = stream.readFile(file);
					logger.debug("Verifying for: ", page.getPage().getWebURL());
					if (page.getPage().getWebURL().isInternalLink() && page.getPage().getStatusCode() == 200
							&& page.getPage().getContentType().contains("text/html")) {
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
				} catch (Exception e) {
					logger.debug("Error " + e);
					test.log(LogStatus.FAIL, "URL: " + page.getPage().getWebURL().getURL());
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
		} catch (Exception e) {
			logger.debug("Error " + e);
			test.log(LogStatus.FAIL, "Test case failed", e);
		}
	}
}
