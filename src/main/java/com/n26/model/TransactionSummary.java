package com.n26.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author neil
 *
 */
public class TransactionSummary {

	private double sum;
	private double avg;
	
	private int count;
	private double max=-1;
	private double min =-1;
	
	@JsonIgnore
	private long highestTimeStamp;
	
	public double getSum() {
		return sum;
	}
	
	public void setSum(double sum) {
		this.sum = sum;
	}
	
	public double getAvg() 
	{
		if(count!=0 ) 
		{
			avg=sum/count;
		}
		return avg;
	}

	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public double getMax() {
		return max;
	}
	public void setMax(double max) {
		this.max = max;
	}
	public double getMin() {
		return min;
	}
	public void setMin(double min) {
		this.min = min;
	}

	public long getHighestTimeStamp() {
		return highestTimeStamp;
	}

	public void setHighestTimeStamp(long highestTimeStamp) {
		this.highestTimeStamp = highestTimeStamp;
	}
	
	public void resetAll() {
		sum=0;
		avg=0;
		min=0;
		max=0;
		count=0;
		
	}

	@Override
	public String toString() {
		return "TransactionSummary [sum=" + sum + ", avg=" + avg + ", count=" + count + ", max=" + max + ", min=" + min
				+ ", highestTimeStamp=" + highestTimeStamp + "]";
	}
	
	
	
	
}
