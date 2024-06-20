package com.example.bankApp5.service.utils;

import com.example.bankApp5.Repository.TransactionRepository;
import com.example.bankApp5.dto.TransactionDto;
import com.example.bankApp5.entity.Transaction;
import com.example.bankApp5.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    TransactionRepository transactionRepository;
@Override
public void saveTransaction(TransactionDto transactionDto) {
        Transaction transaction = Transaction.builder()
                    .transactionType(transactionDto.getTransactionType())
                    .accountNumber(transactionDto.getAccountNumber())
                    .amount(transactionDto.getAmount())
                    .status("SUCCESS")
                    .build();
            transactionRepository.save(transaction);
            System.out.println("Transaction saved Successfully");

        }
    }

