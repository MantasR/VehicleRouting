package com.vr.coursework.helpers.custom;

import java.util.ArrayList;
import java.util.List;

import com.vr.coursework.helpers.Customer;

/**
 * Route class for handling routes.
 * 
 * Adds routes to array and keeps track on capacity,
 * first and last elements, handles merging and conversation to ArrayList
 * 
 * 
 * @author Mantas Rancevas
 * @see Customer
 */
public class Route
{
	public Customer[] route;
	
	// Amount of customers in route
	public int currentCount = 0;
	// Head of the array (starting position)
	public int head = 0;

	// First and last elements of route.
	// Might be the same if only one customer in route
	private Customer first = null;
	private Customer last = null;
	
	// Capacity of the route
	private int capacityUsed = 0;
	private int capacity = 0;
	
	/**
	 * Constructor for Route object.
	 * 
	 * @param capacity - maximal amount of items
	 * @param maxCustomers - maximal number of customers in route (total number of customers)
	 * @param from - First element of route. Route can not exist without any customers
	 */
	public Route(int capacity, int maxCustomers, Customer from)
	{
		// Initializes array of customers to be twice the size of maxCustomers to support "addAtStart"
		this.route = new Customer[maxCustomers * 2];
		// Staring position at the middle to support "addAtStart"
		this.head = maxCustomers;
		this.capacity = capacity;
		
		// Adding first element and setting it to be as last as well
		this.add(from);
		this.first = from;
	}
	/**
	 * Adds customer to end of the Customers array.
	 * 
	 * @param point - customer to add
	 */
	public void add(Customer point)
	{
		// this.head + this.currentCount - position of end + 1 of the list
		this.route[this.head + this.currentCount] = point;
		currentCount++;
		this.last = point;
		
		this.capacityUsed += point.c;
	}
	
	/**
	 * Adds customer to start of the Customers array.
	 * 
	 * @param point - customer to add
	 */
	public void addAtStart(Customer point)
	{
		this.head--;// Sets head to be -1 of the current to not overwrite first element 
		this.route[this.head] = point;
		currentCount++;
		
		this.first = point;
		
		this.capacityUsed += point.c;
	}
	
	/**
	 * Checks if point is the last element in the array
	 * @param point
	 * @return true if point is last in the list; false - otherwise
	 */
	public boolean isLastDelivery(Customer point)
	{
		return this.last.id == point.id;
	}

	/**
	 * Checks if point is the first element in the array
	 * @param point
	 * @return true if point is first in the list; false - otherwise
	 */
	public boolean isFirstDelivery(Customer point)
	{
		return this.first.id == point.id;
	}
	
	/**
	 * Checks if customer requirements do not exceed the capacity
	 * 
	 * @param point - customer to check
	 * @return true if customer can be added to the list; false - otherwise
	 */
	public boolean willFit(Customer point)
	{
		return this.capacity >= (this.capacityUsed + point.c);
	}
	
	/**
	 * Checks if requirements do not exceed the capacity
	 * 
	 * Used when merging 2 routes
	 * 
	 * @param capacity - number of items to add
	 * @return true if customer can be added to the list; false - otherwise
	 */
	public boolean willFit(int capacity)
	{
		return this.capacity >= (this.capacityUsed + capacity);
	}
	
	/**
	 * Returns capacity used by this route
	 * 
	 * @return capacity used
	 */
	public int getCapacityUsed()
	{
		return this.capacityUsed;
	}
	
	/**
	 * Merge 2 routes.
	 * 
	 * Copies array of route to merge to this route, updates capacityUsed
	 * and last element.
	 * 
	 * @param route - route to merge
	 */
	public void merge(Route route)
	{
		//arraycopy(Object src, int srcPos, Object dest, int destPos, int length)
		System.arraycopy(route.route, route.head, this.route, this.head + this.currentCount, route.currentCount);
		this.currentCount += route.currentCount;
		this.capacityUsed += route.getCapacityUsed();
		this.last = route.last;
	}
	
	/**
	 * Converts customers array to ArrayList for compatibility with current "calculate cost method" 
	 * @return
	 */
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
