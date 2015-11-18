package com.vr.coursework.helpers;
import java.awt.geom.Point2D;


public class Customer extends Point2D.Double{

	/**
	 * Customer class 
	 * 
	 * Holds customer information
	 * @see Point2D
	 */
	private static final long serialVersionUID = 16285383345341806L;
	// Counter of customers. Used for unique ID.
	private static int counter = 0;
	// Requirements of the customer (number to be delivered)
	public int c;
	// Customer id
	public int id;
	
	public Customer(int x, int y, int requirement){
		// set customer id as counter and increment it.
		this.id = counter;
		counter++;
		this.x = x;
		this.y = y;
		this.c = requirement;
	}
	
	/*
	 *  Theoretical case. 2 customers with different requirements in the same place(non-Javadoc)
	 * @see java.awt.geom.Point2D#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		return (super.equals(obj) && this.c == ((Customer)obj).c);
	}
}
