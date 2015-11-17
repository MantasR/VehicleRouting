package com.vr.coursework.helpers;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vr.coursework.helpers.custom.Route;
import com.vr.coursework.helpers.custom.Saving;
import com.vr.coursework.helpers.custom.SavingComparator;

public class VRSolution {
	public VRProblem prob;
	public List<List<Customer>>soln;
	public List<Route> sol;
	
	private List<Customer> inRoutes;
	
	public VRSolution(VRProblem problem){
		this.prob = problem;
	}

	//The dumb solver adds one route per customer
	public void oneRoutePerCustomerSolution(){
		this.soln = new ArrayList<List<Customer>>();
		for(Customer c:prob.customers){
			ArrayList<Customer> route = new ArrayList<Customer>();
			route.add(c);
			soln.add(route);
		}
	}

	//Students should implement another solution
	// Clarke Wright solution
	public void clarkeWrightSolution()
	{
		this.soln = new ArrayList<List<Customer>>();
		this.sol = new ArrayList<Route>();
		this.inRoutes = new ArrayList<Customer>();
		
		List<Saving> savings = new ArrayList<Saving>();
		@SuppressWarnings("unchecked")
		List<Customer> customers = (List<Customer>) prob.customers.clone();// = prob.customers;
		
		int customersCount = prob.customers.size();
		
		for(Customer from : customers)
		{
			for(Customer to : customers)
			{
				if(!from.equals(to))
				{
					double saving = (prob.depot.distance(from) + prob.depot.distance(to)) - from.distance(to);
					savings.add(new Saving(from, to, saving));
				}
			}
		}
		
		Collections.sort(savings, new SavingComparator());
		for(Saving s : savings)
		{
			if(!this.inRoutes.contains(s.from) && !this.inRoutes.contains(s.to))
			{
				if(s.from.c + s.to.c <= prob.depot.c)
				{
					Route route = new Route(prob.depot.c, customersCount);
					route.add(s.from);
					route.add(s.to);

					this.inRoutes.add(s.from);
					this.inRoutes.add(s.to);
					customers.remove(s.from);
					customers.remove(s.to);
					
					this.sol.add(route);
				}
			}
			else
			{
				if(!this.inRoutes.contains(s.to))
				{
					for(Route route : this.sol)
					{
						if(route.isLastDelivery(s.from) && route.willFit(s.to))
						{
							route.add(s.to);
							this.inRoutes.add(s.to);
							customers.remove(s.to);
							break;
						}
					}
				}
				
				if(!this.inRoutes.contains(s.from))
				{
					for(Route route : this.sol)
					{
						if(route.isFirstDelivery(s.to) && route.willFit(s.from))
						{
							route.addAtStart(s.from);
							this.inRoutes.add(s.from);
							customers.remove(s.from);
							break;
						}
					}
				}
				
				Route merged = null;
				for(Route routeStart : this.sol)
				{
					if(merged != null)
						break;
					if(routeStart.isLastDelivery(s.from))
					{
						for(Route routeEnd : this.sol)
						{
							if(!routeStart.equals(routeEnd))
							{
								if(routeEnd.isFirstDelivery(s.to))
								{
									if(routeStart.willFit(routeEnd.getCapacityUsed()))
									{
										routeStart.merge(routeEnd);
										merged = routeEnd;
									}
								}
							}
						}
					}
				}
				
				if(merged != null)
				{
					this.sol.remove(merged);
				}
			}
		}
		while(customers.size() > 0)
		{
			Route route = new Route(prob.depot.c, customersCount);
			route.add(customers.remove(0));
			
			this.sol.add(route);
		}
	}
	
