package com.n26.controller;


import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.n26.model.TransactionSummary;
import com.n26.service.TransactionServiceImpl;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@RunWith(SpringRunner.class)
@WebMvcTest(TransactionController.class)
public class TransactionControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TransactionServiceImpl transactionService;


    @Before
    public void setUp() throws Exception {
        
    }

    @Test
    public void shouldAcceptValidRequest() throws Exception {
        long currTimestamp = new Date().getTime();
    	mvc.perform(post("/transactions")
                .contentType("application/json")
                .content("{\"amount\": 15.1,\"timestamp\":"+String.valueOf(currTimestamp) +"}"))
                .andExpect(status().isCreated())
                .andExpect(content().bytes(new byte[0]));
    }

    @Test
    public void shouldValidateRequest() throws Exception {
        mvc.perform(post("/transactions")
                .contentType("application/json")
                .content("{\"timestamp\": 120}"))
                .andExpect(status().isNoContent())
                .andExpect(content().bytes(new byte[0]));
    }

    @Test
    public void shouldHandleInvalidTimestampException() throws Exception {
     
        mvc.perform(post("/transactions")
                .contentType("application/json")
                .content("{\"amount\": 12.3,\"timestamp\": 1522581848956}"))
                .andExpect(status().isNoContent())
                .andExpect(content().bytes(new byte[0]));
    }
    
    @Test
    public void shouldReturnSampleStatistics() throws Exception {
    	TransactionSummary trans  = new TransactionSummary();
    	trans.setCount(9);
    	trans.setMax(60);
    	trans.setMin(10);
    	trans.setSum(54);
        when(transactionService.getStatistics()).thenReturn(trans);

        mvc.perform(get("/statistics").accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("count", is(9)))
                .andExpect(jsonPath("sum", is(54.0)))
                .andExpect(jsonPath("avg", is(6.0)))
                .andExpect(jsonPath("max", is(60.0)))
                .andExpect(jsonPath("min", is(10.0)));
    }

    
    
}