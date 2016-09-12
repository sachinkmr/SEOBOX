package sachin.seobox.seo.test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;

import edu.uci.ics.crawler4j.url.WebURL;
import sachin.seobox.crawler.CrawlerConfig;
import sachin.seobox.helpers.StreamUtils;
import sachin.seobox.reporter.BaseReporting;
import sachin.seobox.seo.SEOConfig;
import sachin.seobox.seo.SEOPage;

public class LinkLevel extends BaseReporting {

	private static final Logger logger = LoggerFactory.getLogger(LinkLevel.class);
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

	@Test(description = "Verify that internal link response time is less than required", groups = {
			"Links" }, enabled = true)
	public void verifyLinkResponseTime() {
		File[] urlFiles = new File(CrawlerConfig.dataLocation).listFiles();
		for (File file : urlFiles) {
			try {
				SEOPage page = stream.readFile(file);
				WebURL webUrl = page.getPage().getWebURL();
				if (page.getPage().getStatusCode() == 200 && webUrl.isInternalLink()) {
					int responseTime = page.getPage().getResponseTime();
					if (responseTime <= SEOConfig.MAXIMUM_RESPONSE_TIME) {
						test.log(LogStatus.PASS,
								"Response time is less than maximum response time.<br><b>URL: </b>" + webUrl.getURL(),
								"<b>Link Response Time: </b>" + responseTime + "<br/><b>Max Response Time: </b>"
										+ SEOConfig.MAXIMUM_RESPONSE_TIME);
					} else {
						test.log(LogStatus.FAIL,
								"Response time is greater than maximum response time.<br><b>URL: </b>"
										+ webUrl.getURL(),
								"<b>Link Response Time: </b>" + responseTime + "<br/><b>Max Response Time: </b>"
										+ SEOConfig.MAXIMUM_RESPONSE_TIME);
					}
				}
			} catch (ClassNotFoundException | IOException e) {
				logger.error("error in reading file", e);
			}

		}
	}

	@Test(description = "Verify that image size is less than required", groups = { "Links" }, enabled = true)
	public void verifyImageSize() {
		File[] urlFiles = new File(CrawlerConfig.dataLocation).listFiles();
		for (File file : urlFiles) {
			try {
				SEOPage page = stream.readFile(file);
				WebURL webUrl = page.getPage().getWebURL();
				if (page.getPage().getStatusCode() == 200
						&& SEOConfig.IMAGE_PATTERN.matcher(webUrl.getURL()).matches()) {
					int size = page.getPage().getContentData().length;
					if (size <= SEOConfig.MAXIMUM_IMAGE_SIZE) {
						test.log(LogStatus.PASS,
								"Image Size is less than required size.<br><b>URL: </b>" + webUrl.getURL(),
								"<b>Image Size: </b>" + size + "<br/><b>Required Image Size: </b>"
										+ SEOConfig.MAXIMUM_IMAGE_SIZE);
					} else {
						test.log(LogStatus.FAIL,
								"Image Size is greater than required size.<br><b>URL: </b>" + webUrl.getURL(),
								"<b>Image Size: </b>" + size + "<br/><b>Required Image Size: </b>"
										+ SEOConfig.MAXIMUM_IMAGE_SIZE);
					}
				}
			} catch (ClassNotFoundException | IOException e) {
				logger.error("error in reading file", e);
			}

		}
	}

	@Test(description = "Verify that compression is enabled. This verify for gzip in Content-Encoding header ", groups = {
			"Links" }, enabled = true)
	public void verifyCompression() {
		File[] urlFiles = new File(CrawlerConfig.dataLocation).listFiles();
		for (File file : urlFiles) {
			try {
				SEOPage page = stream.readFile(file);
				WebURL webUrl = page.getPage().getWebURL();
				if (page.getPage().getStatusCode() == 200 && webUrl.isInternalLink()) {
					Header[] headers = page.getPage().getFetchResponseHeaders();
					Map<String, String> map = new HashMap<>();
					for (Header header : headers) {
						map.put(header.getName(), header.getValue());
					}
					if (map.containsKey("Content-Encoding")) {
						if (map.get("Content-Encoding").contains("gzip")) {
							test.log(LogStatus.PASS,
									"Content-Encoding header value is gzip.<br/><b>URL: <b/>" + webUrl.getURL());
						} else {
							test.log(LogStatus.FAIL,
									"Content-Encoding header value is not gzip.<br/><b>URL: <b/>" + webUrl.getURL());
						}
					} else {
						test.log(LogStatus.FAIL, "Content-Encoding header is not present in response.<br/><b>URL: <b/>"
								+ webUrl.getURL());
					}
				}
			} catch (ClassNotFoundException | IOException e) {
				logger.error("error in reading file", e);
			}
		}
	}

	@Test(description = "get 2XX status code", groups = { "HTTP Status Codes" }, enabled = true)
	public void get2xxStatusCodeAllLinks() {
		File[] urlFiles = new File(CrawlerConfig.dataLocation).listFiles();
		for (File file : urlFiles) {
			try {
				SEOPage page = stream.readFile(file);
				WebURL webUrl = page.getPage().getWebURL();
				int statusCode = page.getPage().getStatusCode();
				if (statusCode >= 200 && statusCode < 300) {
					test.log(LogStatus.PASS, "Status code is 2xx.<br/><b>URL: <b/>" + webUrl.getURL(),
							"<b>Status Code: </b>" + statusCode);
				}
			} catch (ClassNotFoundException | IOException e) {
				logger.error("error in reading file", e);
			}
		}
	}

