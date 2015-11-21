package com.vr.coursework;

import java.io.File;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.vr.coursework.helpers.VRProblem;
import com.vr.coursework.helpers.VRSolution;

public class VehicleRouting
{
	static long currTime = System.currentTimeMillis();
	private static File dataDir = new File("data");

	private static File resultDir = new File("results/clarke-wright-optimized-dell-latitude/" + currTime); 

	public static void main(String[] args) throws Exception
	{
		List<String> resultsSummary = new ArrayList<String>();
		System.out.println("Program Started");
		resultDir.mkdirs();// create dir for saving results
		
		// Going through all files in data directory
		Files.walk(Paths.get(dataDir.getAbsolutePath())).forEach(filePath -> {
		    if (Files.isRegularFile(filePath)) {
		    	String name = filePath.toFile().getName();
		    	// checking if file is a problem file 
		    	if(name.endsWith("prob.csv"))
		    	{
		    		// getting problem name
		    		String problemName = name.replace(".csv", "");
		    		try {
		    			// solving problem and saving in summary
		    			String result = solveProblem(problemName);
//		    			String result = benchmarkSolutions(problemName);
						resultsSummary.add(result);
					} catch (Exception e) {
						e.printStackTrace();
					}
		    	}
		    }
		});
		
		// Printing out summary
		for(String result : resultsSummary)
			System.out.println(result);
		
		// Saving summary to file 
		writeOut(resultDir.getAbsolutePath() + "/results-summary.csv", resultsSummary);
	}
	
	/**
	 * Function used to get costs of benchmark solutions
	 * 
	 * @param problemName - name of the problem to run
	 * @return problem name and solution cost in csv. -> "problem0000,5000"
	 * @throws Exception
	 */
	static String benchmarkSolutions(String problemName) throws Exception
	{
		String result = "";
		VRProblem p = new VRProblem(dataDir.getAbsolutePath() + "/" + problemName + ".csv");
		String solName = problemName.replace("prob", "cwsn.csv");
		File solFile = new File(dataDir.getAbsolutePath() + "/" + solName);
		if(solFile.exists())
		{
			VRSolution s = new VRSolution(p);
			s.readIn(solFile.getAbsolutePath());
			double cost = s.solnCost();
			result = problemName + "," + cost;
		}
		
		return result;
	}
	
	/**
	 * Function which runs the solutions and gathers the data about performance and cost
	 * 
	 * @param problemName - name of the problem to run
	 * @return problem name, solution cost and solution performance in milliseconds in csv. -> "problem0000,5000,1234"
	 * @throws Exception
	 */
	static String solveProblem(String problemName) throws Exception
	{
		VRProblem p = new VRProblem(dataDir.getAbsolutePath() + "/" + problemName + ".csv");
		
		VRSolution s = new VRSolution(p);
		
		long startTime = System.currentTimeMillis();
		
//		s.oneRoutePerCustomerSolution();
		s.clarkeWrightSolution();
		
		long endTime = System.currentTimeMillis();
		double cost = s.solnCost();
		long timeTaken = endTime - startTime;

		s.writeOut(resultDir.getAbsolutePath() + "/solution-" + problemName + ".csv");
		s.writeSVG(resultDir.getAbsolutePath() + "/" + problemName + ".svg", resultDir.getAbsolutePath() + "/solution-" + problemName + ".svg");
	
		return problemName + "," + cost + "," + timeTaken;
	}
	
	/**
	 * Function used to save results summary in file
	 * 
	 * @param filename - path to file
	 * @param results - List of strings in csv format
	 * @throws Exception
	 */
	public static void writeOut(String filename, List<String> results) throws Exception
	{
		PrintStream ps = new PrintStream(filename);
		for(String result:results)
		{
			ps.printf(result);
			ps.println();
		}
		ps.close();
	}
}
