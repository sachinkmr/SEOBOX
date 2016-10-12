package sachin.seobox.seo;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Set;

import org.apache.http.ParseException;
import org.jdom2.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import edu.uci.ics.crawler4j.url.WebURL;
import sachin.seobox.common.SEOConfig;
import sachin.seobox.helpers.HelperUtils;
import sachin.seobox.helpers.SiteMapUtils;
import sachin.seobox.helpers.StreamUtils;

public class LinkLevel {

	protected final Logger logger = LoggerFactory.getLogger(LinkLevel.class);
	private File[] pages;
	private StreamUtils streamUtils;

	@BeforeClass
	public void getPages() {
		pages = new File(SEOConfig.dataLocation).listFiles();
		streamUtils = new StreamUtils();
	}

	@Test(description = "Verify that internal link response time is less than required", groups = {
			"Links" }, enabled = true)
	public void verifyLinkResponseTime() {
		Method caller = new Object() {
		}.getClass().getEnclosingMethod();
		ExtentTest test = HelperUtils.getTestLogger(caller);
		for (File file : pages) {
			SEOPage page = streamUtils.readFile(file);
			try {
				WebURL webUrl = page.getPage().getWebURL();
				logger.debug("Verifying for: ", webUrl);
				if (page.getPage().getStatusCode() == 200 && webUrl.isInternalLink()) {
					int responseTime = page.getPage().getResponseTime();
					if (responseTime <= SEOConfig.MAXIMUM_RESPONSE_TIME) {
						test.log(LogStatus.PASS,
								"Response time is less than maximum response time.<br><b>URL: </b>" + webUrl.getURL()
										+ "<br/><b>Parent: </b>" + webUrl.getParentUrl(),
								"<b>Link Response Time: </b>" + responseTime + "<br/><b>Max Response Time: </b>"
										+ SEOConfig.MAXIMUM_RESPONSE_TIME);
					} else {
						test.log(LogStatus.FAIL,
								"Response time is greater than maximum response time.<br><b>URL: </b>" + webUrl.getURL()
										+ "<br/><b>Parent: </b>" + webUrl.getParentUrl(),
								"<b>Link Response Time: </b>" + responseTime + "<br/><b>Max Response Time: </b>"
										+ SEOConfig.MAXIMUM_RESPONSE_TIME);
					}
				}
			} catch (Exception e) {
				logger.debug("Error ", e);
			}
		}
	}

	@Test(description = "Verify that image size is less than required", groups = { "Links" }, enabled = true)
	public void verifyImageSize() {
		Method caller = new Object() {
		}.getClass().getEnclosingMethod();
		ExtentTest test = HelperUtils.getTestLogger(caller);
		for (File file : pages) {
			SEOPage page = streamUtils.readFile(file);
			try {
				WebURL webUrl = page.getPage().getWebURL();
				logger.debug("Verifying for: ", webUrl);
				if (page.getPage().getStatusCode() == 200 && SEOConfig.IMAGE_PATTERN.matcher(webUrl.getURL()).find()) {
					int size = page.getPage().getContentData().length;
					if (size <= SEOConfig.MAXIMUM_IMAGE_SIZE) {
						test.log(LogStatus.PASS,
								"Image Size is less than required size.<br><b>URL: </b>" + webUrl.getURL()
										+ "<br/><b>Parent: </b>" + webUrl.getParentUrl(),
								"<b>Image Size: </b>" + size + "<br/><b>Required Image Size: </b>"
										+ SEOConfig.MAXIMUM_IMAGE_SIZE);
					} else {
						test.log(LogStatus.FAIL,
								"Image Size is greater than required size.<br><b>URL: </b>" + webUrl.getURL()
										+ "<br/><b>Parent: </b>" + webUrl.getParentUrl(),
								"<b>Image Size: </b>" + size + "<br/><b>Required Image Size: </b>"
										+ SEOConfig.MAXIMUM_IMAGE_SIZE);
					}
				}
			} catch (Exception e) {
				logger.debug("Error ", e);
			}

		}
	}

