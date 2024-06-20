package com.example.bankApp5.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AccountInfo {

        private String accountNumber;
        private String accountName;
        private BigDecimal accountBalance;

    }
