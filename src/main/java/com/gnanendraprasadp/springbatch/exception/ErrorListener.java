package com.gnanendraprasadp.springbatch.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gnanendraprasadp.springbatch.model.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.SkipListener;

public class ErrorListener implements SkipListener<Customer, Number> {

    private static final Logger logger = LoggerFactory.getLogger(ErrorListener.class);

    @Override
    public void onSkipInRead(Throwable throwable) {
        logger.info("A failure on read {}", throwable.getMessage());
    }

    @Override
    public void onSkipInWrite(Number number, Throwable throwable) {
        logger.info("A failure on write {} , {}", throwable.getMessage(), number);
    }

    @Override
    public void onSkipInProcess(Customer customer, Throwable throwable) {
        try {
            logger.info("Item {} was skipped due to exception {}", new ObjectMapper().writeValueAsString(customer),throwable.getMessage());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
