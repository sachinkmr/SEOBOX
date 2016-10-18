package sachin.seobox.reporter;

import java.io.File;

import com.relevantcodes.extentreports.DisplayOrder;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.relevantcodes.extentreports.NetworkMode;

import sachin.seobox.common.SEOConfig;
import sachin.seobox.helpers.HelperUtils;

public class ComplexReportFactory {
	private static ComplexReportFactory factory;
	private ExtentReports reporter;
	// private Map<String, ExtentTest> nameToTestMap;

	private ComplexReportFactory() {
		// nameToTestMap = new HashMap<>();
		reporter = new ExtentReports(SEOConfig.reportPath, true, DisplayOrder.OLDEST_FIRST, NetworkMode.ONLINE);
		reporter.loadConfig(new File(HelperUtils.getResourceFile("extent-config.xml")));
	}

	public synchronized static ComplexReportFactory getInstance() {
		if (factory != null)
			return factory;
		factory = new ComplexReportFactory();
		return factory;
	}

	public ExtentReports getExtentReport() {
		return reporter;
	}

	public ExtentTest getTest(String testName, String testDescription) {
		return reporter.startTest(testName, testDescription);
		// if (!nameToTestMap.containsKey(testName)) {
		// ExtentTest test = reporter.startTest(testName, testDescription);
		// nameToTestMap.put(testName, test);
		// }
		// return nameToTestMap.get(testName);
	}

	public ExtentTest getTest(String testName) {
		return getTest(testName, "");
	}

	public void closeTest(String testName) {
		if (!testName.isEmpty()) {
			ExtentTest test = getTest(testName);
			reporter.endTest(test);
		}
	}

	public void closeTest(ExtentTest test) {
		if (test != null) {
			test.log(LogStatus.INFO, "END", "Test Case Completed.");
			test.setEndedTime(HelperUtils.getTestCaseTime(System.currentTimeMillis()));
			reporter.endTest(test);
			reporter.flush();
		}
	}

	public void closeReport() {
		if (reporter != null) {
			reporter.close();
			reporter = null;
		}
		factory = null;
	}
}