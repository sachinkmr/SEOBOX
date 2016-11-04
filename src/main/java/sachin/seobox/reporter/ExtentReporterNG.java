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
		ComplexReportFactory.getInstance().closeReport();
		try {
			File file = new File(SEOConfig.reportPath);
			Document doc = Jsoup.parse(file, "UTF-8");
			doc.select("div.report-name").html(SEOConfig.site);
			FileUtils.writeStringToFile(file, doc.outerHtml(), "UTF-8");
		} catch (IOException e) {
		}
		String path = SEOConfig.reportPath;
		if (null != System.getenv("JENKINS_URL") && !System.getenv("JENKINS_URL").isEmpty()) {
			path = System.getenv("JENKINS_URL").substring(0, System.getenv("JENKINS_URL").indexOf("jenkins")) + "/"
					+ SEOConfig.reportPath.substring(SEOConfig.reportPath.indexOf("SEOBOX"));
		}
		System.out.println("Report Generated: " + path);
	}
}
