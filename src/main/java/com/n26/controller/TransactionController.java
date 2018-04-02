package com.n26.controller;

import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.n26.exceptions.InvalidTimestampException;
import com.n26.model.TransactionRequest;
import com.n26.model.TransactionSummary;
import com.n26.service.TransactionServiceImpl;


@RestController
public class TransactionController {
	
	@Autowired
	private TransactionServiceImpl transactionService;
	
	@RequestMapping(value = "/transactions",
            method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping(consumes = "application/json")
	public void logTransaction(@Valid @RequestBody TransactionRequest transactionRequest ) throws InvalidTimestampException{		
		
		checkValidTransactionTime(transactionRequest);
		transactionService.logTransaction(transactionRequest);
	}
	
	@RequestMapping(value = "/statistics",
            method = RequestMethod.GET)
	@GetMapping(produces = "application/json")
	public TransactionSummary getStatistics(){		
		return transactionService.getStatistics();
	}
	
	private void checkValidTransactionTime(TransactionRequest transactionRequest) throws InvalidTimestampException {
		long timestamp = transactionRequest.getTimestamp();
		long currentTimestamp = new Date().getTime();
		long diff = currentTimestamp - timestamp;	
		if(diff>60000) {
			throw new InvalidTimestampException("Input timestamp is more than 60 seconds old");
		}
	}
	
	
	/*@RequestMapping(value = "/print",
            method = RequestMethod.GET)
	public String print(){		
		return transactionService.print();
	}*/
	
	/*@RequestMapping(value = "test",
            method = RequestMethod.GET)
	public void test()  throws InvalidTransactionException,Exception
	{		
		transactionService.loadTestData();
	}*/

}
