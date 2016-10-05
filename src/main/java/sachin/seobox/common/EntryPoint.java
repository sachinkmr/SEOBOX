package sachin.seobox.common;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.TestNG;

import sachin.seobox.exception.SEOException;
import sachin.seobox.helpers.HelperUtils;
import sachin.seobox.reporter.ExtentReporterNG;

public class EntryPoint {
	protected static final Logger logger = LoggerFactory.getLogger(EntryPoint.class);

	public static void main(String[] args) {
		// System.setProperty("SiteAddress", "http://us.tigiprofessional.com/");
		try {
			if (null == System.getProperty("SiteAddress") || System.getProperty("SiteAddress").isEmpty()) {
				throw new SEOException("Site url is missing");
			} else {
				SEOConfig.site = System.getProperty("SiteAddress");
				SEOConfig.user = System.getProperty("Username");
				SEOConfig.pass = System.getProperty("Password");
			}
			List<String> suites = new ArrayList<>();
			suites.add(HelperUtils.getResourceFile("testng.xml"));
			TestNG testng = new TestNG();
			testng.setTestSuites(suites);
			testng.addListener(new ExtentReporterNG());
			testng.setUseDefaultListeners(false);
			testng.setVerbose(0);
			testng.run();
		} catch (SEOException e) {
			System.out.println(e);
			logger.error("\n\n\nError occured:  " + e + "\n\n");
			System.exit(1);
		}
	}
}
