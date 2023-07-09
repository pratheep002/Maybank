package com.maybank.assessment.controller;

import com.maybank.assessment.entity.Transaction;
import com.maybank.assessment.model.UpdateTransactionDescriptionRequest;
import com.maybank.assessment.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by PRATHEEP on 7/7/2023.
 */
@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping
    public ResponseEntity<Page<Transaction>> getAllTransactions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Transaction> transactions = transactionService.getAllTransactions(pageable);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Transaction>> searchTransactions(
            @RequestParam(defaultValue = "0") int customerId,
            @RequestParam List<String> accountNumbers,
            @RequestParam String description,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Transaction> transactions = transactionService.searchTransactions(customerId, accountNumbers, description, pageable);
        return ResponseEntity.ok(transactions);
    }

    @PutMapping("/description/{id}")
    public ResponseEntity<Transaction> updateTransactionDescriptionNew(
            @PathVariable("id") Long transactionId,
            @RequestBody UpdateTransactionDescriptionRequest request) {
        System.out.println("updateTransactionDescriptionNew called:id:"+transactionId+"desc:"+request.getDescription());
        Transaction updatedTransaction = transactionService.updateTransactionDescription(transactionId, request.getDescription());
        return ResponseEntity.ok(updatedTransaction);
    }



}
