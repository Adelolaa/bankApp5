package com.example.bankApp5.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "transactions")
public class Transaction {
        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        private String transactionId;
        private String transactionType;
        private BigDecimal amount;
        private String accountNumber;
        private String status;
}
