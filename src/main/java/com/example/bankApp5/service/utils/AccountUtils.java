package com.example.bankApp5.service.utils;

import java.time.Year;

public class AccountUtils {

        public static final String ACCOUNT_EXISTS_CODE = "001";
        public static final String ACCOUNT_EXISTS_MESSAGE = "User Account already exists!";
        public static final String ACCOUNT_CREATION_SUCCESS = "002";
        public static final String ACCOUNT_CREATION_MESSAGE = "Account successfully created";
        public static final String ACCOUNT_DOES_NOT_EXISTS_CODE = "003";
        public static final String ACCOUNT_DOES_NOT_EXISTS_MESSAGE = "User with the provided account number does not exists";
        public static final String ACCOUNT_FOUND_CODE = "004";
        public static final String ACCOUNT_FOUND_SUCCESS = "User Account Found";
        public static final String ACCOUNT_CREDITED_SUCCESS = "005";
        public static final String ACCOUNT_CREDITED_SUCCESS_MESSAGE = "User Account has been Credited";
        public static final String INSUFFICIENT_BALANCE_CODE = "006";
        public static final String INSUFFICIENT_BALANCE_MESSAGE = "Insufficient Balance";
        public static final String ACCOUNT_DEBITED_SUCCESS = "007";
        public static final String ACCOUNT_DEBITED_SUCCESS_MESSAGE = "User Account has been Successfully    Debited";
        public static final String TRANSFER_SUCCESSFUL_CODE = "008";
        public static final String TRANSFER_SUCCESSFUL_MESSAGE = "Transfer successful";
        public static String generateAccountNumber() {
            /**
             * I want the account numbers to contain the current year and any random numbers
             * that is, 2023 + randomSixDigit so, I need to concatenate the
             * current year with any six digits
             */
            Year currentYear = Year.now();
            int min = 10000009;
            int max = 99999999;
//    generate a random number between min and max
//    to generate a random number

            int randNumber = (int) Math.floor(Math.random() * (max - min + 1) + min);
//    convert the current year & random numbers to strings, then concatenate together
            String year= String.valueOf(currentYear);
            String randomNumber = String.valueOf(randNumber);
            StringBuilder accountNumber = new StringBuilder();

            return accountNumber.append(year).append(randNumber).toString();







        }


    }


