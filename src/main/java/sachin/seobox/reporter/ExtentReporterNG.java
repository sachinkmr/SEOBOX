package sachin.seobox.reporter;

import java.util.List;

import org.testng.IReporter;
import org.testng.ISuite;
import org.testng.xml.XmlSuite;

import sachin.seobox.common.SEOConfig;

public class ExtentReporterNG implements IReporter {

	@Override
	public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
		System.out.println("\nGenerating Report, Please Wait.....");
		System.out.println("----------------------------------------------------------");
		ComplexReportFactory.getInstance().closeReport();

		String path = SEOConfig.reportPath;
		if (null != System.getenv("JENKINS_URL") && !System.getenv("JENKINS_URL").isEmpty()) {
			path = System.getenv("JENKINS_URL").substring(0, System.getenv("JENKINS_URL").indexOf("jenkins")) + "/"
					+ SEOConfig.reportPath.substring(SEOConfig.reportPath.indexOf("SEOBOX"));
		}
		System.out.println("Report Generated: " + path);
	}
}
