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
	private static File resultDir = new File("results/clarke-wright-route-arrays-dell-latitude/" + currTime); 

	public static void main(String[] args) throws Exception
	{
		List<String> resultsSummary = new ArrayList<String>();
		System.out.println("Program Started");
		resultDir.mkdirs();// create dir for saving results
//		resultDir.cr
//		File dataDir = new File("data");
//		File resultDir = new File("results");
		
		Files.walk(Paths.get(dataDir.getAbsolutePath())).forEach(filePath -> {
		    if (Files.isRegularFile(filePath)) {
		    	String name = filePath.toFile().getName();
		    	if(name.endsWith("prob.csv"))
		    	{
//		    		System.out.println(name.replace(".csv", ""));
		    		String problemName = name.replace(".csv", "");
		    		try {
		    			String result = solveProblem(problemName);
						resultsSummary.add(result);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    	}
		    }
		});
		
		for(String result : resultsSummary)
			System.out.println(result);
		
		writeOut(resultDir.getAbsolutePath() + "/results-summary.csv", resultsSummary);

//		String problemName = "rand00020prob";
//		solveProblem(problemName);
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
