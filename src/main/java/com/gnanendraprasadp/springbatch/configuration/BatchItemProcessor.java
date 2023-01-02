package com.gnanendraprasadp.springbatch.configuration;

import com.gnanendraprasadp.springbatch.model.Customer;
import org.springframework.batch.item.ItemProcessor;

public class BatchItemProcessor implements ItemProcessor<Customer, Customer> {
    @Override
    public Customer process(Customer customer) throws Exception {
        if (Integer.parseInt(customer.getAge()) > 10) {
            return customer;
        } else {
            return null;
        }
    }
}
