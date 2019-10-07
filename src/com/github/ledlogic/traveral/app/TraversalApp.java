package com.github.ledlogic.traveral.app;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.ledlogic.traveral.model.TraverseRequest;
import com.github.ledlogic.traveral.service.TraversalService;

public class TraversalApp {

	private static final Logger LOG = LoggerFactory.getLogger(TraversalApp.class);

	static TraversalService traversalService = TraversalService.getSingleton();

	public static void main(String[] args) throws Exception {
		String csvInput = "data/prod.csv";
		String csvOutput = "data/prod-out.csv";
		Path pathInput = Paths.get(csvInput);
		Path pathOutput = Paths.get(csvOutput);
		List<TraverseRequest> traverseRequests = traversalService.load(pathInput);
		traversalService.traverse(traverseRequests);
		
		System.out.println(traverseRequests.get(0).isMatched());
		//writeOutput(pathOutput, routedRequests);
	}
}