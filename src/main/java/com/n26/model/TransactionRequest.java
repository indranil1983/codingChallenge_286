package com.n26.model;

public class TransactionRequest{

	public TransactionRequest(double amount, long timestamp) {
		super();
		this.amount = amount;
		this.timestamp = timestamp;
	}
	
	public TransactionRequest() {
		
	}
	
	private double amount;
	private long timestamp;
	
	
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	@Override
	public String toString() {
		return "TransactionRequest [amount=" + amount + ", timestamp=" + timestamp + "]";
	}	
	
}
