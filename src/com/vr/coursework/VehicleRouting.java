package com.vr.coursework;

import java.io.File;

import com.vr.coursework.helpers.VRProblem;
import com.vr.coursework.helpers.VRSolution;

public class VehicleRouting
{

	public static void main(String[] args) throws Exception
	{
		System.out.println("Program Started");
		
		File dataDir = new File("data");
		File resultDir = new File("results");
		
		System.out.println(dataDir.getAbsolutePath() + "/rand00040prob.csv");
		VRProblem p = new VRProblem(dataDir.getAbsolutePath() + "/rand00040prob.csv");
		
		VRSolution s = new VRSolution(p);
		
		long startTime = System.currentTimeMillis();
		
//		s.oneRoutePerCustomerSolution(); Cost: 15775.47015215582
		s.clarkeWrightSolution();
		
		long endTime = System.currentTimeMillis();
		
		System.out.println("Cost: " + s.solnCost());
		System.out.println("Time Taken: " + (endTime - startTime));
		
		long currTime = System.currentTimeMillis();

		System.out.println(resultDir.getAbsolutePath() + "/solution-rand00040prob-" + currTime + ".csv");
		s.writeOut(resultDir.getAbsolutePath() + "/solution-rand00040prob-" + currTime + ".csv");
		s.writeSVG(resultDir.getAbsolutePath() + "/rand00040prob.svg", resultDir.getAbsolutePath() + "/solution-rand00040prob-" + currTime + ".svg");
	}

}
