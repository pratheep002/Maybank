package com.maybank.assessment.repository;

import com.maybank.assessment.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Customer,Integer> {
    Customer findByUserName(String username);
}
