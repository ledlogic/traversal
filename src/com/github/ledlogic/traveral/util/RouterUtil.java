package com.github.ledlogic.traveral.util;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpCoreContext;
import org.apache.http.util.EntityUtils;

import com.github.ledlogic.traveral.model.RoutedRequest;

public class RouterUtil {
	
	private RouterUtil() {
		// static-only
	}
	
	public static RoutedRequest routeUrlViaClient(HttpContext localContext, HttpClient httpClient, String routerUrl, String targetUrl) throws IOException, ClientProtocolException {
		HttpGet routerGet = new HttpGet(routerUrl);
		HttpResponse customerResponse = httpClient.execute(routerGet, localContext);
		HttpHost target = (HttpHost) localContext.getAttribute(HttpCoreContext.HTTP_TARGET_HOST);
		HttpUriRequest customerRequest = (HttpUriRequest) localContext.getAttribute(HttpCoreContext.HTTP_REQUEST);
		String customerResultUrl = filterUrl(localContext);
		HttpEntity entity = customerResponse.getEntity();
		StatusLine statusLine = customerResponse.getStatusLine();
		int statusCode = statusLine.getStatusCode();
		
		RoutedRequest ret = new RoutedRequest();
		ret.setResultUrl(customerResultUrl);
		ret.setEntity(EntityUtils.toString(entity));
		ret.setStatusCode(statusCode);
		return ret;
	}
	
	public static String filterUrl(HttpContext localContext) {
		Object targetHost = localContext.getAttribute(HttpCoreContext.HTTP_TARGET_HOST);
		HttpHost httpPost = (HttpHost) targetHost;
		HttpUriRequest httpUriRequest = (HttpUriRequest) localContext.getAttribute(HttpCoreContext.HTTP_REQUEST);
		String url = httpPost.toString() + httpUriRequest.getURI();
		if (StringUtils.contains(url, ";jsessionid=")) {
			String between = StringUtils.substringBetween(url, ";jsessionid=", "?");
			url = StringUtils.remove(url, ";jsessionid=" + between);
		}
		if (StringUtils.contains(url, ";jsessionid=")) {
			url = StringUtils.substringBefore(url, ";jsessionid=");
		}
		if (StringUtils.contains(url, "&ln=")) {
			url = StringUtils.substringBefore(url, "&ln=");
		}
		if (StringUtils.endsWith(url, "&")) {
			url = StringUtils.substring(url, 0, url.length() - 1);
		}
		return url;
	}

}