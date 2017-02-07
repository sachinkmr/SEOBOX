package sachin.seobox.reporter;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import sachin.seobox.crawler.CrawlerConstants;

public class ExtentReporterNG {
	public static void generateReport() {
		System.out.println("\nGenerating Report, Please Wait.....");
		System.out.println("----------------------------------------------------------");
		ComplexReportFactory.getInstance().closeReport();
		if (CrawlerConstants.ERROR && !CrawlerConstants.ERROR_TEXT.isEmpty()) {
			try {
				File file = new File(CrawlerConstants.REPORT_PATH);
				Document doc = Jsoup.parse(file, "UTF-8");
				Element e = doc.select("div.loading").first();
				e.attr("style", "display:block");
				String errorText = "URL: <a href='" + CrawlerConstants.SITE + "'>" + CrawlerConstants.SITE + "</a><br/>"
						+ CrawlerConstants.ERROR_TEXT;
				e.select("#error").first().html(errorText);
				FileUtils.writeStringToFile(file, doc.outerHtml(), "UTF-8");
			} catch (IOException e) {
			}
		}
		if ((null != System.getProperty("JENKINS_URL") && !System.getProperty("JENKINS_URL").isEmpty())
				|| (null != System.getProperty("WEB") && !System.getProperty("WEB").isEmpty())) {
			String path = CrawlerConstants.REPORT_PATH.substring(0,
					CrawlerConstants.REPORT_PATH.indexOf("webapps\\") + 8);
			try {
				System.out.println("Report Generated: " + CrawlerConstants.REPORT_PATH.replace(path,
						"http://" + InetAddress.getLocalHost().getHostAddress()));
			} catch (UnknownHostException e) {

			}
			FileUtils.deleteQuietly(new File(System.getProperty("user.dir"), "Config.properties"));
			FileUtils.deleteQuietly(new File(System.getProperty("user.dir"), "CrawlerConfigFile"));
		} else {
			System.out.println("Report Generated: " + CrawlerConstants.REPORT_PATH);
		}

	}
}
