package com.adx.pagamentoSimplificado.services;

import com.adx.pagamentoSimplificado.domain.transaction.Transaction;
import com.adx.pagamentoSimplificado.domain.user.User;
import com.adx.pagamentoSimplificado.dto.TransactionDTO;
import com.adx.pagamentoSimplificado.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class TransactionService {

    @Autowired
    private UserService userService;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private RestTemplate restTemplate;

    @Value("authorizationURL")
    private String authorizationURL;

    public void createTransaction(TransactionDTO transaction){
        User payer = this.userService.findUserById(transaction.payerId());
        User payee = this.userService.findUserById(transaction.payeeId());

        userService.validateTransaction(payer, transaction.value());

        boolean isAuthorized = this.authorizeTransaction(payer, transaction.value());

        if(!isAuthorized){
            throw new RuntimeException("Transação não autorizada!");
        }

        Transaction newTransaction = new Transaction();
        newTransaction.setAmount(transaction.value());
        newTransaction.setPayer(payer);
        newTransaction.setPayee(payee);
        newTransaction.setTimestamp(LocalDateTime.now());

        payer.setBalance(payer.getBalance().subtract(transaction.value()));
        payee.setBalance(payee.getBalance().add(transaction.value()));

        this.transactionRepository.save(newTransaction);
        this.userService.saveUser(payer);
        this.userService.saveUser(payee);

    }

    public boolean authorizeTransaction(User payer, BigDecimal value){
        ResponseEntity<Map> authorizationResponse = restTemplate.getForEntity(authorizationURL, Map.class);
        return authorizationResponse.getStatusCode() == HttpStatus.OK && authorizationResponse.getBody().get("message") == "Autorizado";

    }

}
