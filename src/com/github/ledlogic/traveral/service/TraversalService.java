package com.github.ledlogic.traveral.service;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import com.github.ledlogic.traveral.model.TraverseRequest;
import com.github.ledlogic.traveral.util.RouterUtil;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;

public class TraversalService {

	static TraversalService singleton = new TraversalService();

	public static TraversalService getSingleton() {
		return singleton;
	}

	public List<TraverseRequest> load(Path pathInput) throws IOException {
		System.out.println("reading file " + pathInput);
		Reader reader = null;
		CSVReader csvReader = null;
		List<TraverseRequest> routedRequests = new ArrayList<TraverseRequest>();
		try {
			reader = Files.newBufferedReader(pathInput);
			csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build();
	
			// read csv line by line
			String[] record = null;
			while ((record = csvReader.readNext()) != null) {
				int index = 0;
				String reason = record[index++];
				String initialUrl = record[index++];
				String targetUrl = record[index++];
				TraverseRequest request = new TraverseRequest();
				request.setReason(reason);
				request.setInitialUrl(initialUrl);
				request.setTargetUrl(targetUrl);
				routedRequests.add(request);
			}
		}
		finally {
			IOUtils.closeQuietly(csvReader);
			IOUtils.closeQuietly(reader);
		}

		System.out.println("read " + routedRequests.size() + " requests");
		return routedRequests;
	}

	public void traverseRequests(List<TraverseRequest> routedRequests) throws IOException {
		HttpContext localContext = new BasicHttpContext();
		HttpClient httpClient = new DefaultHttpClient();
		httpClient.getParams().setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true); 

		for (TraverseRequest request : routedRequests) {
			RouterUtil.routeUrlViaClient(localContext, httpClient, request);
		}
		System.out.println("");
	}

	public void writeRequests(Path pathOutput, List<TraverseRequest> traverseRequests) throws IOException {
		System.out.println("writing file " + pathOutput);

		Writer writer = null;
		CSVWriter csvWriter = null;
		try {
			writer = Files.newBufferedWriter(pathOutput);
			csvWriter = new CSVWriter(writer);
	
			String[] header = TraverseRequest.getWriteHeader();
			csvWriter.writeNext(header);
	
			for (TraverseRequest traverseRequest: traverseRequests) {
				String[] data = traverseRequest.getWriteData();
				csvWriter.writeNext(data);
			}
		} finally {
			IOUtils.closeQuietly(csvWriter);
			IOUtils.closeQuietly(writer);
		}
	}
	
	public void traverse(Path pathInput, Path pathOutput) throws IOException {
		long t0 = System.currentTimeMillis();
		System.out.println("traverse started, " + dateFormat(t0));
		List<TraverseRequest> traverseRequests = load(pathInput);
		traverseRequests(traverseRequests);
		writeRequests(pathOutput, traverseRequests);
		displayMetrics(traverseRequests);
		long t1 = System.currentTimeMillis();
		
		System.out.println("traverse finished, " + dateFormat(t1));
		System.out.println("completed in " + ((t1 - t0)/1000.0f) + "s");
	}
	
	private String dateFormat(long t) {
		DateFormat formatter = DateFormat.getDateTimeInstance(
                DateFormat.LONG, 
                DateFormat.LONG);
		Date d = new Date(t);
		String ret = formatter.format(d);
		return ret;
	}

	private void displayMetrics(List<TraverseRequest> traverseRequests) {
		int matches = 0;
		for (TraverseRequest traverseRequest: traverseRequests) {
			matches += traverseRequest.isMatched() ? 1 : 0;
		}
		int count = traverseRequests.size();
		float pct = ((float)matches / (float)count) * 100.0f;
		
		String m = "matches/count: " + matches + "/" + count + " (" + pct + "%)";
		System.out.println(m);
	}

}