package com.n26.service;

import java.util.Date;

import javax.transaction.InvalidTransactionException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.n26.exceptions.InvalidTimestampException;
import com.n26.model.TransactionRequest;
import com.n26.model.TransactionSummary;

@Service
public class TransactionService 
{
	private static final Logger logger = LogManager.getLogger(TransactionService.class);
	
	@Autowired
	StatisticsBean statbean;


	public void logTransaction(TransactionRequest transactionRequest) throws InvalidTimestampException 
	{
		//logger.info("Logging transaction "+transactionRequest);
		transactionRequest.getTimestamp();
		insertIntoArray(transactionRequest);		
	}


	private void insertIntoArray(TransactionRequest transactionRequest) throws InvalidTimestampException {
		if(StatisticsBean.appStartTime==0) 
		{
			StatisticsBean.appStartTime = new Date().getTime();
		}
		checkValidTransactionTime(transactionRequest.getTimestamp());
		long diffFromFirstInsert = transactionRequest.getTimestamp() - (StatisticsBean.appStartTime);
		int[] pointer = calculate2dArrayPointer(diffFromFirstInsert);
		if(pointer[0]!=StatisticsBean.currentArrayPointer)
		{
			for(int j=0;j<60000;j++) {
				statbean.getTransactionArray()[pointer[0]][j]=null;
			}
			StatisticsBean.currentArrayPointer=pointer[0];
		}						
		TransactionSummary objTransactionSummary = statbean.getTransactionArray()[pointer[0]][pointer[1]];
		if(objTransactionSummary==null) {
			objTransactionSummary = new TransactionSummary();
		}
		calculateSummaryInArray(objTransactionSummary, transactionRequest);
		statbean.getTransactionArray()[pointer[0]][pointer[1]]=objTransactionSummary;	
	}
	
	private void checkValidTransactionTime(long timestamp) throws InvalidTimestampException {
		long currentTimeStamp = new Date().getTime();
		long diff = currentTimeStamp - timestamp;	
		if(diff>60000) {
			throw new InvalidTimestampException("Input timestamp is more than 60 seconds old");
		}
	}
	
	private void calculateSummaryInArray(TransactionSummary objTransactionSummary,TransactionRequest transactionRequest) {
		double currentAmount = transactionRequest.getAmount();
		double sum = objTransactionSummary.getSum()+currentAmount;
		objTransactionSummary.setSum(sum);
		int count = objTransactionSummary.getCount()+1;
		objTransactionSummary.setCount(count);
		if(objTransactionSummary.getMin()>currentAmount || objTransactionSummary.getMin()==0) {
			objTransactionSummary.setMin(currentAmount);
		}
		else if(currentAmount>objTransactionSummary.getMax()) {
			objTransactionSummary.setMax(currentAmount);
		}
	}
	
	public TransactionSummary getStatistics() {
		TransactionSummary finalTransactionSummary = new TransactionSummary();
		long currentTime = 1522401727867L;//new Date().getTime();
		long diffFromFirstInsert=currentTime - (StatisticsBean.appStartTime);
		int statisticsStartTime=(int)diffFromFirstInsert;
		int statisticsEndTime=(int)statisticsStartTime-59999;
		if(statisticsEndTime<0) statisticsEndTime=0;
		for(int i=statisticsEndTime;i<=statisticsStartTime;i++) {			
			//find respective array location
			int[] pointer = calculate2dArrayPointer(i);
			TransactionSummary objTrans = statbean.getTransactionArray()[pointer[0]][pointer[1]];
			
			if(objTrans!=null) {				
				//calculateSummaryInArray(finalTransactionSummary, objTrans);
				double currentSum = objTrans.getSum();
				double sum = finalTransactionSummary.getSum()+currentSum;
				finalTransactionSummary.setSum(sum);
				int count = objTrans.getCount()+finalTransactionSummary.getCount();
				finalTransactionSummary.setCount(count);				
				if(objTrans.getMin()<finalTransactionSummary.getMin() || finalTransactionSummary.getMax()==0) {
					finalTransactionSummary.setMin(objTrans.getMin());
				}
				else if(finalTransactionSummary.getMax()<objTrans.getMax() || finalTransactionSummary.getMax()==0) {
					finalTransactionSummary.setMax(objTrans.getMax());
				}
			}
		}
		return finalTransactionSummary;
		
	}
	
	public String print() {
		return statbean.toString();
		
	}
	
	private int[] calculate2dArrayPointer(long diffFromFirstInsert){
		
		int intDiff=(int)diffFromFirstInsert;
		int minute = intDiff/60000;
		if(minute>4) {
			minute=minute%5;
		}
		int millisec=intDiff%60000;
		int[] pointer = {minute,millisec};
		
		return pointer;
	}
	
	
	
	public void loadTestData() throws InvalidTimestampException {
		
		StatisticsBean.appStartTime=1522401617867L;
		TransactionRequest transactionRequest = null;
		transactionRequest = new TransactionRequest(10.0, 1522401717867L);
		logTransaction(transactionRequest);
		
		transactionRequest = new TransactionRequest(10.0, 1522401717867L);
		logTransaction(transactionRequest);
		
		transactionRequest = new TransactionRequest(10.0, 1522401717867L);
		logTransaction(transactionRequest);
		
		transactionRequest = new TransactionRequest(20.0, 1522401717867L);
		logTransaction(transactionRequest);
		
		transactionRequest = new TransactionRequest(10.0, 1522401717867L);
		logTransaction(transactionRequest);
		
		transactionRequest = new TransactionRequest(30.0, 1522401717867L);
		logTransaction(transactionRequest);
		
		transactionRequest = new TransactionRequest(10.0, 1522401717867L);
		logTransaction(transactionRequest);
		
		transactionRequest = new TransactionRequest(5.0, 1522401717867L);
		logTransaction(transactionRequest);
		
		transactionRequest = new TransactionRequest(10.0, 1522401717867L);
		logTransaction(transactionRequest);
		
		transactionRequest = new TransactionRequest(10.0, 1522401717867L);
		logTransaction(transactionRequest);
		
		transactionRequest = new TransactionRequest(10.0, 1522401717867L);
		logTransaction(transactionRequest);
		
		transactionRequest = new TransactionRequest(10.0, 1522401717867L);
		logTransaction(transactionRequest);
		
		for(int i=0;i<10000;i++) {
			transactionRequest = new TransactionRequest(10.0, (1522401717867L-givenUsingPlainJava_whenGeneratingRandomLongBounded_thenCorrect()));
			logTransaction(transactionRequest);
		}
		
		/*for(int i=0;i<1000;i++) {
			transactionRequest = new TransactionRequest(10.0, (1522401717867L-givenUsingPlainJava_whenGeneratingRandomLongBounded_thenCorrect()));
			logTransaction(transactionRequest);
		}*/
		
		//logger.info(getStatistics());
	}
	
	public long givenUsingPlainJava_whenGeneratingRandomLongBounded_thenCorrect() {
	    long leftLimit = 1000L;
	    long rightLimit = 10000L;
	    long generatedLong = leftLimit + (long) (Math.random() * (rightLimit - leftLimit));
	    return generatedLong;
	}
	
}
