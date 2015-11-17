package com.vr.coursework.helpers.custom;

import java.util.ArrayList;
import java.util.List;

import com.vr.coursework.helpers.Customer;

public class Route
{
	public List<Customer> route = new ArrayList<Customer>();
	public Customer[] route2;// = new Customer[10];
	
	private Customer last = null;
	
	private int capacityUsed = 0;
	private int capacity = 0;
	
	public Route(int capacity)
	{
		this.capacity = capacity;
	}
	
	public void add(Customer point)
	{
		this.route.add(point);
		this.last = point;
		
		this.capacityUsed += point.c;
	}
	
	public void addAtStart(Customer point)
	{
		this.route.add(0, point);
//		this.last = point;
		
		this.capacityUsed += point.c;
	}
	
	public boolean isLastDelivery(Customer point)
	{
		return this.last.equals(point);
	}
	
	public boolean isFirstDelivery(Customer point)
	{
		return this.route.get(0).equals(point);
	}
	
	public boolean willFit(Customer point)
	{
		return this.capacity >= (this.capacityUsed + point.c);
	}
	
	public boolean willFit(int capacity)
	{
		return this.capacity >= (this.capacityUsed + capacity);
	}
	
	public int getCapacityUsed()
	{
		return this.capacityUsed;
	}
	
	public void merge(Route route)
	{
		this.route.addAll(route.route);
		this.last = route.route.get(route.route.size() - 1);
		this.capacityUsed += route.getCapacityUsed();
	}
	
}
