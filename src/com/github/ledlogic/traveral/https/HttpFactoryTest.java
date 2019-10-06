package com.github.ledlogic.traveral.https;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.ledlogic.traveral.model.RoutedRequest;
import com.github.ledlogic.traveral.util.RouterUtil;

public class HttpFactoryTest {

	private static final Logger LOG = LoggerFactory.getLogger(HttpFactoryTest.class);

	public static void main(String[] args) throws Exception {
		HttpContext localContext = new BasicHttpContext();
		HttpClient httpClient = new DefaultHttpClient();
		
		String routerUrl = "http://www.google.com";
		String targetUrl = "https://www.google.com";
		
		RoutedRequest routedRequest = RouterUtil.routeUrlViaClient(localContext, httpClient, routerUrl, targetUrl);
		System.out.println(routedRequest);
	}

}