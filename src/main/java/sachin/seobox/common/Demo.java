package sachin.seobox.common;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class Demo {

	public static void main(String[] args) {
		CloseableHttpClient client = HttpClients.createDefault();
		JSONObject json = null;
		String str = null;
		try {
			HttpClientContext localContext = HttpClientContext.create();
			HttpPost httpPost = new HttpPost("https://search.google.com/structured-data/testing-tool/validate");
			httpPost.addHeader("user-agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:53.0) Gecko/20100101 Firefox/53.0");
			httpPost.addHeader("referer", "https://search.google.com/structured-data/testing-tool");
			httpPost.addHeader("accept-encoding", "gzip, deflate, br");
			httpPost.addHeader("accept-language", "en-US,en;q=0.5");
			httpPost.addHeader("origin", "https://search.google.com");
			httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

			List<NameValuePair> params = new ArrayList<>();
			params.add(new BasicNameValuePair("html", "<html></html>"));
			httpPost.setEntity(new UrlEncodedFormEntity(params));
			CloseableHttpResponse response = client.execute(httpPost, localContext);
			str = EntityUtils.toString(response.getEntity(), "UTF-8");
			EntityUtils.consumeQuietly(response.getEntity());
			httpPost.releaseConnection();
			client.close();
			json = new JSONObject(str.substring(4, str.length()));
			str = null;
			json.remove("html");
			json.remove("cse");
		} catch (Exception e) {
			System.out.println("Unable to fatch page structured data for: " + e);
		}

		// SitemapParser sitemapParser = new SitemapParser();
		// Set<String> sitemapLocations =
		// sitemapParser.getSitemapLocations("https://www.google.com/");
		// int sitemapLocationSize = sitemapLocations.size();
		// String lastSitemapLocation = sitemapLocations.toArray(new
		// String[sitemapLocationSize])[sitemapLocationSize - 1];
		// Sitemap sitemap = sitemapParser.parseSitemap(lastSitemapLocation,
		// true);
		// System.out.println(lastSitemapLocation);
		// System.out.println(sitemap.getSitemapEntries());
		// System.out.println(sitemapLocationSize);
	}
}
