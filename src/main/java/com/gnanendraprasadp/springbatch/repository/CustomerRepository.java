package com.gnanendraprasadp.springbatch.repository;

import com.gnanendraprasadp.springbatch.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Integer> {
}
