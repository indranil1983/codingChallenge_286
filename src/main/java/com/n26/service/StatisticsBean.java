package com.n26.service;



import java.util.Date;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.google.common.annotations.VisibleForTesting;
import com.n26.model.TransactionSummary;


@Component
public class StatisticsBean {

	Logger logger = LogManager.getLogger(TransactionServiceImpl.class);
	
	
	
	private static TransactionSummary[][] arrTransactionSummary;
	public static long appStartTime;
	public static long[] highestValueInEachArray;
	
	@PostConstruct
	public void init() {
		arrTransactionSummary= new TransactionSummary[5][60000];
		appStartTime=new Date().getTime()-60000; //to ensure the input timestamp is always greater than appStartTime
		logger.info("length of array is "+arrTransactionSummary.length);
	}
	
	public TransactionSummary[][] getTransactionArray(){
		return arrTransactionSummary;
	}
	
	@VisibleForTesting
	public int countInsert() {
		int count=0;
		int arrTransactionSummaryLength = arrTransactionSummary.length;
		for (int row = 0; row < arrTransactionSummaryLength; row++) { 
			int arrTransactionSummary2dLength = arrTransactionSummary[row].length;
			for (int col = 0; col < arrTransactionSummary2dLength; col++) 
			{ 
				TransactionSummary objTrans = arrTransactionSummary[row][col];
				if(objTrans!=null) {
					count++;
				}
			} 
		}
		return count;
	}

	@Override
	public String toString() {
		StringBuilder strBld = new StringBuilder();
		for (int row = 0; row < arrTransactionSummary.length; row++) { 
			for (int col = 0; col < arrTransactionSummary[row].length; col++) 
				{ 
				TransactionSummary objTrans = arrTransactionSummary[row][col];
					if(objTrans!=null) {
						strBld.append("SUM = "+objTrans.getSum()+"  count= "+objTrans.getCount());
						strBld.append("/n");
					}
				} 
			}

		return strBld.toString();
	}
	
	
}
