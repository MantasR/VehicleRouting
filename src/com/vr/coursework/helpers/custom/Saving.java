package com.vr.coursework.helpers.custom;

import com.vr.coursework.helpers.Customer;

/**
 * Saving class for storing savings between 2 points (Customers)
 * 
 * 
 * @author Mantas Rancevas
 * @see Customer
 */
public class Saving
{
	public Customer from;
	public Customer to;
	
	public double 	saving;
	
	/**
	 * Constructor for Savings object.
	 * 
	 * @param from - Customer number 1
	 * @param to - Customer number 2
	 * @param saving - savings if going through 2 customers instead of going to each of them separately.
	 */
	public Saving(Customer from, Customer to, double saving)
	{
		this.from 	= from;
		this.to 	= to;
		this.saving = saving;
	}
}
