package sachin.seobox.seo;

import java.io.File;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import edu.uci.ics.crawler4j.url.WebURL;
import sachin.seobox.crawler.CrawlerConstants;
import sachin.seobox.helpers.HelperUtils;
import sachin.seobox.helpers.StreamUtils;
import sachin.seobox.parameters.SEOPage;
import sachin.seobox.reporter.ComplexReportFactory;

public class LinkLevel {

	final Logger logger = LoggerFactory.getLogger(LinkLevel.class);
	private File[] pages;
	private StreamUtils streamUtils;

	@BeforeClass
	public void getPages() {
		pages = new File(CrawlerConstants.DATA_LOCATION).listFiles();
		streamUtils = new StreamUtils();
	}

	@AfterClass
	public void afterClass() {
		pages = null;
		streamUtils = null;
	}

	@Test(description = "Verify that internal link response time is less than required", groups = {
			"Links" }, testName = "Link Response Time", enabled = true)
	public void linkResponseTime() {
		Method caller = new Object() {
		}.getClass().getEnclosingMethod();
		ExtentTest test = HelperUtils.getTestLogger(caller);
		for (File file : pages) {
			SEOPage page = streamUtils.readFile(file);
			try {
				WebURL webUrl = page.getPage().getWebURL();

				if (page.getPage().getStatusCode() == 200 && webUrl.isInternalLink()) {
					int responseTime = page.getPage().getResponseTime();
					if (responseTime <= CrawlerConstants.MAXIMUM_RESPONSE_TIME) {
						test.log(LogStatus.PASS,
								"Response time is less than maximum response time.<br><b>URL: </b>" + webUrl.getURL()
										+ "<br/><b>Parent: </b>" + webUrl.getParentUrl(),
								"<b>Link Response Time: </b>" + responseTime + "<br/><b>Max Response Time: </b>"
										+ CrawlerConstants.MAXIMUM_RESPONSE_TIME);
					} else {
						test.log(LogStatus.FAIL,
								"Response time is greater than maximum response time.<br><b>URL: </b>" + webUrl.getURL()
										+ "<br/><b>Parent: </b>" + webUrl.getParentUrl(),
								"<b>Link Response Time: </b>" + responseTime + "<br/><b>Max Response Time: </b>"
										+ CrawlerConstants.MAXIMUM_RESPONSE_TIME);
					}
				}
			} catch (Exception e) {
				logger.debug("Error ", e);
			}
		}
		ComplexReportFactory.getInstance().closeTest(test);
	}

	@Test(description = "Verify that internal image size is less than required", testName = "Image Size", groups = {
			"Image" }, enabled = true)
	public void imageSize() {
		Method caller = new Object() {
		}.getClass().getEnclosingMethod();
		ExtentTest test = HelperUtils.getTestLogger(caller);
		for (File file : pages) {
			SEOPage page = streamUtils.readFile(file);
			try {
				WebURL webUrl = page.getPage().getWebURL();
				if (page.getPage().getStatusCode() == 200
						&& CrawlerConstants.IMAGE_PATTERN.matcher(webUrl.getURL()).find()) {
					int size = page.getPage().getContentData().length;
					if (size <= CrawlerConstants.MAXIMUM_IMAGE_SIZE) {
						test.log(LogStatus.PASS,
								"Image Size is less than required size.<br><b>URL: </b>" + webUrl.getURL()
										+ "<br/><b>Parent: </b>" + webUrl.getParentUrl(),
								"<b>Image Size: </b>" + size + "<br/><b>Required Image Size: </b>"
										+ CrawlerConstants.MAXIMUM_IMAGE_SIZE);
					} else {
						test.log(LogStatus.FAIL,
								"Image Size is greater than required size.<br><b>URL: </b>" + webUrl.getURL()
										+ "<br/><b>Parent: </b>" + webUrl.getParentUrl(),
								"<b>Image Size: </b>" + size + "<br/><b>Required Image Size: </b>"
										+ CrawlerConstants.MAXIMUM_IMAGE_SIZE);
					}
				}
			} catch (Exception e) {
				logger.debug("Error ", e);
			}
		}
		ComplexReportFactory.getInstance().closeTest(test);
	}

