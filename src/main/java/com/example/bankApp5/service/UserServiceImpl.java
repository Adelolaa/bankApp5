package com.example.bankApp5.service;

import com.example.bankApp5.Repository.RoleRepository;
import com.example.bankApp5.Repository.UserRepository;
import com.example.bankApp5.dto.*;
import com.example.bankApp5.entity.Role;
import com.example.bankApp5.service.utils.AccountUtils;
import com.example.bankApp5.entity.User;
import com.example.bankApp5.service.utils.Response;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserServiceImpl implements UserService {

    Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final TransactionService transactionService;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;


    public UserServiceImpl(UserRepository userRepository, TransactionService transactionService, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.transactionService = transactionService;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @Autowired
    AuthenticationManager authenticationManager;


    @Override
    public Response createAccount(UserRequest userRequest) {
        Optional<User> optionalUser = userRepository.findByEmail(userRequest.getEmail());
        System.out.println();
        if(optionalUser.isPresent()) {
            System.out.println(optionalUser.get().getFirstName());

            return new Response("user already exit",400,null);
        }
        User newUser = User.builder()
                .firstName(userRequest.getFirstName())
                .otherName(userRequest.getOtherName())
                .lastName(userRequest.getLastName())
                .gender(userRequest.getGender())
                .address(userRequest.getAddress())
                .stateOfOrigin(userRequest.getStateOfOrigin())
                .accountNumber(AccountUtils.generateAccountNumber())
                .email(userRequest.getEmail())
                .accountBalance(BigDecimal.ZERO)
                .phoneNumber(userRequest.getPhoneNumber())
                .password(userRequest.getPassword())//passwordEncoder.encode(userRequest.getPassword()))
                .alternativePhoneNumber(userRequest.getAlternativePhoneNumber())
                .status("ACTIVE")
                .build();
        Optional<Role> optionalRole = roleRepository.findByRoleName("ROLE_USER");
        if(optionalRole.isEmpty())   {
            System.out.println("called......");
            Role role = new Role();
            role.setRoleName("ROLE_USER");
             newUser.setRoles(Collections.singleton(role));
        }else{
            System.out.println("called......1");
            newUser.setRoles(Collections.singleton(optionalRole.get()));
        }
        System.out.println("out.....");
        User savedUser = userRepository.save(newUser);
        return new Response("successful",201, savedUser);
    }


  @Override
    public BankResponse balanceEnquiry(EnquiryRequest request) {
        logger.info("Enquiry request for account number: {}", request.getAccountNumber());

        // Check if the provided account number exists in the db
        boolean isAccountExists = userRepository.existsByAccountNumber(request.getAccountNumber());
        if (!isAccountExists) {
            logger.warn("Account number: {} does not exist", request.getAccountNumber());
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_DOES_NOT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_DOES_NOT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        User foundUser = userRepository.findByAccountNumber(request.getAccountNumber());
        if (foundUser == null) {
            logger.error("Account number: {} exists but user not found", request.getAccountNumber());
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_DOES_NOT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_DOES_NOT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        logger.info("User found for account number: {}", request.getAccountNumber());
        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_FOUND_CODE)
                .responseMessage(AccountUtils.ACCOUNT_FOUND_SUCCESS)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(foundUser.getAccountBalance())
                        .accountNumber(request.getAccountNumber())
                        .accountName(foundUser.getFirstName() + " " + foundUser.getOtherName() + " " + foundUser.getLastName())
                        .build())
                .build();
    }

    @Override
    public String nameEnquiry(EnquiryRequest request) {
        boolean isAccountExists = userRepository.existsByAccountNumber(request.getAccountNumber());
        if (!isAccountExists) {
            return AccountUtils.ACCOUNT_DOES_NOT_EXISTS_MESSAGE;
        }
        User foundUser = userRepository.findByAccountNumber(request.getAccountNumber());
        return foundUser.getFirstName() + " " + foundUser.getOtherName() + " " + foundUser.getLastName();
    }
    @Override
    public BankResponse creditAccount(CreditDebitRequest request) {
//        check if the account exist
        boolean isAccountExits = userRepository.existsByAccountNumber(request.getAccountNumber());
        if (!isAccountExits) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_DOES_NOT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_DOES_NOT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        User userToCredit = userRepository.findByAccountNumber(request.getAccountNumber());
        userToCredit.setAccountBalance(userToCredit.getAccountBalance().add(request.getAmount()));
        userRepository.save(userToCredit);

        //Save Transaction
        TransactionDto transactionDto =TransactionDto.builder()
                .accountNumber(userToCredit.getAccountNumber())
                .transactionType("CREDIT")
                .amount(request.getAmount() )
                .build();
        transactionService.saveTransaction(transactionDto);



        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREDITED_SUCCESS)
                .responseMessage(AccountUtils.ACCOUNT_CREDITED_SUCCESS_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountName(userToCredit.getFirstName() + " " + userToCredit.getOtherName() + " " + userToCredit.getLastName())
                        .accountBalance(userToCredit.getAccountBalance())
                        .accountNumber(userToCredit.getAccountNumber())
                        .build())
                .build();

    }



    @Override
    public BankResponse debitAccount(CreditDebitRequest request) {
        boolean isAccountExists = userRepository.existsByAccountNumber(request.getAccountNumber());
        if (!isAccountExists) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_FOUND_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_FOUND_SUCCESS)
                    .accountInfo(null)
                    .build();
        }
        User userToDebit = userRepository.findByAccountNumber(request.getAccountNumber());
        BigInteger availableBalance = userToDebit.getAccountBalance().toBigInteger();
        BigInteger debitAmount = request.getAmount().toBigInteger();
        if (availableBalance.intValue() < debitAmount.intValue()) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
                    .responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
                    .accountInfo(null)
                    .build();
        } else {
            userToDebit.setAccountBalance(userToDebit.getAccountBalance().subtract(request.getAmount()));
            userRepository.save(userToDebit);


            TransactionDto transactionDto = TransactionDto.builder()
                    .accountNumber(userToDebit.getAccountNumber())
                    .transactionType("DEBIT")
                    .amount(request.getAmount())
                    .build();
            transactionService.saveTransaction(transactionDto);

            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_DEBITED_SUCCESS)
                    .responseMessage(AccountUtils.ACCOUNT_DEBITED_SUCCESS_MESSAGE)
                    .accountInfo(AccountInfo.builder()
                            .accountNumber(request.getAccountNumber())
                            .accountName(userToDebit.getFirstName() + " " + userToDebit.getOtherName() + " " + userToDebit.getLastName())
                            .accountBalance(userToDebit.getAccountBalance())
                            .build())
                    .build();

        }
    }
        @Override
        public BankResponse transfer(TransferRequest request) {

            boolean isDestinationAccountExist = userRepository.existsByAccountNumber(request.getDestinationAccountNumber());
            if (!isDestinationAccountExist) {
                return BankResponse.builder()
                        .responseCode(AccountUtils.ACCOUNT_DOES_NOT_EXISTS_CODE)
                        .responseMessage(AccountUtils.ACCOUNT_DOES_NOT_EXISTS_MESSAGE)
                        .accountInfo(null)
                        .build();
            }
            User sourceAccount = userRepository.findByAccountNumber(request.getSourceAccountNumber());
            if (request.getAmount().compareTo(sourceAccount.getAccountBalance()) > 0){
                return BankResponse.builder()
                        .responseCode(AccountUtils.ACCOUNT_FOUND_CODE)
                        .responseMessage(AccountUtils.ACCOUNT_FOUND_SUCCESS)
                        .accountInfo(null)
                        .build();
            }
            sourceAccount.setAccountBalance(sourceAccount.getAccountBalance().subtract(request.getAmount()));
            String sourceUsername = sourceAccount.getFirstName()+" "+sourceAccount.getOtherName()+" "+sourceAccount.getLastName();
            userRepository.save(sourceAccount);



            User destionationAccountUser = userRepository.findByAccountNumber(request.getDestinationAccountNumber());
            destionationAccountUser.setAccountBalance(destionationAccountUser.getAccountBalance().add(request.getAmount()));
//        String recipientUserName =destinationAccountUser.getFirstName() + " "+destinationAccountUser.getOtherName() + " "+ destinationAccountUser.getLastName();
            userRepository.save(destionationAccountUser);



            return BankResponse.builder()
                    .responseCode(AccountUtils.TRANSFER_SUCCESSFUL_CODE)
                    .responseMessage(AccountUtils.TRANSFER_SUCCESSFUL_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

    @Override
    public Response login(LoginDto loginDto) {
        Optional<User> optionalUser = userRepository.findByEmail(loginDto.getUsernameOrEmail());
        if(optionalUser.isEmpty()) return  new Response("User not found",400,null);
        else
         return  new Response("Successfull",200,null);

    };


    }






