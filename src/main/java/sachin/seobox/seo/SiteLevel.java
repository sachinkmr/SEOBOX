package sachin.seobox.seo;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.fluent.Response;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import sachin.seobox.common.SEOConfig;
import sachin.seobox.helpers.HelperUtils;
import sachin.seobox.helpers.HttpRequestUtils;
import sachin.seobox.helpers.SiteMapUtils;
import sachin.seobox.helpers.StreamUtils;
import sachin.seobox.reporter.ComplexReportFactory;

public class SiteLevel {
	protected static final Logger logger = LoggerFactory.getLogger(SiteLevel.class);
	private StreamUtils streamUtils;

	@BeforeClass
	public void getPages() {
		streamUtils = new StreamUtils();
	}

	@AfterClass
	public void afterClass() {
		streamUtils = null;
	}

	@Test(description = "Verify that site does have robot.txt file and its encoding is gzip", groups = {
			"Robots.txt" }, enabled = true)
	public void verifyRobotsTXT() {
		Method caller = new Object() {
		}.getClass().getEnclosingMethod();
		ExtentTest test = HelperUtils.getTestLogger(caller);
		try {
			Response response = HelperUtils.getRobotFileResponse(SEOConfig.site, SEOConfig.user, SEOConfig.pass);
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
			test.log(LogStatus.FAIL, "Test Step Failed");
		}
		ComplexReportFactory.getInstance().closeTest(test);
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
		Method caller = new Object() {
		}.getClass().getEnclosingMethod();
		ExtentTest test = HelperUtils.getTestLogger(caller);
		try {
			Response response = SiteMapUtils.getSiteMapXMLResponse(SEOConfig.site, SEOConfig.user, SEOConfig.pass);
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
			test.log(LogStatus.FAIL, "Test Step Failed");
		}
		ComplexReportFactory.getInstance().closeTest(test);
	}

	@Test(description = "Verify that Sitemap.xml file does not have broken links. This method depends on <b>'verifySitemapXML'</b> method.", groups = {
			"SiteMap.xml" }, dependsOnMethods = { "verifySitemapXML" }, enabled = true)
	public void brokenLinksSitemapXML() {
		Method caller = new Object() {
		}.getClass().getEnclosingMethod();
		ExtentTest test = HelperUtils.getTestLogger(caller);
		Set<String> urls = new HashSet<>();
		try {
			try {
				urls = SiteMapUtils.getLocURLsWithAltUrlsFromSitemapXML(SEOConfig.site, SEOConfig.user, SEOConfig.pass);
			} catch (Exception e1) {
				logger.debug("Error " + e1);
				test.log(LogStatus.FAIL, "Error in fatching urls from sitemap.xml", e1.getMessage());
			}
			for (String url : urls) {
				int responseCode = 0;
				File file = new File(SEOConfig.dataLocation, url.hashCode() + ".webUrl");
				if (file.exists()) {
					try {
						SEOPage page = streamUtils.readFile(file);
						responseCode = page.getPage().getStatusCode();
						if (responseCode == 200) {
							test.log(LogStatus.PASS, "<b>URL: </b> " + url, "StatusCode: " + responseCode);
						} else {
							test.log(LogStatus.FAIL, "<b>URL: </b> " + url, "StatusCode: " + responseCode);
						}
					} catch (Exception e) {
						logger.debug("Error " + e);
					}
				} else {
					try {
						CloseableHttpResponse res = HttpRequestUtils.getUrlResponse(url, SEOConfig.user,
								SEOConfig.pass);
						responseCode = res.getStatusLine().getStatusCode();
						EntityUtils.consume(res.getEntity());
						res.close();
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
			test.log(LogStatus.FAIL, "Test Step Failed");
		}
		ComplexReportFactory.getInstance().closeTest(test);
	}

	@Test(description = "Verify that site has custom error pages enabled", groups = {
			"Custom Error Pages" }, enabled = false)
	public void verifyCutomErrorPages() {
		Method caller = new Object() {
		}.getClass().getEnclosingMethod();
		ExtentTest test = HelperUtils.getTestLogger(caller);
		test.log(LogStatus.INFO, "Not implemented yet");
		ComplexReportFactory.getInstance().closeTest(test);
	}

}
