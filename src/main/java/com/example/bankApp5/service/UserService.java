package com.example.bankApp5.service;

import com.example.bankApp5.dto.*;
import com.example.bankApp5.service.utils.Response;

public interface UserService {
    Response createAccount(UserRequest userRequest);

    BankResponse creditAccount(CreditDebitRequest request);

    BankResponse balanceEnquiry(EnquiryRequest request);

    String nameEnquiry(EnquiryRequest request);

    BankResponse debitAccount(CreditDebitRequest request);
    BankResponse transfer(TransferRequest request);

    Response login(LoginDto loginDto);
}
