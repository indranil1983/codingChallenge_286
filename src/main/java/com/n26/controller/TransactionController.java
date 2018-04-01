package com.n26.controller;

import javax.transaction.InvalidTransactionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.n26.exceptions.InvalidTimestampException;
import com.n26.model.TransactionRequest;
import com.n26.model.TransactionSummary;
import com.n26.service.TransactionService;

@RestController
public class TransactionController {
	
	@Autowired
	private TransactionService transactionService;
	
	@RequestMapping(value = "/transaction",
            method = RequestMethod.POST)
	public void logTransaction(@RequestBody TransactionRequest transactionRequest ) throws InvalidTimestampException{		
		transactionService.logTransaction(transactionRequest);
	}
	
	@RequestMapping(value = "/statistics",
            method = RequestMethod.GET)
	public TransactionSummary getStatistics(){		
		return transactionService.getStatistics();
	}
	
	
	@RequestMapping(value = "/print",
            method = RequestMethod.GET)
	public String print(){		
		return transactionService.print();
	}
	
	@RequestMapping(value = "test",
            method = RequestMethod.GET)
	public void test()  throws InvalidTransactionException,Exception
	{		
		transactionService.loadTestData();
	}

}
