package sachin.seobox.reporter;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import org.apache.commons.io.FileUtils;
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

		if (null != System.getenv("JENKINS_URL") && !System.getenv("JENKINS_URL").isEmpty()) {
			String path = SEOConfig.reportPath.substring(0, SEOConfig.reportPath.indexOf("webapps\\") + 8);
			try {
				System.out.println("Report Generated: "
						+ SEOConfig.reportPath.replace(path, "http://" + InetAddress.getLocalHost().getHostAddress()));
			} catch (UnknownHostException e) {

			}
			FileUtils.deleteQuietly(new File(System.getProperty("user.dir"), "Config.properties"));
			FileUtils.deleteQuietly(new File(System.getProperty("user.dir"), "CrawlerConfigFile"));
		} else {
			System.out.println("Report Generated: " + SEOConfig.reportPath);
		}
		FileUtils.deleteQuietly(new File(SEOConfig.crawlStorageFolder));
	}
}
