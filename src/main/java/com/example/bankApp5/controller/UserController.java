package com.example.bankApp5.controller;

import com.example.bankApp5.dto.*;
import com.example.bankApp5.service.UserServiceImpl;
import com.example.bankApp5.service.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserServiceImpl userService;

    @PostMapping("/create")
    public ResponseEntity<Response> createAccount(@RequestBody UserRequest userRequest) {

     Response response = userService.createAccount(userRequest);
        System.out.println(response);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
    @PostMapping("/signin")
    public ResponseEntity<Response> login(@RequestBody LoginDto loginDto) {
        System.out.println(loginDto);
      Response response =  userService.login(loginDto);
        System.out.println(response.toString());
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    @GetMapping("/balanceEnquiry")
    public BankResponse balanceEnquiry(@RequestBody EnquiryRequest request) {
        return userService.balanceEnquiry(request);
    }

    @GetMapping("/nameEnquiry")
    public String nameEnquiry(@RequestBody EnquiryRequest request) {
        return userService.nameEnquiry(request);
    }

    @PostMapping("/user/debit")
    public BankResponse debitAccount(@RequestBody CreditDebitRequest request) {
        return userService.debitAccount(request);
    }

    @PostMapping("/user/credit")
    public BankResponse creditAccount(@RequestBody CreditDebitRequest request) {
        return userService.creditAccount(request);

    }
    @PostMapping("/user/transfer")
    public BankResponse transfer(@RequestBody TransferRequest request) {
        return userService.transfer(request);
    }

}