	@Test(testName = "Link Compression", description = "Verify that compression is enabled. This verify for gzip in Content-Encoding header ", groups = {
			"Links" }, enabled = false)
	public void linkCompression() {
		Method caller = new Object() {
		}.getClass().getEnclosingMethod();
		ExtentTest test = HelperUtils.getTestLogger(caller);
		for (File file : pages) {
			SEOPage page = streamUtils.readFile(file);
			try {
				WebURL webUrl = page.getPage().getWebURL();

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
		ComplexReportFactory.getInstance().closeTest(test);
	}

	@Test(testName = "Status Codes for External Links", description = "Status code of external links on site. To view, choose filter on right side.<br/>Passed: 2XX, Wanring: 3XX, Error: 4XX, Fatal: 5XX", groups = {
			"HTTP Status Codes" }, enabled = true)
	public void statusCodeExternalLinks() {
		Method caller = new Object() {
		}.getClass().getEnclosingMethod();
		ExtentTest test = HelperUtils.getTestLogger(caller);
		for (File file : pages) {
			SEOPage page = streamUtils.readFile(file);
			try {
				WebURL webUrl = page.getPage().getWebURL();
				if (!webUrl.isInternalLink()) {
					int statusCode = page.getPage().getStatusCode();
					if (statusCode >= 200 && statusCode < 300) {
						test.log(LogStatus.PASS, "Status code is 2xx.<br/>URL: " + webUrl.getURL() + "<br/>Parent: "
								+ webUrl.getParentUrl(), "Status Code: " + statusCode);
					} else if (statusCode >= 300 && statusCode < 400) {
						test.log(LogStatus.WARNING, "Status code is 3xx.<br/>URL: <b/>" + webUrl.getURL()
								+ "<br/>Parent: " + webUrl.getParentUrl(), "Status Code: " + statusCode);
					} else if (statusCode >= 400 && statusCode < 500) {
						test.log(LogStatus.FAIL, "URL: " + page.getPage().getWebURL().getURL() + "<br/>Parent: "
								+ page.getPage().getWebURL().getParentUrl(), "Status Code: " + statusCode);
					} else if (statusCode >= 500) {
						test.log(LogStatus.FATAL, "URL: " + page.getPage().getWebURL().getURL() + "<br/>Parent: "
								+ page.getPage().getWebURL().getParentUrl(), "Status Code: " + statusCode);
					}
				}
			} catch (Exception e) {
				logger.debug("Error ", e);
			}
		}
		ComplexReportFactory.getInstance().closeTest(test);
	}

	@Test(testName = "Status Codes for Internal Links", description = "status code for internal links. To view, choose filter on right side.<br/>Passed: 2XX, Wanring: 3XX, Error: 4XX, Fatal: 5XX", groups = {
			"HTTP Status Codes" }, enabled = true)
	public void statusCodeInternalLinks() {
		Method caller = new Object() {
		}.getClass().getEnclosingMethod();
		ExtentTest test = HelperUtils.getTestLogger(caller);
		for (File file : pages) {
			SEOPage page = streamUtils.readFile(file);
			try {
				WebURL webUrl = page.getPage().getWebURL();
				if (webUrl.isInternalLink()) {
					int statusCode = page.getPage().getStatusCode();
					if (statusCode >= 200 && statusCode < 300) {
						test.log(LogStatus.PASS, "Status code is 2xx.<br/>URL: <b/>" + webUrl.getURL() + "<br/>Parent: "
								+ page.getPage().getWebURL().getParentUrl(), "Status Code: " + statusCode);
					} else if (statusCode >= 300 && statusCode < 400) {
						test.log(LogStatus.WARNING,
								"Status code is 3xx.<br/>URL: <b/>" + webUrl.getURL() + "<br/>Redireted To: <b/>"
										+ page.getPage().getRedirectedToUrl() + "<br/>Parent: " + webUrl.getParentUrl(),
								"Status Code: " + statusCode);
					} else if (statusCode >= 400 && statusCode < 500) {
						test.log(LogStatus.FAIL, "URL: " + page.getPage().getWebURL().getURL() + "<br/>Parent: "
								+ page.getPage().getWebURL().getParentUrl(), "Status Code: " + statusCode);
					} else if (statusCode >= 500) {
						test.log(LogStatus.FATAL, "URL: " + page.getPage().getWebURL().getURL() + "<br/>Parent: "
								+ page.getPage().getWebURL().getParentUrl(), "Status Code: " + statusCode);
					}
				}
			} catch (Exception e) {
				logger.debug("Error ", e);
			}
		}
		ComplexReportFactory.getInstance().closeTest(test);
	}

	@Test(testName = "3XX Status Codes", description = "get 3XX status code", groups = {
			"HTTP Status Codes" }, enabled = false)
	public void get3xxStatusCodeAllLinks() {
		Method caller = new Object() {
		}.getClass().getEnclosingMethod();
		ExtentTest test = HelperUtils.getTestLogger(caller);
		for (File file : pages) {
			SEOPage page = streamUtils.readFile(file);
			try {
				WebURL webUrl = page.getPage().getWebURL();

				int statusCode = page.getPage().getStatusCode();
				if (statusCode >= 300 && statusCode < 400) {
					test.log(LogStatus.WARNING,
							"Status code is 3xx.<br/><b>URL: <b/>" + webUrl.getURL() + "<b>Redireted To: <b/>"
									+ page.getPage().getRedirectedToUrl() + "<br/><b>Parent: </b>"
									+ webUrl.getParentUrl(),
							"<b>Status Code: </b>" + statusCode);
				}
			} catch (Exception e) {
				logger.debug("Error ", e);
			}
		}
		ComplexReportFactory.getInstance().closeTest(test);
	}

	@Test(testName = "3XX Status Code for Internal Links", description = "get 3XX status code for internal links", groups = {
			"HTTP Status Codes" }, enabled = false)
	public void get3xxStatusCodeInternalLinks() {
		Method caller = new Object() {
		}.getClass().getEnclosingMethod();
		ExtentTest test = HelperUtils.getTestLogger(caller);
		for (File file : pages) {
			SEOPage page = streamUtils.readFile(file);
			try {
				WebURL webUrl = page.getPage().getWebURL();

				int statusCode = page.getPage().getStatusCode();
				if (webUrl.isInternalLink() && statusCode >= 300 && statusCode < 400) {
					test.log(LogStatus.WARNING,
							"Status code is 3xx.<br/><b>URL: <b/>" + webUrl.getURL() + "<b>Redireted To: <b/>"
									+ page.getPage().getRedirectedToUrl() + "<br/><b>Parent: </b>"
									+ "<br/><b>Parent: </b>" + webUrl.getParentUrl(),
							"<b>Status Code: </b>" + statusCode);
				}
			} catch (Exception e) {
				logger.debug("Error ", e);
			}
		}
		ComplexReportFactory.getInstance().closeTest(test);
	}

	@Test(testName = "4XX Status Codes", description = "get 4XX status code", groups = {
			"HTTP Status Codes" }, enabled = false)
	public void get4xxStatusCodeAllLinks() {
		Method caller = new Object() {
		}.getClass().getEnclosingMethod();

		ExtentTest test = HelperUtils.getTestLogger(caller);
		for (File file : pages) {
			SEOPage page = streamUtils.readFile(file);
			try {
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
		ComplexReportFactory.getInstance().closeTest(test);
	}

	@Test(testName = "4XX Status Code for Internal Links", description = "get 4XX status code for internal links", groups = {
			"HTTP Status Codes" }, enabled = false)
	public void get4xxStatusCodeInternalLinks() {
		Method caller = new Object() {
		}.getClass().getEnclosingMethod();

		ExtentTest test = HelperUtils.getTestLogger(caller);
		for (File file : pages) {
			SEOPage page = streamUtils.readFile(file);
			try {
				WebURL webUrl = page.getPage().getWebURL();

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
		ComplexReportFactory.getInstance().closeTest(test);
	}

	@Test(testName = "", description = "get 5XX status code", groups = { "HTTP Status Codes" }, enabled = false)
	public void get5xxStatusCodeAllLinks() {
		Method caller = new Object() {
		}.getClass().getEnclosingMethod();
		ExtentTest test = HelperUtils.getTestLogger(caller);
		for (File file : pages) {
			SEOPage page = streamUtils.readFile(file);
			try {
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
		ComplexReportFactory.getInstance().closeTest(test);
	}

	@Test(testName = "", description = "get 5XX status code for internal links", groups = {
			"HTTP Status Codes" }, enabled = false)
	public void get5xxStatusCodeInternalLinks() {
		Method caller = new Object() {
		}.getClass().getEnclosingMethod();
		ExtentTest test = HelperUtils.getTestLogger(caller);
		for (File file : pages) {
			SEOPage page = streamUtils.readFile(file);
			try {
				WebURL webUrl = page.getPage().getWebURL();

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
		ComplexReportFactory.getInstance().closeTest(test);
	}

}
