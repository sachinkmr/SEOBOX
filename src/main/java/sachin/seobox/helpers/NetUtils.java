package sachin.seobox.helpers;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLContext;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uci.ics.crawler4j.url.URLCanonicalizer;
import sachin.seobox.crawler.CrawlerConstants;
import sachin.seobox.exception.SEOException;

public class NetUtils {
	protected static final Logger logger = LoggerFactory.getLogger(NetUtils.class);

	/***
	 * 
	 * To get response for a url
	 * 
	 * @throws IOException
	 * @throws ClientProtocolException
	 * 
	 * 
	 */

	@SuppressWarnings("deprecation")
	public static CloseableHttpResponse getUrlResponse(String... data) throws ClientProtocolException, IOException {
		String add = URLCanonicalizer.getCanonicalURL(data[0]);

		RequestConfig requestConfig = RequestConfig.custom().setExpectContinueEnabled(false).setCookieSpec(CookieSpecs.IGNORE_COOKIES).setRedirectsEnabled(false).setSocketTimeout(Integer.parseInt(CrawlerConstants.PROPERTIES.getProperty("crawler.connectionTimeout", "120000"))).setConnectTimeout(Integer.parseInt(CrawlerConstants.PROPERTIES.getProperty("crawler.connectionTimeout", "120000"))).build();

		RegistryBuilder<ConnectionSocketFactory> connRegistryBuilder = RegistryBuilder.create();
		connRegistryBuilder.register("http", PlainConnectionSocketFactory.INSTANCE);
		if (add.toLowerCase().startsWith("https://")) {
			try {
				SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, new TrustStrategy() {
					@Override
					public boolean isTrusted(final X509Certificate[] chain, String authType) {
						return true;
					}
				}).build();
				SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
				connRegistryBuilder.register("https", sslsf);
			} catch (Exception e) {
				logger.warn("Exception thrown while trying to register https");
				logger.debug("Stacktrace", e);
			}
		}
		Registry<ConnectionSocketFactory> connRegistry = connRegistryBuilder.build();
		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(connRegistry);
		connectionManager.setMaxTotal(10);
		connectionManager.setDefaultMaxPerRoute(10);

		HttpClientBuilder clientBuilder = HttpClientBuilder.create();
		clientBuilder.setDefaultRequestConfig(requestConfig);
		clientBuilder.setConnectionManager(connectionManager);
		clientBuilder.setUserAgent(CrawlerConstants.PROPERTIES.getProperty("crawler.userAgentString", "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:48.0) Gecko/20100101 Firefox/48.0"));

		CloseableHttpClient httpClient = clientBuilder.setSSLHostnameVerifier(new NoopHostnameVerifier()).build();

