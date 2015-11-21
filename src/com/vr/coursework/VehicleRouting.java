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
	private static File resultDir = new File("results/clarke-wright-benchmark/" + currTime); 

	public static void main(String[] args) throws Exception
	{
		List<String> resultsSummary = new ArrayList<String>();
		System.out.println("Program Started");
		resultDir.mkdirs();// create dir for saving results
		
		Files.walk(Paths.get(dataDir.getAbsolutePath())).forEach(filePath -> {
		    if (Files.isRegularFile(filePath)) {
		    	String name = filePath.toFile().getName();
		    	if(name.endsWith("prob.csv"))
		    	{
		    		String problemName = name.replace(".csv", "");
		    		try {
//		    			String result = solveProblem(problemName);
		    			String result = benchmarkSolutions(problemName);
						resultsSummary.add(result);
					} catch (Exception e) {
						e.printStackTrace();
					}
		    	}
		    }
		});
		 
		for(String result : resultsSummary)
			System.out.println(result);
		
		writeOut(resultDir.getAbsolutePath() + "/results-summary.csv", resultsSummary);
	}
	
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
	
	static String solveProblem(String problemName) throws Exception
	{
		VRProblem p = new VRProblem(dataDir.getAbsolutePath() + "/" + problemName + ".csv");
		
		VRSolution s = new VRSolution(p);
		
		long startTime = System.currentTimeMillis();
		
//		s.oneRoutePerCustomerSolution(); //Cost: 15775.47015215582
		s.clarkeWrightSolution(); //Cost: 4386.4998707833365 time: 7;  Cost with merging: 3520.4639295412285 time: 8
		
		long endTime = System.currentTimeMillis();
		double cost = s.solnCost();
		long timeTaken = endTime - startTime;

		s.writeOut(resultDir.getAbsolutePath() + "/solution-" + problemName + ".csv");
		s.writeSVG(resultDir.getAbsolutePath() + "/" + problemName + ".svg", resultDir.getAbsolutePath() + "/solution-" + problemName + ".svg");
	
		return problemName + "," + cost + "," + timeTaken;
	}
	
	public static void writeOut(String filename, List<String> results) throws Exception{
		PrintStream ps = new PrintStream(filename);
		for(String result:results){
//			boolean firstOne = true;
//			for(Customer c:route){
//				if (!firstOne)
//					ps.print(",");
//				firstOne = false;
//			}
			ps.printf(result);
			ps.println();
		}
		ps.close();
	}
}
