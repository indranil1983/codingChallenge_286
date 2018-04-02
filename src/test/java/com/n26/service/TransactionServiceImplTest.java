package com.n26.service;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.n26.controller.TransactionControllerAdvice;
import com.n26.model.TransactionRequest;
import com.n26.model.TransactionSummary;


@RunWith(MockitoJUnitRunner.class)
public class TransactionServiceImplTest {


	private static final Logger logger = LogManager.getLogger(TransactionServiceImplTest.class);
	
    private static final Long TIMESTAMP = new Random().nextLong();
    private static final double AMOUNT = new Random().nextDouble();



    private TransactionServiceImpl service;

    @InjectMocks
    private StatisticsBean statbean;

 


    @Before
    public void setUp() throws Exception {
    	StatisticsBean statbean = new StatisticsBean();
    	statbean.init();
    	service = new TransactionServiceImpl();
    	service.setStatbean(statbean);
    }

    @Test
    public void testLogTransaction() throws Exception {
    	long currTs = new Date().getTime();
    	TransactionRequest transactionRequest  = new TransactionRequest(12.3, currTs) ;
        service.logTransaction(transactionRequest);
        assertEquals(statbean.countInsert(),1);
    }
    
    

    @Test
    public void testGetStatistics() throws Exception {
       
    	long currTs = new Date().getTime()-1000;
    	TransactionRequest transactionRequest  = new TransactionRequest(12.3, currTs) ;
        service.logTransaction(transactionRequest);
    	TransactionSummary actual = service.getStatistics();

    	assertEquals(actual.getSum(), 12.3,0.001);

        //verify(statistic).merge(anotherStatistic);
    }
    
    
    @Test
    public void testMultipleLoadAndGet() {	
    	
    	long currTs = new Date().getTime();
    	
		TransactionRequest transactionRequest = null;
		
		try {
			transactionRequest = new TransactionRequest(30.0, (currTs-30000));
			service.logTransaction(transactionRequest);

			transactionRequest = new TransactionRequest(40.0, (currTs-40000)
					);
			service.logTransaction(transactionRequest);

			transactionRequest = new TransactionRequest(50.0, (currTs-50000));
			service.logTransaction(transactionRequest);

			transactionRequest = new TransactionRequest(60.0, (currTs-59900));
			service.logTransaction(transactionRequest);

			transactionRequest = new TransactionRequest(10.0, (currTs-10000));
			service.logTransaction(transactionRequest);

			transactionRequest = new TransactionRequest(20.0, (currTs-20000));
			service.logTransaction(transactionRequest);

			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		transactionRequest = new TransactionRequest(40.0, (currTs-40000));
		service.logTransaction(transactionRequest);
		
		transactionRequest = new TransactionRequest(30.0, (currTs-30000));
		service.logTransaction(transactionRequest);
		
		transactionRequest = new TransactionRequest(40.0, (currTs-40000));
		service.logTransaction(transactionRequest);
		
		transactionRequest = new TransactionRequest(10.0, (currTs-10000));
		service.logTransaction(transactionRequest);
		
		transactionRequest = new TransactionRequest(10.0, (currTs-10000));
		service.logTransaction(transactionRequest);
		
		transactionRequest = new TransactionRequest(20.0, (currTs-20000));
		service.logTransaction(transactionRequest);
		
		/*for(int i=0;i<10000;i++) {
			transactionRequest = new TransactionRequest(10.0, (1522401717867L-givenUsingPlainJava_whenGeneratingRandomLongBounded_thenCorrect()));
			logTransaction(transactionRequest);
		}
		
		for(int i=0;i<1000;i++) {
			transactionRequest = new TransactionRequest(10.0, (1522401717867L-givenUsingPlainJava_whenGeneratingRandomLongBounded_thenCorrect()));
			logTransaction(transactionRequest);
		}
		*/
		
		
		TransactionSummary actual = service.getStatistics();
		logger.info("TransactionSummary ="+actual);
    	assertEquals(actual.getSum(), 360,.001);
    	assertEquals(actual.getCount(), 12);
    	assertEquals(actual.getAvg(), 30,.001);
    	assertEquals(actual.getMin(), 10,.001);
    	assertEquals(actual.getMax(), 60,.001);
	}
    
    /*	
	public long givenUsingPlainJava_whenGeneratingRandomLongBounded_thenCorrect() {
	    long leftLimit = 1000L;
	    long rightLimit = 10000L;
	    long generatedLong = leftLimit + (long) (Math.random() * (rightLimit - leftLimit));
	    return generatedLong;
	}*/
}