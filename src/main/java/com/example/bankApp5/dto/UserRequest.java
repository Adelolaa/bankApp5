package com.example.bankApp5.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequest {

        private String firstName;
        private String lastName;
        private String otherName;
        private String gender;
        private String address;
        private  String stateOfOrigin;
        private BigDecimal accountBalance;
        private String email;
        private String phoneNumber;
        private String alternativePhoneNumber;
        private String password;
    }


