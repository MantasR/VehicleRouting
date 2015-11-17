package com.vr.coursework.helpers.custom;

import java.util.ArrayList;
import java.util.List;

import com.vr.coursework.helpers.Customer;

public class Route
{
//	public List<Customer> route = new ArrayList<Customer>();
	public Customer[] route;// = new Customer[10];
	public int currentCount = 0;
	public int head = 0;
	
	private int capacityUsed = 0;
	private int capacity = 0;
	
	public Route(int capacity, int maxCustomers)
	{
		this.route = new Customer[maxCustomers * 2];
		this.head = maxCustomers;
		this.capacity = capacity;
	}
	
	public void add(Customer point)
	{
		this.route[this.head + this.currentCount] = point;
		currentCount++;
		
		this.capacityUsed += point.c;
	}
	
	public void addAtStart(Customer point)
	{
		this.head--;
		this.route[this.head] = point;
		currentCount++;
//		this.last = point;
		
		this.capacityUsed += point.c;
	}
	
	public boolean isLastDelivery(Customer point)
	{
		return this.route[this.head + this.currentCount - 1].equals(point);
	}
	
	public boolean isFirstDelivery(Customer point)
	{
		return this.route[this.head].equals(point);
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
		//arraycopy(Object src, int srcPos, Object dest, int destPos, int length)
		System.arraycopy(route.route, route.head, this.route, this.head + this.currentCount, route.currentCount);
		this.currentCount += route.currentCount;
		this.capacityUsed += route.getCapacityUsed();
	}
	
	public List<Customer> toArrayList()
	{
		List<Customer> list = new ArrayList<Customer>();
		
		for(int i = this.head; i < this.head + this.currentCount; i++)
		{
			list.add(this.route[i]);
		}
		
		return list;
	}
}
