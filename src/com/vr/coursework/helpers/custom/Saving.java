package com.vr.coursework.helpers.custom;

import com.vr.coursework.helpers.Customer;

public class Saving
{
	public Customer from;
	public Customer to;
	
	public double 	saving;
	
	public Saving(Customer from, Customer to, double saving)
	{
		this.from 	= from;
		this.to 	= to;
		this.saving = saving;
	}
}
