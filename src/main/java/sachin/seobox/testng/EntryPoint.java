package sachin.seobox.testng;

import java.util.ArrayList;
import java.util.List;

import org.testng.TestNG;

import sachin.seobox.helpers.HelperUtils;
import sachin.seobox.reporter.ExtentReporterNG;

public class EntryPoint {

	public static void main(String[] args) {
		System.out.println(System.getProperty("JOB_NAME"));
		System.out.println(System.getProperty("JENKINS_URL"));
		System.out.println(System.getenv("JOB_NAME"));
		System.out.println(System.getenv("JENKINS_URL"));
		try {
			List<String> suites = new ArrayList<>();
			suites.add(HelperUtils.getResourceFile("testng.xml"));
			TestNG testng = new TestNG();
			testng.setTestSuites(suites);
			testng.addListener(new ExtentReporterNG());
			testng.setUseDefaultListeners(false);
			testng.setVerbose(0);
			testng.run();
		} catch (Exception e) {
			System.out.println(e);
			System.exit(1);
		}
	}
}
