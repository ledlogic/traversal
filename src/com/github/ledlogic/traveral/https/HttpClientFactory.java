package com.github.ledlogic.traveral.https;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClientFactory {

	private static CloseableHttpClient client;

	private static PoolingHttpClientConnectionManager connPool = null;

	private static final Logger LOG = LoggerFactory.getLogger(HttpClientFactory.class);

	private static class DummyTrustManager implements X509TrustManager {
		public java.security.cert.X509Certificate[] getAcceptedIssuers() {
			return null;
		}

		public void checkClientTrusted(X509Certificate[] certs, String authType) {
		}

		public void checkServerTrusted(X509Certificate[] certs, String authType) {
		}
	}

	static {
		try {
			SSLContext sslcontext = SSLContexts.custom().loadTrustMaterial(new TrustSelfSignedStrategy()).build();
			sslcontext.init(new KeyManager[0], new TrustManager[] { new DummyTrustManager() }, new SecureRandom());
			sslcontext.init(null, new X509TrustManager[] { new HttpsTrustManager() }, new SecureRandom());
			SSLConnectionSocketFactory ssf = new SSLConnectionSocketFactory(sslcontext, new HostnameVerifier() {
				@Override
				public boolean verify(final String s, final SSLSession sslSession) {
					return true;
				}
			});
			ConnectionSocketFactory psf = new PlainConnectionSocketFactory();
			Registry r = RegistryBuilder.create().register("https", ssf).register("http", psf).build();
			connPool = new PoolingHttpClientConnectionManager(r);
			connPool.setMaxTotal(200);
			connPool.setDefaultMaxPerRoute(20);
			client = HttpClients.custom().setConnectionManagerShared(true).setConnectionManager(connPool).setSSLSocketFactory(ssf).build();
		} catch (Exception e) {
			LOG.error("Error initiliazing HttpClientFactory :: ", e);
		}
	}

	public static CloseableHttpClient getHttpsClient() throws KeyManagementException, NoSuchAlgorithmException {
		if (client != null) {
			return client;
		}
		throw new RuntimeException("Client is not initiliazed properly");

	}

	public static void releaseInstance() {
		client = null;
	}
}