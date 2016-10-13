package sachin.seobox.reporter;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.testng.IReporter;
import org.testng.ISuite;
import org.testng.xml.XmlSuite;

import sachin.seobox.common.SEOConfig;

public class ExtentReporterNG implements IReporter {

    @Override
    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
	System.out.println("\nGenerating Report, Please Wait.....");
	System.out.println("----------------------------------------------------------");
//	
//	for (ISuite suite : suites) {
//	    Map<String, ISuiteResult> result = suite.getResults();
//	    for (ISuiteResult r : result.values()) {
//		ITestContext context = r.getTestContext();
//		buildTestNodes(context.getPassedTests(), LogStatus.PASS);
//		buildTestNodes(context.getFailedTests(), LogStatus.FAIL);
//		buildTestNodes(context.getSkippedTests(), LogStatus.SKIP);
//	    }
//	}
	ComplexReportFactory.getInstance().closeReport();
	try {
	    File file = new File(SEOConfig.reportPath);
	    Document doc = Jsoup.parse(file, "UTF-8");
	    doc.select("div.report-name").html(SEOConfig.site);
	    FileUtils.writeStringToFile(file, doc.outerHtml(), "UTF-8");
	} catch (IOException e) {
	    e.printStackTrace();
	}
	String path = SEOConfig.reportPath;
	if (null != System.getenv("JENKINS_URL") && !System.getenv("JENKINS_URL").isEmpty()) {
	    path = System.getenv("JENKINS_URL").substring(0, System.getenv("JENKINS_URL").indexOf("jenkins")) + "/"
		    + SEOConfig.reportPath.substring(SEOConfig.reportPath.indexOf("SEOBOX"));
	}
	System.out.println("Report Generated: " + path);
    }

    // private void buildTestNodes(IResultMap tests, LogStatus status) {
    // ExtentTest test;
    // if (tests.size() > 0) {
    // for (ITestResult result : tests.getAllResults()) {
    // test =
    // ComplexReportFactory.getInstance().getTest(result.getMethod().getMethodName(),
    // result.getMethod().getDescription());
    // test.setDescription("<b>Test Case Description: </b>" +
    // result.getMethod().getDescription());
    // test.setStartedTime(getTime(result.getStartMillis()));
    // test.setEndedTime(getTime(result.getEndMillis()));
    // for (String group : result.getMethod().getGroups()) {
    // test.assignCategory(group);
    // }
    // if (result.getThrowable() != null) {
    // test.log(status, "Failed", "<br/>" +
    // result.getThrowable().getLocalizedMessage());
    // } else {
    // test.log(status, "END", "Test Case Completed.");
    // }
    // ComplexReportFactory.getInstance().closeTest(test);
    // }
    // }
    // }

    

}
