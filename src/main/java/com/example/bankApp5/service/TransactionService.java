package com.example.bankApp5.service;

import com.example.bankApp5.dto.TransactionDto;
import org.springframework.stereotype.Service;


public interface TransactionService {
    void saveTransaction(TransactionDto transactionDto);
}
