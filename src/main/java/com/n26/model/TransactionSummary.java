package com.n26.model;

/**
 * @author neil
 *
 */
public class TransactionSummary {

	private double sum;
	private double avg;
	
	private int count;
	private double max;
	private double min;
	public double getSum() {
		return sum;
	}
	
	public void setSum(double sum) {
		this.sum = sum;
	}
	
	public double getAvg() {
		if(count!=0 ) {
			avg=sum/count;
		}
		else {
			avg=0;
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
	
	
	
	
}
