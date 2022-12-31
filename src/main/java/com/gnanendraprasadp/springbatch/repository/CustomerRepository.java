package com.gnanendraprasadp.springbatch.repository;

import com.gnanendraprasadp.springbatch.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer,Integer> {
}
