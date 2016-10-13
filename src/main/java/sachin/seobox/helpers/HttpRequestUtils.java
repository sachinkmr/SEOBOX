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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uci.ics.crawler4j.url.URLCanonicalizer;
import sachin.seobox.common.SEOConfig;

public class HttpRequestUtils {
	protected static final Logger logger = LoggerFactory.getLogger(HttpRequestUtils.class);

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

		RequestConfig requestConfig = RequestConfig.custom().setExpectContinueEnabled(false)
				.setCookieSpec(CookieSpecs.IGNORE_COOKIES).setRedirectsEnabled(false)
				.setSocketTimeout(
						Integer.parseInt(SEOConfig.PROPERTIES.getProperty("crawler.connectionTimeout", "120000")))
				.setConnectTimeout(
						Integer.parseInt(SEOConfig.PROPERTIES.getProperty("crawler.connectionTimeout", "120000")))
				.build();

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
				SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext,
						SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
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
		clientBuilder.setUserAgent(SEOConfig.PROPERTIES.getProperty("crawler.userAgentString",
				"Mozilla/5.0 (Windows NT 10.0; WOW64; rv:48.0) Gecko/20100101 Firefox/48.0"));

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

	public static HttpResponse getUrlFluentResponse(String... data)
			throws ParseException, ClientProtocolException, IOException {
		String add = URLCanonicalizer.getCanonicalURL(data[0]);
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
		return request.execute().returnResponse();
	}

	public static JSONObject getStructuredData(String html) {
		CloseableHttpClient client = HttpClients.createDefault();
		try {
			HttpClientContext localContext = HttpClientContext.create();
			HttpPost httpPost = new HttpPost(SEOConfig.PAGE_STRUCTURE_URL);
			httpPost.addHeader("user-agent", SEOConfig.USER_AGENT);
			httpPost.addHeader("referer", SEOConfig.PAGE_STRUCTURE_URL);
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
			JSONObject json = new JSONObject(str.substring(4, str.length()));
			json.remove("html");
			json.remove("cse");
			return json;
		} catch (IOException e) {
			logger.debug("Unable to fatch structured data", e);
		}
		return null;
	}

	public static JSONObject getPageSpeedData(String url, String strategy) {
		try {
			CloseableHttpClient client = HttpClients.createDefault();
			URIBuilder builder = new URIBuilder(SEOConfig.PAGE_SPEED_URL);
			builder.addParameter("key", SEOConfig.PAGE_SPEED_KEY);
			builder.addParameter("locale", "en_US");
			builder.addParameter("url", url);
			builder.addParameter("strategy", strategy);

			HttpGet httpget = new HttpGet(builder.build());
			httpget.addHeader("user-agent", SEOConfig.USER_AGENT);
			CloseableHttpResponse response = client.execute(httpget);
			String str = EntityUtils.toString(response.getEntity(), "UTF-8");
			EntityUtils.consumeQuietly(response.getEntity());
			httpget.releaseConnection();
			client.close();
			return new JSONObject(str);
		} catch (URISyntaxException | IOException e) {
			logger.debug("Unable to fatch page speed data", e);
		}
		return null;
	}

}
