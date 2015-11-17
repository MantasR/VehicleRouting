package com.vr.coursework.helpers;
import java.awt.geom.Point2D;


public class Customer extends Point2D.Double{

	// Requirements of the customer (number to be delivered)
	private static int counter = 0;
	public int c;
	public int id;
	
	public Customer(int x, int y, int requirement){
		this.id = counter;
		this.x = x;
		this.y = y;
		this.c = requirement;
		counter++;
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
