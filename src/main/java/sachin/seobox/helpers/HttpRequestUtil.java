package sachin.seobox.helpers;

import java.io.IOException;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;

import edu.uci.ics.crawler4j.url.URLCanonicalizer;
import sachin.seobox.crawler.CrawlerConfig;

public class HttpRequestUtil {
	/***
	 * 
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
}
