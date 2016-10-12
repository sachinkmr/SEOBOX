package sachin.seobox.reporter;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.relevantcodes.extentreports.DisplayOrder;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.NetworkMode;

import sachin.seobox.common.SEOConfig;
import sachin.seobox.helpers.HelperUtils;

public class ComplexReportFactory {
	private static ComplexReportFactory factory;
	private ExtentReports reporter;
	private Map<String, ExtentTest> nameToTestMap;

	private ComplexReportFactory() {
		nameToTestMap = new HashMap<>();
		reporter = new ExtentReports(SEOConfig.reportPath, true, DisplayOrder.OLDEST_FIRST, NetworkMode.ONLINE);
		reporter.loadConfig(new File(HelperUtils.getResourceFile("extent-config.xml")));
	}

	public synchronized static ComplexReportFactory getInstance() {
		if (factory != null)
			return factory;
		factory = new ComplexReportFactory();
		return factory;
	}

	public ExtentTest getTest(String testName, String className, String testDescription) {
		String name = className + "." + testName;
		if (!nameToTestMap.containsKey(name)) {
			ExtentTest test = reporter.startTest(testName, testDescription);
			nameToTestMap.put(name, test);
		}
		return nameToTestMap.get(name);
	}

	public ExtentReports getExtentReport() {
		return reporter;
	}

	public ExtentTest getTest(String testName, String testDescription) {
		if (!nameToTestMap.containsKey(testName)) {
			ExtentTest test = reporter.startTest(testName, testDescription);
			nameToTestMap.put(testName, test);
		}
		return nameToTestMap.get(testName);
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
			reporter.endTest(test);
		}
	}

	public void closeReport() {
		if (reporter != null) {
			reporter.flush();
			reporter.close();
			reporter = null;
		}
	}
}