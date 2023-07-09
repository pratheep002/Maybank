package com.maybank.assessment.service;

import com.maybank.assessment.entity.Transaction;
import com.maybank.assessment.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by PRATHEEP on 7/7/2023.
 */
@Service
public class TransactionService {
    private static final String FILE_PATH = "classpath:transactions.txt";
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }


    @Transactional
    public void processFileAndStoreTransactions() {
        System.out.println("inside processFileAndStoreTransactions");
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("transactions.txt");
        System.out.println(" inputStream"+inputStream);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            boolean isFirstLine = true;
            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Skip the first line
                }

                String[] data = line.split("\\|");

                Transaction transaction = new Transaction();
                transaction.setAccountNumber(data[0]);
                transaction.setTrxAmount(new Double(data[1]));
                transaction.setDescription(data[2]);
                transaction.setTrxDate(data[3]);
                transaction.setTrxTime(data[4]);
                transaction.setCustomerId(Integer.parseInt(data[5]));

                transactionRepository.save(transaction);
            }

            System.out.println("Data has been successfully inserted into the database.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Transactional
    public Transaction updateTransactionDescription(Long transactionId, String description) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new EntityNotFoundException("Transaction not found"));

        transaction.setDescription(description);
        return transactionRepository.save(transaction);
    }




    public Page<Transaction> searchTransactions(int customerId, List<String> accountNumbers, String description, Pageable pageable) {
        if (customerId != 0) {
            return transactionRepository.findByCustomerId(customerId, pageable);
        } else if (!accountNumbers.isEmpty()) {
            return transactionRepository.findByAccountNumberIn(accountNumbers, pageable);
        } else if (description != null && !description.isEmpty()) {
            return transactionRepository.findByDescriptionContaining(description, pageable);
        } else {
            return transactionRepository.findAll(pageable);
        }
    }

    public Page<Transaction> getAllTransactions(Pageable pageable) {
        return transactionRepository.findAll(pageable);
    }

}