	//Calculate the total journey
	public double solnCost(){
		double cost = 0;
		if(this.sol != null && this.sol.size() > 0)
		{
			this.soln = new ArrayList<List<Customer>>();
			for(Route route : this.sol)
			{
				this.soln.add(route.toArrayList());
				Customer prev = this.prob.depot;
				for (Customer c : route.toArrayList()){
					cost += prev.distance(c);
					prev = c;
				}
				//Add the cost of returning to the depot
				cost += prev.distance(this.prob.depot);
			}
		}
		else
		{
			for(List<Customer>route:soln){
				Customer prev = this.prob.depot;
				for (Customer c:route){
					cost += prev.distance(c);
					prev = c;
				}
				//Add the cost of returning to the depot
				cost += prev.distance(this.prob.depot);
			}
		}
		
		this.sol = null;
		
		return cost;
	}
	public Boolean verify(){
		//Check that no route exceeds capacity
		Boolean okSoFar = true;
		for(List<Customer> route : soln){
			//Start the spare capacity at
			int total = 0;
			for(Customer c:route)
				total += c.c;
			if (total>prob.depot.c){
				System.out.printf("********FAIL Route starting %s is over capacity %d\n",
						route.get(0),
						total
						);
				okSoFar = false;
			}
		}
		//Check that we keep the customer satisfied
		//Check that every customer is visited and the correct amount is picked up
		Map<String,Integer> reqd = new HashMap<String,Integer>();
		for(Customer c:this.prob.customers){
			String address = String.format("%fx%f", c.x,c.y);
			reqd.put(address, c.c);
		}
		for(List<Customer> route:this.soln){
			for(Customer c:route){
				String address = String.format("%fx%f", c.x,c.y);
				if (reqd.containsKey(address))
					reqd.put(address, reqd.get(address)-c.c);
				else
					System.out.printf("********FAIL no customer at %s\n",address);
			}
		}
		for(String address:reqd.keySet())
			if (reqd.get(address)!=0){
				System.out.printf("********FAIL Customer at %s has %d left over\n",address,reqd.get(address));
				okSoFar = false;
			}
		return okSoFar;
	}
	
	public void readIn(String filename) throws Exception{
		BufferedReader br = new BufferedReader(new FileReader(filename));
		String s;
		this.soln = new ArrayList<List<Customer>>();
		while((s=br.readLine())!=null){
			ArrayList<Customer> route = new ArrayList<Customer>();
			String [] xycTriple = s.split(",");
			for(int i=0;i<xycTriple.length;i+=3)
				route.add(new Customer(
						(int)Double.parseDouble(xycTriple[i]),
						(int)Double.parseDouble(xycTriple[i+1]),
						(int)Double.parseDouble(xycTriple[i+2])));
			soln.add(route);
		}
		br.close();
	}
	
	public void writeSVG(String probFilename,String solnFilename) throws Exception{
		String[] colors = "chocolate cornflowerblue crimson cyan darkblue darkcyan darkgoldenrod".split(" ");
		int colIndex = 0;
		String hdr = 
				"<?xml version='1.0'?>\n"+
				"<!DOCTYPE svg PUBLIC '-//W3C//DTD SVG 1.1//EN' '../../svg11-flat.dtd'>\n"+
				"<svg width='8cm' height='8cm' viewBox='0 0 500 500' xmlns='http://www.w3.org/2000/svg' version='1.1'>\n";
		String ftr = "</svg>";
        StringBuffer psb = new StringBuffer();
        StringBuffer ssb = new StringBuffer();
        psb.append(hdr);
        ssb.append(hdr);
        for(List<Customer> route:this.soln){
        	ssb.append(String.format("<path d='M%s %s ",this.prob.depot.x,this.prob.depot.y));
        	for(Customer c:route)
        		ssb.append(String.format("L%s %s",c.x,c.y));
        	ssb.append(String.format("z' stroke='%s' fill='none' stroke-width='2'/>\n",
        			colors[colIndex++ % colors.length]));
        }
        for(Customer c:this.prob.customers){
        	String disk = String.format(
        			"<g transform='translate(%.0f,%.0f)'>"+
        	    	"<circle cx='0' cy='0' r='%d' fill='pink' stroke='black' stroke-width='1'/>" +
        	    	"<text text-anchor='middle' y='5'>%d</text>"+
        	    	"</g>\n", 
        			c.x,c.y,10,c.c);
        	psb.append(disk);
        	ssb.append(disk);
        }
        String disk = String.format("<g transform='translate(%.0f,%.0f)'>"+
    			"<circle cx='0' cy='0' r='%d' fill='pink' stroke='black' stroke-width='1'/>" +
    			"<text text-anchor='middle' y='5'>%s</text>"+
    			"</g>\n", this.prob.depot.x,this.prob.depot.y,20,"D");
    	psb.append(disk);
    	ssb.append(disk);
        psb.append(ftr);
        ssb.append(ftr);
        PrintStream ppw = new PrintStream(new FileOutputStream(probFilename));
        PrintStream spw = new PrintStream(new FileOutputStream(solnFilename));
        ppw.append(psb);
        spw.append(ssb);
    	ppw.close();
    	spw.close();
	}
	public void writeOut(String filename) throws Exception{
		PrintStream ps = new PrintStream(filename);
		for(List<Customer> route:this.soln){
			boolean firstOne = true;
			for(Customer c:route){
				if (!firstOne)
					ps.print(",");
				firstOne = false;
				ps.printf("%f,%f,%d",c.x,c.y,c.c);
			}
			ps.println();
		}
		ps.close();
	}
}