	@Test(description = "get 2XX status code for internal links", groups = { "HTTP Status Codes" }, enabled = true)
	public void get2xxStatusCodeInternalLinks() {
		File[] urlFiles = new File(CrawlerConfig.dataLocation).listFiles();
		for (File file : urlFiles) {
			try {
				SEOPage page = stream.readFile(file);
				WebURL webUrl = page.getPage().getWebURL();
				int statusCode = page.getPage().getStatusCode();
				if (webUrl.isInternalLink() && statusCode >= 200 && statusCode < 300) {
					test.log(LogStatus.PASS, "Status code is 2xx.<br/><b>URL: <b/>" + webUrl.getURL(),
							"<b>Status Code: </b>" + statusCode);
				}
			} catch (ClassNotFoundException | IOException e) {
				logger.error("error in reading file", e);
			}
		}
	}

	@Test(description = "get 3XX status code", groups = { "HTTP Status Codes" }, enabled = true)
	public void get3xxStatusCodeAllLinks() {
		File[] urlFiles = new File(CrawlerConfig.dataLocation).listFiles();
		for (File file : urlFiles) {
			try {
				SEOPage page = stream.readFile(file);
				WebURL webUrl = page.getPage().getWebURL();
				int statusCode = page.getPage().getStatusCode();
				if (statusCode >= 300 && statusCode < 400) {
					test.log(LogStatus.WARNING, "Status code is 3xx.<br/><b>URL: <b/>" + webUrl.getURL(),
							"<b>Status Code: </b>" + statusCode);
				}
			} catch (ClassNotFoundException | IOException e) {
				logger.error("error in reading file", e);
			}
		}
	}

	@Test(description = "get 3XX status code for internal links", groups = { "HTTP Status Codes" }, enabled = true)
	public void get3xxStatusCodeInternalLinks() {
		File[] urlFiles = new File(CrawlerConfig.dataLocation).listFiles();
		for (File file : urlFiles) {
			try {
				SEOPage page = stream.readFile(file);
				WebURL webUrl = page.getPage().getWebURL();
				int statusCode = page.getPage().getStatusCode();
				if (webUrl.isInternalLink() && statusCode >= 300 && statusCode < 400) {
					test.log(LogStatus.WARNING, "Status code is 3xx.<br/><b>URL: <b/>" + webUrl.getURL(),
							"<b>Status Code: </b>" + statusCode);
				}
			} catch (ClassNotFoundException | IOException e) {
				logger.error("error in reading file", e);
			}
		}
	}

	@Test(description = "get 4XX status code", groups = { "HTTP Status Codes" }, enabled = true)
	public void get4xxStatusCodeAllLinks() {
		File[] urlFiles = new File(CrawlerConfig.dataLocation).listFiles();
		for (File file : urlFiles) {
			try {
				SEOPage page = stream.readFile(file);
				WebURL webUrl = page.getPage().getWebURL();
				int statusCode = page.getPage().getStatusCode();
				if (statusCode >= 400 && statusCode < 500) {
					test.log(LogStatus.FAIL, "Status code is 4xx.<br/><b>URL: <b/>" + webUrl.getURL(),
							"<b>Status Code: </b>" + statusCode);
				}
			} catch (ClassNotFoundException | IOException e) {
				logger.error("error in reading file", e);
			}
		}
	}

	@Test(description = "get 4XX status code for internal links", groups = { "HTTP Status Codes" }, enabled = true)
	public void get4xxStatusCodeInternalLinks() {
		File[] urlFiles = new File(CrawlerConfig.dataLocation).listFiles();
		for (File file : urlFiles) {
			try {
				SEOPage page = stream.readFile(file);
				WebURL webUrl = page.getPage().getWebURL();
				int statusCode = page.getPage().getStatusCode();
				if (webUrl.isInternalLink() && statusCode >= 400 && statusCode < 500) {
					test.log(LogStatus.FAIL, "Status code is 4xx.<br/><b>URL: <b/>" + webUrl.getURL(),
							"<b>Status Code: </b>" + statusCode);
				}
			} catch (ClassNotFoundException | IOException e) {
				logger.error("error in reading file", e);
			}
		}
	}

	@Test(description = "get 5XX status code", groups = { "HTTP Status Codes" }, enabled = true)
	public void get5xxStatusCodeAllLinks() {
		File[] urlFiles = new File(CrawlerConfig.dataLocation).listFiles();
		for (File file : urlFiles) {
			try {
				SEOPage page = stream.readFile(file);
				WebURL webUrl = page.getPage().getWebURL();
				int statusCode = page.getPage().getStatusCode();
				if (statusCode >= 500) {
					test.log(LogStatus.FAIL, "Status code is 5xx.<br/><b>URL: <b/>" + webUrl.getURL(),
							"<b>Status Code: </b>" + statusCode);
				}
			} catch (ClassNotFoundException | IOException e) {
				logger.error("error in reading file", e);
			}
		}
	}

	@Test(description = "get 5XX status code for internal links", groups = { "HTTP Status Codes" }, enabled = true)
	public void get5xxStatusCodeInternalLinks() {
		File[] urlFiles = new File(CrawlerConfig.dataLocation).listFiles();
		for (File file : urlFiles) {
			try {
				SEOPage page = stream.readFile(file);
				WebURL webUrl = page.getPage().getWebURL();
				int statusCode = page.getPage().getStatusCode();
				if (webUrl.isInternalLink() && statusCode >= 500) {
					test.log(LogStatus.FAIL, "Status code is 5xx.<br/><b>URL: <b/>" + webUrl.getURL(),
							"<b>Status Code: </b>" + statusCode);
				}
			} catch (ClassNotFoundException | IOException e) {
				logger.error("error in reading file", e);
			}
		}
	}
}
