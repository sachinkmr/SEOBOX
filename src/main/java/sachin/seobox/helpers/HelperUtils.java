package sachin.seobox.helpers;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;

import edu.uci.ics.crawler4j.url.URLCanonicalizer;
import sachin.seobox.crawler.CrawlerConfig;

public class HelperUtils {

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
		String add = URLCanonicalizer.getCanonicalURL(data[0] + "/robots.txt");
		Response response = null;
		if (data.length == 1 || null == data[1] || data[1].trim().isEmpty()) {
			response = Request.Get(add)
					.connectTimeout(Integer
							.parseInt(CrawlerConfig.PROPERTIES.getProperty("crawler.connectionTimeout", "20000")))
					.socketTimeout(Integer
							.parseInt(CrawlerConfig.PROPERTIES.getProperty("crawler.connectionTimeout", "20000")))
					.execute();

		} else {
			String login = data[1] + ":" + data[2];
			String base64login = new String(Base64.encodeBase64(login.getBytes()));
			response = Request.Get(add).addHeader("Authorization", "Basic " + base64login)
					.connectTimeout(Integer
							.parseInt(CrawlerConfig.PROPERTIES.getProperty("crawler.connectionTimeout", "20000")))
					.socketTimeout(Integer
							.parseInt(CrawlerConfig.PROPERTIES.getProperty("crawler.connectionTimeout", "20000")))
					.execute();
		}
		return response;
	}

	/***
	 * To get response for a url
	 * 
	 * 
	 */
	public static Response getUrlResponse(String... data) throws ParseException, ClientProtocolException, IOException {
		String add = URLCanonicalizer.getCanonicalURL(data[0]);
		Response response = null;
		if (data.length == 1 || null == data[1] || data[1].trim().isEmpty()) {
			response = Request.Get(add)
					.connectTimeout(Integer
							.parseInt(CrawlerConfig.PROPERTIES.getProperty("crawler.connectionTimeout", "20000")))
					.socketTimeout(Integer
							.parseInt(CrawlerConfig.PROPERTIES.getProperty("crawler.connectionTimeout", "20000")))
					.execute();

		} else {
			String login = data[1] + ":" + data[2];
			String base64login = new String(Base64.encodeBase64(login.getBytes()));
			response = Request.Get(add).addHeader("Authorization", "Basic " + base64login)
					.connectTimeout(Integer
							.parseInt(CrawlerConfig.PROPERTIES.getProperty("crawler.connectionTimeout", "20000")))
					.socketTimeout(Integer
							.parseInt(CrawlerConfig.PROPERTIES.getProperty("crawler.connectionTimeout", "20000")))
					.execute();
		}
		return response;
	}

	public static String getResourceFile(String fileName) {
		File file = null;
		try {
			String str = IOUtils.toString(HelperUtils.class.getClassLoader().getResourceAsStream(fileName));
			file = new File(System.getProperty("user.dir"), fileName);
			FileUtils.write(file, str, "utf-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file.getAbsolutePath();
	}
}