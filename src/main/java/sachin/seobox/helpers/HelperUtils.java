package sachin.seobox.helpers;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uci.ics.crawler4j.url.URLCanonicalizer;
import edu.uci.ics.crawler4j.url.WebURL;
import sachin.seobox.common.SEOConfig;
import sachin.seobox.seo.SEOPage;

public class HelperUtils {

	protected static final Logger logger = LoggerFactory.getLogger(HelperUtils.class);

	/**
	 * Method returns the unique string based on time stamp
	 *
	 *
	 * @return unique string
	 */
	public static String generateUniqueString() {
		DateFormat df = new SimpleDateFormat("dd-MMMM-yyyy");
		DateFormat df1 = new SimpleDateFormat("hh-mm-ss-SSaa");
		Calendar calobj = Calendar.getInstance();
		String time = df1.format(calobj.getTime());
		String date = df.format(calobj.getTime());
		return date + "_" + time;
	}

	/**
	 * Method to get current time.
	 *
	 * @return Date date object of current time
	 *
	 **/
	public static Date getTime(long millis) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(millis);
		return calendar.getTime();
	}

	public static Response getRobotFileResponse(String... data)
			throws ParseException, ClientProtocolException, IOException {
		String add = HelperUtils.getSiteAddress(data[0]) + "robots.txt";
		if (SEOConfig.PROPERTIES.getProperty("seo.robotFile") != null
				&& !SEOConfig.PROPERTIES.getProperty("seo.robotFile").isEmpty()) {
			add = SEOConfig.PROPERTIES.getProperty("seo.robotFile");
		}
		Request request = Request.Get(add)
				.addHeader("user-agent",
						SEOConfig.PROPERTIES.getProperty("crawler.userAgentString",
								"Mozilla/5.0 (Windows NT 10.0; WOW64; rv:48.0) Gecko/20100101 Firefox/48.0"))
				.connectTimeout(
						Integer.parseInt(SEOConfig.PROPERTIES.getProperty("crawler.connectionTimeout", "120000")))
				.socketTimeout(
						Integer.parseInt(SEOConfig.PROPERTIES.getProperty("crawler.connectionTimeout", "120000")));
		if (data.length > 1 && null != data[1] && !data[1].trim().isEmpty()) {
			String login = data[1] + ":" + data[2];
			String base64login = new String(Base64.encodeBase64(login.getBytes()));
			request.addHeader("Authorization", "Basic " + base64login);
		}
		return request.execute();
	}

	public static String getResourceFile(String fileName) {
		File file = null;
		try {
			String str = IOUtils.toString(HelperUtils.class.getClassLoader().getResourceAsStream(fileName));
			file = new File(SEOConfig.crawlStorageFolder, fileName);
			FileUtils.write(file, str, "utf-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file.getAbsolutePath();
	}

	public static List<SEOPage> getInternalPages() {
		System.out.println("Reading Internal Links.....");
		List<SEOPage> pages = new ArrayList<>();
		File[] urlFiles = new File(SEOConfig.dataLocation).listFiles();
		for (File file : urlFiles) {
			try {
				SEOPage page = StreamUtils.readFile(file);
				logger.debug("Verifying for: ", page.getPage().getWebURL());
				if (page.getPage().getWebURL().isInternalLink() && page.getPage().getStatusCode() == 200
						&& page.getPage().getContentType().contains("text/html")) {
					pages.add(page);
				}
			} catch (ClassNotFoundException | IOException e) {
				logger.error("error in reading file", e);
			} catch (Exception e) {
				logger.debug("Error " + e);
			}
		}
		return pages;
	}

	public static List<SEOPage> getAllPages() {
		System.out.println("Reading Links.....");
		List<SEOPage> pages = new ArrayList<>();
		File[] urlFiles = new File(SEOConfig.dataLocation).listFiles();
		for (File file : urlFiles) {
			try {
				SEOPage page = StreamUtils.readFile(file);
				pages.add(page);
			} catch (ClassNotFoundException | IOException e) {
				logger.error("error in reading file", e);
			} catch (Exception e) {
				logger.debug("Error " + e);
			}
		}
		return pages;
	}

	public static Set<WebURL> getAllOutgoingLinksOnPage() {
		Set<WebURL> urls = new HashSet<>();
		for (SEOPage page : getInternalPages()) {
			try {
				urls.addAll(page.getPage().getParseData().getOutgoingUrls());
			} catch (Exception e) {
				logger.debug("Error " + e);
			}
		}
		return urls;
	}

	public static String getSiteAddress(String address) {
		String add = URLCanonicalizer.getCanonicalURL(address);
		WebURL url = new WebURL();
		url.setURL(add);
		String domain = url.getDomain();
		String site = add.substring(0, add.indexOf(domain) + domain.length() + 1);
		return site;
	}

	public static String getResourceFile(String fileName, String pROPERTIES_LOC) {
		File file = null;
		try {
			String str = FileUtils.readFileToString(new File(pROPERTIES_LOC), "UTF-8");
			file = new File(SEOConfig.crawlStorageFolder, fileName);
			FileUtils.write(file, str, "utf-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file.getAbsolutePath();
	}
}
