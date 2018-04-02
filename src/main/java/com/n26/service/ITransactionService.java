package com.n26.service;

import com.n26.exceptions.InvalidTimestampException;
import com.n26.model.TransactionRequest;
import com.n26.model.TransactionSummary;

interface ITransactionService {

	public void logTransaction(TransactionRequest transactionRequest) throws InvalidTimestampException;
	
	public TransactionSummary getStatistics() throws InvalidTimestampException;
	
}
