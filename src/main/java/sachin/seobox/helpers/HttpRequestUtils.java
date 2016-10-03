package sachin.seobox.helpers;

import java.io.IOException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uci.ics.crawler4j.url.URLCanonicalizer;
import sachin.seobox.crawler.CrawlerConfig;

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
		RequestConfig requestConfig = RequestConfig.custom().setExpectContinueEnabled(false)
				.setCookieSpec(CookieSpecs.IGNORE_COOKIES).setRedirectsEnabled(false)
				.setSocketTimeout(
						Integer.parseInt(CrawlerConfig.PROPERTIES.getProperty("crawler.connectionTimeout", "20000")))
				.setConnectTimeout(
						Integer.parseInt(CrawlerConfig.PROPERTIES.getProperty("crawler.connectionTimeout", "20000")))
				.build();

		RegistryBuilder<ConnectionSocketFactory> connRegistryBuilder = RegistryBuilder.create();
		connRegistryBuilder.register("http", PlainConnectionSocketFactory.INSTANCE);

		try { // Fixing:
				// https://code.google.com/p/crawler4j/issues/detail?id=174
				// By always trusting the ssl certificate
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

		Registry<ConnectionSocketFactory> connRegistry = connRegistryBuilder.build();
		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(connRegistry);
		connectionManager.setMaxTotal(10);
		connectionManager.setDefaultMaxPerRoute(10);

		HttpClientBuilder clientBuilder = HttpClientBuilder.create();
		clientBuilder.setDefaultRequestConfig(requestConfig);
		clientBuilder.setConnectionManager(connectionManager);
		clientBuilder.setUserAgent(CrawlerConfig.PROPERTIES.getProperty("crawler.userAgentString",
				"Mozilla/5.0 (Windows NT 10.0; WOW64; rv:48.0) Gecko/20100101 Firefox/48.0"));

		CloseableHttpClient httpClient = clientBuilder.build();
		if (data.length == 1 || null == data[1] || data[1].trim().isEmpty()) {
			CredentialsProvider credsProvider = new BasicCredentialsProvider();
			credsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(data[1], data[2]));
			httpClient = HttpClients.custom().setDefaultCredentialsProvider(credsProvider).build();
		}
		HttpClientContext context = HttpClientContext.create();
		String add = URLCanonicalizer.getCanonicalURL(data[0]);
		HttpGet get = new HttpGet(add);
		CloseableHttpResponse response = httpClient.execute(new HttpGet(add), context);
		get.releaseConnection();
		return response;
	}
}
