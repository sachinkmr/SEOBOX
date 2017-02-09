package sachin.seobox.common;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.TestNG;

import sachin.seobox.crawler.CrawlerConstants;
import sachin.seobox.exception.SEOException;
import sachin.seobox.helpers.HelperUtils;
import sachin.seobox.helpers.HttpRequestUtils;
import sachin.seobox.reporter.ExtentReporterNG;
import sachin.seobox.reporter.SEOTransformer;

public class EntryPoint {
	protected static final Logger logger = LoggerFactory.getLogger(EntryPoint.class);

	public static void main(String[] args) {
		// System.setProperty("SiteAddress", "http://www.dove.com/");
		// System.setProperty("Username", "d2showcase");
		// System.setProperty("Password", "D2$0wca$3");
		if (null == System.getProperty("SiteAddress") || System.getProperty("SiteAddress").isEmpty()) {
			try {
				throw new SEOException("Site url is missing");
			} catch (SEOException e) {
				logger.error("Error in application: ", e);
				return;
			}
		}
		String red = HttpRequestUtils.getRedirectedURL(System.getProperty("SiteAddress"),
				System.getProperty("Username"), System.getProperty("Password"));
		if (red != null) {
			CrawlerConstants.SITE = red;
		}
		try {
			if (HelperUtils
					.getFluentResponse(CrawlerConstants.SITE, CrawlerConstants.USERNAME, CrawlerConstants.PASSWORD)
					.returnResponse().getStatusLine().getStatusCode() != 200) {
				throw new Exception(
						"URL is down, something went wrong or there is some error in faching URL data. Please review log for more detail.");
			}
			List<String> suites = new ArrayList<>();
			suites.add(HelperUtils.getResourceFile("testng.xml"));
			TestNG testng = new TestNG();
			testng.setTestSuites(suites);
			testng.setAnnotationTransformer(new SEOTransformer());
			testng.setUseDefaultListeners(false);
			testng.setPreserveOrder(true);
			testng.setVerbose(0);
			testng.run();
		} catch (Exception e) {
			logger.error("Error in application: ", e);
			CrawlerConstants.ERROR = true;
			CrawlerConstants.ERROR_TEXT = "URL is down, something went wrong or there is some error in faching URL data. Please review log for more detail. <br/> Error: "
					+ e.getMessage();
		}
		CrawlerConstants.SITE = System.getProperty("SiteAddress");
		ExtentReporterNG.generateReport();
	}
}
