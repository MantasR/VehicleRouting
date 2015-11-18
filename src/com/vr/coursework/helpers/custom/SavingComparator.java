package com.vr.coursework.helpers.custom;

import java.util.Comparator;

/**
 * SavingComparator
 * 
 * Used for sorting. Decides which Saving object saves more.
 * 
 * @author Mantas Rancevas
 * @see Saving
 */
public class SavingComparator implements Comparator<Saving>
{
	@Override
	public int compare(Saving arg0, Saving arg1)
	{
		if(arg0.saving < arg1.saving) return 1;
		if(arg0.saving > arg1.saving) return -1;
		return 0;
	}
}
