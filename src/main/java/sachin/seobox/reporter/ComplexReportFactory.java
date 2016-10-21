package sachin.seobox.reporter;

import java.io.File;
import java.text.SimpleDateFormat;

import org.json.JSONArray;
import org.json.JSONObject;

import com.relevantcodes.extentreports.DisplayOrder;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.relevantcodes.extentreports.NetworkMode;
import com.relevantcodes.extentreports.model.Log;
import com.relevantcodes.extentreports.view.Icon;

import sachin.seobox.common.SEOConfig;
import sachin.seobox.helpers.HelperUtils;
import sachin.seobox.helpers.StreamUtils;

public class ComplexReportFactory {
	private static ComplexReportFactory factory;
	private ExtentReports reporter;
	private StreamUtils stream = new StreamUtils();
	private SimpleDateFormat df = new SimpleDateFormat("h:mm:ss a");
	private File file = new File(SEOConfig.outputDirectory + File.separator + "methods");
	private File tests = new File(new File(SEOConfig.dataLocation).getParentFile(), "tests");

	private ComplexReportFactory() {
		file.mkdirs();
		tests.mkdirs();
		reporter = new ExtentReports(SEOConfig.reportPath, true, DisplayOrder.OLDEST_FIRST, NetworkMode.ONLINE);
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
			Icon ic = new Icon();
			JSONObject json = new JSONObject();
			String id = test.getTest().getId().toString();
			json.put("id", id);
			json.put("name", test.getTest().getName());
			JSONArray arr1 = new JSONArray();
			for (Log log : test.getTest().getLogList()) {
				JSONArray arr = new JSONArray();
				arr.put(ic.getIcon(log.getLogStatus()));
				arr.put(log.getLogStatus().name());
				arr.put(df.format(log.getTimestamp()));
				arr.put(log.getStepName());
				arr.put(log.getDetails());
				arr1.put(arr);
			}
			json.put("logs", arr1);
			stream.writeJSON(new File(file, id + ".json"), json);
			stream.writeTestCase(new File(tests, id + ".test"), test);
			test = null;
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