	@Test(description = "Verify that compression is enabled. This verify for gzip in Content-Encoding header ", groups = {
			"Links" }, enabled = true)
	public void verifyCompression() {
		Method caller = new Object() {
		}.getClass().getEnclosingMethod();
		ExtentTest test = HelperUtils.getTestLogger(caller);
		for (File file : pages) {
			SEOPage page = streamUtils.readFile(file);
			try {
				WebURL webUrl = page.getPage().getWebURL();
				logger.debug("Verifying for: ", webUrl);
				if (page.getPage().getStatusCode() == 200 && webUrl.isInternalLink()
						&& page.getPage().getContentEncoding() != null
						&& !page.getPage().getContentEncoding().isEmpty()) {
					if (page.getPage().getContentEncoding().contains("gzip")) {
						test.log(LogStatus.PASS, "Content-Encoding header value is gzip.<br/><b>URL: <b/>"
								+ webUrl.getURL() + "<br/><b>Parent: </b>" + webUrl.getParentUrl());
					} else {
						test.log(LogStatus.FAIL, "Content-Encoding header value is not gzip.<br/><b>URL: <b/>"
								+ webUrl.getURL() + "<br/><b>Parent: </b>" + webUrl.getParentUrl());
					}
				} else {
					test.log(LogStatus.FAIL,
							"Content-Encoding header is not present in response.<br/><b>URL: <b/>" + webUrl.getURL());
				}
			} catch (Exception e) {
				logger.debug("Error ", e);
			}
		}

	}

	@Test(description = "get 2XX status code", groups = { "HTTP Status Codes" }, enabled = true)
	public void get2xxStatusCodeAllLinks() {
		Method caller = new Object() {
		}.getClass().getEnclosingMethod();
		ExtentTest test = HelperUtils.getTestLogger(caller);
		for (File file : pages) {
			SEOPage page = streamUtils.readFile(file);
			try {
				WebURL webUrl = page.getPage().getWebURL();
				logger.debug("Verifying for: ", webUrl);
				int statusCode = page.getPage().getStatusCode();
				if (statusCode >= 200 && statusCode < 300) {
					test.log(
							LogStatus.PASS, "Status code is 2xx.<br/><b>URL: <b/>" + webUrl.getURL()
									+ "<br/><b>Parent: </b>" + webUrl.getParentUrl(),
							"<b>Status Code: </b>" + statusCode);
				}
			} catch (Exception e) {
				logger.debug("Error ", e);
			}
		}
	}

	@Test(description = "get 2XX status code for internal links", groups = { "HTTP Status Codes" }, enabled = true)
	public void get2xxStatusCodeInternalLinks() {
		Method caller = new Object() {
		}.getClass().getEnclosingMethod();
		ExtentTest test = HelperUtils.getTestLogger(caller);
		for (File file : pages) {
			SEOPage page = streamUtils.readFile(file);
			try {
				WebURL webUrl = page.getPage().getWebURL();
				logger.debug("Verifying for: ", webUrl);
				int statusCode = page.getPage().getStatusCode();
				if (webUrl.isInternalLink() && statusCode >= 200 && statusCode < 300) {
					test.log(LogStatus.PASS, "Status code is 2xx.<br/><b>URL: <b/>" + webUrl.getURL(),
							"<b>Status Code: </b>" + statusCode);
				}
			} catch (Exception e) {
				logger.debug("Error ", e);
			}
		}
	}

	@Test(description = "get 3XX status code", groups = { "HTTP Status Codes" }, enabled = true)
	public void get3xxStatusCodeAllLinks() {
		Method caller = new Object() {
		}.getClass().getEnclosingMethod();
		ExtentTest test = HelperUtils.getTestLogger(caller);
		for (File file : pages) {
			SEOPage page = streamUtils.readFile(file);
			try {
				WebURL webUrl = page.getPage().getWebURL();
				logger.debug("Verifying for: ", webUrl);
				int statusCode = page.getPage().getStatusCode();
				if (statusCode >= 300 && statusCode < 400) {
					test.log(
							LogStatus.WARNING, "Status code is 3xx.<br/><b>URL: <b/>" + webUrl.getURL()
									+ "<br/><b>Parent: </b>" + webUrl.getParentUrl(),
							"<b>Status Code: </b>" + statusCode);
				}
			} catch (Exception e) {
				logger.debug("Error ", e);
			}
		}
	}

	@Test(description = "get 3XX status code for internal links", groups = { "HTTP Status Codes" }, enabled = true)
	public void get3xxStatusCodeInternalLinks() {
		Method caller = new Object() {
		}.getClass().getEnclosingMethod();
		ExtentTest test = HelperUtils.getTestLogger(caller);
		for (File file : pages) {
			SEOPage page = streamUtils.readFile(file);
			try {
				WebURL webUrl = page.getPage().getWebURL();
				logger.debug("Verifying for: ", webUrl);
				int statusCode = page.getPage().getStatusCode();
				if (webUrl.isInternalLink() && statusCode >= 300 && statusCode < 400) {
					test.log(
							LogStatus.WARNING, "Status code is 3xx.<br/><b>URL: <b/>" + webUrl.getURL()
									+ "<br/><b>Parent: </b>" + webUrl.getParentUrl(),
							"<b>Status Code: </b>" + statusCode);
				}
			} catch (Exception e) {
				logger.debug("Error ", e);
			}
		}
	}

