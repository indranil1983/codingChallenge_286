package com.n26.service;

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.annotations.VisibleForTesting;
import com.n26.exceptions.InvalidTimestampException;
import com.n26.model.TransactionRequest;
import com.n26.model.TransactionSummary;

@Service
public class TransactionServiceImpl implements ITransactionService
{
	private static final Logger logger = LogManager.getLogger(TransactionServiceImpl.class);
	
	@Autowired
	StatisticsBean statbean;


	@Override
	public void logTransaction(TransactionRequest transactionRequest) throws InvalidTimestampException 
	{
		//logger.info("Logging transaction "+transactionRequest);
		long currentTimeStamp = fetchCurrentTimeStamp();
		//checkValidTransactionTime(transactionRequest.getTimestamp(),currentTimeStamp);
		insertIntoArray(transactionRequest,currentTimeStamp);
	}
	
	@Override
	public TransactionSummary getStatistics() {
		TransactionSummary finalTransactionSummary = new TransactionSummary();
		long currentTime = fetchCurrentTimeStamp();
		int statisticsStartTime=calculateDifferenceInTimeFromAppStartTime(currentTime);
		int statisticsEndTime=(int)statisticsStartTime-59999;
		if(statisticsEndTime<0) statisticsEndTime=0;
		
		int finalCount=0;
		double finalSum=0,finalMin=-1,finalMax=-1;
		for(int i=statisticsEndTime;i<=statisticsStartTime;i++) {			
			//find respective array location
			int[] pointer = calculate2dArrayPointer(i);
			
			//now retrieve the summary obj located at that pointer.
			TransactionSummary objTrans = statbean.getTransactionArray()[pointer[0]][pointer[1]];
			
			if(objTrans!=null)
			{				
				//calculateSummaryInArray(finalTransactionSummary, objTrans);
				long highestTsInSummary  = objTrans.getHighestTimeStamp();
				if(((currentTime-highestTsInSummary)<=60000)) //in case the difference is greater than 60000, then the summary is older than a minute so discard
				{ 
					finalSum += objTrans.getSum();
					finalCount += objTrans.getCount();				
					if(finalMin==-1 || objTrans.getMin()<finalMin) 
					{
						finalMin=objTrans.getMin();
					}
					if(finalMax==-1 || finalMax<objTrans.getMax()) 
					{
						finalMax=objTrans.getMax();
					}
				}				
			}
		}
		finalTransactionSummary.setCount(finalCount);
		finalTransactionSummary.setMax(finalMax);
		finalTransactionSummary.setMin(finalMin);
		finalTransactionSummary.setSum(finalSum);
		return finalTransactionSummary;		
	}


	private void insertIntoArray(TransactionRequest transactionRequest,long currentTimeStamp) throws InvalidTimestampException {
		
		
		//insert and calculate summary for each timestamp block
		
		//Step 1: App start time is a base from which we will determine exactly which array element it belongs to
		//So difference from AppStartTime is calculated.
		int diffFromFirstInsert = calculateDifferenceInTimeFromAppStartTime(transactionRequest.getTimestamp());		
		
		//find respective array location
		int[] pointer = calculate2dArrayPointer(diffFromFirstInsert);
		
		//now retrieve the summary obj located at that pointer.
		TransactionSummary objTransactionSummary = statbean.getTransactionArray()[pointer[0]][pointer[1]];
		
		
		//update the summary with highest timestamp value so later we can understand which timestamp was that array element allocated to.
		objTransactionSummary = updateHighestTsValueInTransactionSummary(transactionRequest.getTimestamp(),objTransactionSummary,currentTimeStamp);		
		
		calculateSummaryInArray(objTransactionSummary, transactionRequest);
		statbean.getTransactionArray()[pointer[0]][pointer[1]]=objTransactionSummary;	
	}
	
	
	private TransactionSummary updateHighestTsValueInTransactionSummary(long timeStamp,TransactionSummary objTransactionSummary,long currentTimeStamp) {
		
		if(objTransactionSummary==null) {
			objTransactionSummary = new TransactionSummary();
			objTransactionSummary.setHighestTimeStamp(timeStamp);
		}
		else 
		{
			long currentHighestTsInSummary = objTransactionSummary.getHighestTimeStamp();
			if((currentTimeStamp-currentHighestTsInSummary)>60000){
				objTransactionSummary.resetAll();
				objTransactionSummary.setHighestTimeStamp(timeStamp);
			}
			else if(timeStamp>currentHighestTsInSummary) {
				objTransactionSummary.setHighestTimeStamp(timeStamp);
			}	
		}
		
		return objTransactionSummary;
			
	}


	private int calculateDifferenceInTimeFromAppStartTime(long timeStamp) {
		long diffFromFirstInsert = timeStamp - (StatisticsBean.appStartTime);
		return (int)diffFromFirstInsert;
	}
	
	
	/**
	 * This will calculate the statistics summary for each array element
	 * @param objTransactionSummary
	 * @param transactionRequest
	 */
	private void calculateSummaryInArray(TransactionSummary objTransactionSummary,TransactionRequest transactionRequest) {
		double currentAmount = transactionRequest.getAmount();
		
		double sum = objTransactionSummary.getSum()+currentAmount;
		objTransactionSummary.setSum(sum);		
		
		objTransactionSummary.setCount(objTransactionSummary.getCount()+1);
		
		if(objTransactionSummary.getMin()>currentAmount || objTransactionSummary.getMin()==-1) 
		{
			objTransactionSummary.setMin(currentAmount);
		}
				
		if(objTransactionSummary.getMax()==-1 || objTransactionSummary.getMax()<currentAmount) 
		{
			objTransactionSummary.setMax(currentAmount);
		}
	}
	
	
	
	
	public String print() {
		return statbean.toString();
		
	}
	
	private long fetchCurrentTimeStamp() {
		return new Date().getTime();
		//return 1522581918956L;

	}
	
	
	
	private int[] calculate2dArrayPointer(int diffFromAppStartTime){
		
		//int intDiff=(int)diffFromFirstInsert;
		int minute = diffFromAppStartTime/60000;
		if(minute>4) {
			//this will move the pointer count start from 0 as we are mapping the timestamp in 5 blocks of array.
			minute=minute%5;  //so if minute calculation is 12 then it will point to array number 2.
		}
		int millisec=diffFromAppStartTime%60000; // so here we are converting any timestamp to a block of 2d array with height 5 and width 60k
		int[] pointer = {minute,millisec};
		
		return pointer;
	}
	
	
	
	
	@VisibleForTesting
	public StatisticsBean getStatbean() {
		return statbean;
	}

	@VisibleForTesting
	public void setStatbean(StatisticsBean statbean) {
		this.statbean = statbean;
	}
	
	
	
	
	
}
