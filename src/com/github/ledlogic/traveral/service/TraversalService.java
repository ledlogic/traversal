package com.github.ledlogic.traveral.service;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import com.github.ledlogic.traveral.model.TraverseRequest;
import com.github.ledlogic.traveral.util.RouterUtil;
import com.opencsv.CSVReader;

public class TraversalService {
	
	static TraversalService singleton = new TraversalService();
	
	public static TraversalService getSingleton() {
		return singleton;
	}
	
	public List<TraverseRequest> load(Path pathInput) throws IOException {
		Reader reader = Files.newBufferedReader(pathInput);
		CSVReader csvReader = new CSVReader(reader, ',', '\'', 1);

		// read csv line by line
		String[] record = null;
		List<TraverseRequest> routedRequests = new ArrayList<TraverseRequest>();
		while ((record = csvReader.readNext()) != null) {
			String initialUrl = record[0];
			String targetUrl = record[1];
			TraverseRequest request = new TraverseRequest();
			request.setInitalUrl(initialUrl);
			request.setTargetUrl(targetUrl);
			routedRequests.add(request);
		}
		
		return routedRequests;
	}

	
	public void traverse(List<TraverseRequest> routedRequests) throws IOException {
		HttpContext localContext = new BasicHttpContext();
		HttpClient httpClient = new DefaultHttpClient();
		
		for (TraverseRequest request: routedRequests) {
			RouterUtil.routeUrlViaClient(localContext, httpClient, request);
		}
	}
}