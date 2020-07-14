package com.github.ledlogic.traveral.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpCoreContext;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.github.ledlogic.traveral.model.TraversalRequest;

public class RouterUtil {

	private final static Logger LOG = Logger.getLogger(RouterUtil.class);

	private RouterUtil() {
		// static-only
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

	public static void routeUrlViaClient(HttpContext localContext, HttpClient httpClient, TraversalRequest request) {
		try {
			// load initial settings
			String routerUrl = request.getInitialUrl();
			String targetUrl = request.getTargetUrl();
			
			// execute get against request url
			HttpGet routerGet = new HttpGet(routerUrl);
			HttpResponse customerResponse = httpClient.execute(routerGet, localContext);
			String customerResultUrl = filterUrl(localContext);
			StatusLine statusLine = customerResponse.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			boolean matched = StringUtils.equals(targetUrl, customerResultUrl);
			HttpEntity entity = customerResponse.getEntity();
			EntityUtils.consumeQuietly(entity);

			// update results
			request.setResultUrl(customerResultUrl);
			request.setStatusCode(statusCode);
			request.setMatched(matched);
			System.out.print(".");
		} catch (Exception e) {
			LOG.warn("Could not perform route request[" + request + "]", e);
			System.out.print("e");
			request.setExceptionMessage(e.getMessage());
		}
	}

}