package com.github.ledlogic.traveral.app;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.github.ledlogic.traveral.service.TraversalService;

public class TraversalApp {

	static TraversalService traversalService = TraversalService.getSingleton();

	public static void main(String[] args) throws Exception {
		String csvInput = "data/prod.csv";
		String csvOutput = "data/prod-out.csv";
		Path pathInput = Paths.get(csvInput);
		Path pathOutput = Paths.get(csvOutput);
		traversalService.traverse(pathInput, pathOutput);
	}
}