package com.maybank.assessment.repository;

import com.maybank.assessment.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by PRATHEEP on 7/7/2023.
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Page<Transaction> findByCustomerId(int customerId, Pageable pageable);

    Page<Transaction> findByAccountNumberIn(List<String> accountNumbers, Pageable pageable);

    Page<Transaction> findByDescriptionContaining(String description, Pageable pageable);

    // Other methods...
}