package sachin.seobox.helpers;

import java.io.IOException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
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
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
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
}
