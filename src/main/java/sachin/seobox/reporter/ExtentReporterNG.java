package sachin.seobox.reporter;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.testng.IReporter;
import org.testng.IResultMap;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.xml.XmlSuite;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import sachin.seobox.common.SEOConfig;

public class ExtentReporterNG implements IReporter {

    @Override
    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
	// for (String name : ComplexReportFactory.nameToTestMap.keySet()) {
	// ExtentTest test = ComplexReportFactory.nameToTestMap.get(name);
	// test.log(test.getRunStatus(), "END", "Test Case Completed.");
	// ComplexReportFactory.closeTest(test);
	// }

	for (ISuite suite : suites) {
	    Map<String, ISuiteResult> result = suite.getResults();
	    for (ISuiteResult r : result.values()) {
		ITestContext context = r.getTestContext();
		buildTestNodes(context.getPassedTests(), LogStatus.PASS);
		buildTestNodes(context.getFailedTests(), LogStatus.FAIL);
		buildTestNodes(context.getSkippedTests(), LogStatus.SKIP);
	    }
	}
	ComplexReportFactory.closeReport();
	File file = new File(SEOConfig.reportPath);
	String path = SEOConfig.reportPath;
	if (null != System.getenv("JENKINS_URL") && !System.getenv("JENKINS_URL").isEmpty()) {
	    path = System.getenv("JENKINS_URL").substring(0, System.getenv("JENKINS_URL").indexOf("jenkins"))
		    + "SEOBOX/Reports" + File.separator + file.getName();
	}
	File fineNew = new File(path);
	try {
	    Document doc = Jsoup.parse(file, "UTF-8");
	    doc.select("div.report-name").html(SEOConfig.site);
	    FileUtils.writeStringToFile(fineNew, doc.outerHtml(), "UTF-8");
	} catch (IOException e) {
	    e.printStackTrace();
	}

	System.out.println("Report Generated: " + path);
    }

    private void buildTestNodes(IResultMap tests, LogStatus status) {
	ExtentTest test;
	if (tests.size() > 0) {
	    for (ITestResult result : tests.getAllResults()) {
		test = ComplexReportFactory.getTest(result.getMethod().getMethodName(),
			result.getMethod().getRealClass().getCanonicalName(), result.getMethod().getDescription());
		test.setDescription("<b>Test Case Description: </b>" + result.getMethod().getDescription());
		test.setStartedTime(getTime(result.getStartMillis()));
		test.setEndedTime(getTime(result.getEndMillis()));
		for (String group : result.getMethod().getGroups()) {
		    test.assignCategory(group);
		}
		if (result.getThrowable() != null) {
		    test.log(status, "Failed", "<br/>" + result.getThrowable().getLocalizedMessage());
		} else {
		    test.log(status, "END", "Test Case Completed.");
		}
		ComplexReportFactory.closeTest(test);
	    }
	}
    }

    private Date getTime(long millis) {
	Calendar calendar = Calendar.getInstance();
	calendar.setTimeInMillis(millis);
	return calendar.getTime();
    }

}