	@Test(description = "get 4XX status code", groups = { "HTTP Status Codes" }, enabled = true)
	public void get4xxStatusCodeAllLinks() {
		Method caller = new Object() {
		}.getClass().getEnclosingMethod();

		ExtentTest test = HelperUtils.getTestLogger(caller);
		for (File file : pages) {
			SEOPage page = streamUtils.readFile(file);
			try {
				WebURL webUrl = page.getPage().getWebURL();
				logger.debug("Verifying for: ", webUrl);
				int statusCode = page.getPage().getStatusCode();
				if (statusCode >= 400 && statusCode < 500) {
					test.log(
							LogStatus.FAIL, "<b>URL:</b> " + page.getPage().getWebURL().getURL()
									+ "<br/><b>Parent: </b>" + page.getPage().getWebURL().getParentUrl(),
							"<b>Status Code: </b>" + statusCode);
				}
			} catch (Exception e) {
				logger.error("error", e);
			}
		}
	}

	@Test(description = "get 4XX status code for internal links", groups = { "HTTP Status Codes" }, enabled = true)
	public void get4xxStatusCodeInternalLinks() {
		Method caller = new Object() {
		}.getClass().getEnclosingMethod();

		ExtentTest test = HelperUtils.getTestLogger(caller);
		for (File file : pages) {
			SEOPage page = streamUtils.readFile(file);
			try {
				WebURL webUrl = page.getPage().getWebURL();
				logger.debug("Verifying for: ", webUrl);
				int statusCode = page.getPage().getStatusCode();
				if (webUrl.isInternalLink() && statusCode >= 400 && statusCode < 500) {
					test.log(
							LogStatus.FAIL, "<b>URL:</b> " + page.getPage().getWebURL().getURL()
									+ "<br/><b>Parent: </b>" + page.getPage().getWebURL().getParentUrl(),
							"<b>Status Code: </b>" + statusCode);
				}
			} catch (Exception e) {
				logger.error("error", e);
			}
		}
	}

	@Test(description = "get 5XX status code", groups = { "HTTP Status Codes" }, enabled = true)
	public void get5xxStatusCodeAllLinks() {
		Method caller = new Object() {
		}.getClass().getEnclosingMethod();
		ExtentTest test = HelperUtils.getTestLogger(caller);
		for (File file : pages) {
			SEOPage page = streamUtils.readFile(file);
			try {
				WebURL webUrl = page.getPage().getWebURL();
				logger.debug("Verifying for: ", webUrl);
				int statusCode = page.getPage().getStatusCode();
				if (statusCode >= 500) {
					test.log(
							LogStatus.FAIL, "<b>URL:</b> " + page.getPage().getWebURL().getURL()
									+ "<br/><b>Parent: </b>" + page.getPage().getWebURL().getParentUrl(),
							"<b>Status Code: </b>" + statusCode);
				}
			} catch (Exception e) {
				logger.debug("Error ", e);
			}
		}
	}

	@Test(description = "get 5XX status code for internal links", groups = { "HTTP Status Codes" }, enabled = true)
	public void get5xxStatusCodeInternalLinks() {
		Method caller = new Object() {
		}.getClass().getEnclosingMethod();
		ExtentTest test = HelperUtils.getTestLogger(caller);
		for (File file : pages) {
			SEOPage page = streamUtils.readFile(file);
			try {
				WebURL webUrl = page.getPage().getWebURL();
				logger.debug("Verifying for: ", webUrl);
				int statusCode = page.getPage().getStatusCode();
				if (webUrl.isInternalLink() && statusCode >= 500) {
					test.log(
							LogStatus.FAIL, "<b>URL:</b> " + page.getPage().getWebURL().getURL()
									+ "<br/><b>Parent: </b>" + page.getPage().getWebURL().getParentUrl(),
							"<b>Status Code: </b>" + statusCode);
				}
			} catch (Exception e) {
				logger.debug("Error ", e);
			}
		}
	}

	@Test(description = "Verify that Sitemap.xml file does not miss any link. This method depends on <b>'verifySitemapXML'</b> method.", groups = {
			"SiteMap.xml" })
	public void missingLinksInSitemapXML() {
		Method caller = new Object() {
		}.getClass().getEnclosingMethod();
		ExtentTest test = HelperUtils.getTestLogger(caller);
		try {
			Set<String> urlsInSiteMap = SiteMapUtils.getLocURLsWithAltUrlsFromSitemapXML(SEOConfig.site, SEOConfig.user,
					SEOConfig.pass);
			for (File file : pages) {
				SEOPage page = streamUtils.readFile(file);
				try {
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
			test.log(LogStatus.FAIL, "Test Step Failed");
		}
	}

}