		if (data.length == 3 && null != data[1] && !data[1].trim().isEmpty()) {
			CredentialsProvider credsProvider = new BasicCredentialsProvider();
			credsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(data[1], data[2]));
			httpClient = HttpClients.custom().setDefaultCredentialsProvider(credsProvider).build();
		}

		HttpGet get = new HttpGet(add);
		CloseableHttpResponse response = httpClient.execute(new HttpGet(add));

		get.releaseConnection();

		// connectionManager.close();
		return response;
	}

	public static HttpResponse getUrlFluentResponse(String... data) throws ParseException, ClientProtocolException, IOException {
		String add = URLCanonicalizer.getCanonicalURL(data[0]);
		Request request = Request.Get(add).addHeader("user-agent", CrawlerConstants.PROPERTIES.getProperty("crawler.userAgentString", "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:48.0) Gecko/20100101 Firefox/48.0")).connectTimeout(Integer.parseInt(CrawlerConstants.PROPERTIES.getProperty("crawler.connectionTimeout", "120000"))).socketTimeout(Integer.parseInt(CrawlerConstants.PROPERTIES.getProperty("crawler.connectionTimeout", "120000")));
		if (data.length > 1 && null != data[1] && !data[1].trim().isEmpty()) {
			String login = data[1] + ":" + data[2];
			String base64login = new String(Base64.encodeBase64(login.getBytes()));
			request.addHeader("Authorization", "Basic " + base64login);
		}
		return request.execute().returnResponse();
	}

	public static JSONObject getStructuredData(String html, String url) throws SEOException {
		CloseableHttpClient client = HttpClients.createDefault();
		JSONObject json = null;
		try {
			HttpClientContext localContext = HttpClientContext.create();
			HttpPost httpPost = new HttpPost(CrawlerConstants.PAGE_STRUCTURE_URL);
			httpPost.addHeader("user-agent", CrawlerConstants.USER_AGENT);
			httpPost.addHeader("referer", CrawlerConstants.PAGE_STRUCTURE_URL);
			httpPost.addHeader("accept-encoding", "gzip, deflate, br");
			httpPost.addHeader("accept-language", "en-US,en;q=0.8");
			httpPost.addHeader("x-client-data", "CIW2yQEIpLbJAQjBtskB");
			httpPost.addHeader("origin", "https://search.google.com");

			List<NameValuePair> params = new ArrayList<>();
			params.add(new BasicNameValuePair("html", html));
			httpPost.setEntity(new UrlEncodedFormEntity(params));
			CloseableHttpResponse response = client.execute(httpPost, localContext);
			String str = EntityUtils.toString(response.getEntity(), "UTF-8");
			EntityUtils.consumeQuietly(response.getEntity());
			httpPost.releaseConnection();
			client.close();
			json = new JSONObject(str.substring(4, str.length()));
			json.remove("html");
			json.remove("cse");
		} catch (IOException e) {
			logger.debug("Unable to fatch structured data", e);
		}
		if (json == null) {
			throw new SEOException("Unable to fatch page structured data. url: " + url);
		}
		return json;
	}

	public static JSONObject getPageSpeedData(String url, String strategy) throws SEOException {
		JSONObject json = null;
		try {
			CloseableHttpClient client = HttpClients.createDefault();
			URIBuilder builder = new URIBuilder(CrawlerConstants.PAGE_SPEED_URL);
			builder.addParameter("key", CrawlerConstants.PAGE_SPEED_KEY);
			builder.addParameter("locale", "en_US");
			builder.addParameter("url", url);
			builder.addParameter("strategy", strategy);

			HttpGet httpget = new HttpGet(builder.build());
			httpget.addHeader("user-agent", CrawlerConstants.USER_AGENT);
			CloseableHttpResponse response = client.execute(httpget);
			String str = EntityUtils.toString(response.getEntity(), "UTF-8");
			EntityUtils.consumeQuietly(response.getEntity());
			httpget.releaseConnection();
			client.close();
			json = new JSONObject(str);
			if (json.has("error")) {
				throw new SEOException("Google Page Speed Insight API is throwing error");
			} else {
				json.remove("version");
				json.remove("title");
				json.remove("snapshots");
				json.remove("screenshot");
				json.remove("kind");
				json.remove("captchaResult");
				json.remove("id");
			}

		} catch (URISyntaxException | IOException e) {
			logger.debug("Unable to fatch page speed data", e);
		}
		if (json == null) {
			throw new SEOException("Unable to fatch page speed data. url: " + url);
		}
		return json;
	}

	public static String[] getRedirectedURL(String... data) {
		try {
			Connection con = Jsoup.connect(data[0]).header("user-agent", CrawlerConstants.PROPERTIES.getProperty("crawler.userAgentString", "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:48.0) Gecko/20100101 Firefox/48.0")).timeout(Integer.parseInt(CrawlerConstants.PROPERTIES.getProperty("crawler.connectionTimeout", "120000"))).followRedirects(true);
			if (data.length > 1 && null != data[1] && !data[1].trim().isEmpty()) {
				String login = data[1] + ":" + data[2];
				String base64login = new String(Base64.encodeBase64(login.getBytes()));
				con.header("Authorization", "Basic " + base64login);
			}
			Response r = con.execute();
			return new String[] { Integer.toString(r.statusCode()), r.url().toExternalForm() };
		} catch (Exception e) {
			logger.error("Error fetching response.\n", e);
			if (e instanceof HttpStatusException) {
				HttpStatusException ex = (HttpStatusException) e;
				return new String[] { Integer.toString(ex.getStatusCode()), ex.getUrl() };
			}
			return new String[] { "", data[0], e.getMessage() };
		}

	}

}
