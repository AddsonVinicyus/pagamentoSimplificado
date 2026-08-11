package com.adx.pagamentoSimplificado.services;

import com.adx.pagamentoSimplificado.domain.transaction.Transaction;
import com.adx.pagamentoSimplificado.domain.user.User;
import com.adx.pagamentoSimplificado.dto.TransactionDTO;
import com.adx.pagamentoSimplificado.infra.exceptions.NotificationException;
import com.adx.pagamentoSimplificado.infra.exceptions.TransactionNotAuthorizedException;
import com.adx.pagamentoSimplificado.repositories.TransactionRepository;
import jakarta.transaction.Transactional;
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
    @Autowired
    private NotificationService notificationService;

    @Value("${authorizationURL}")
    private String authorizationURL;

    public Transaction createTransaction(TransactionDTO transaction){
        User payer = this.userService.findUserById(transaction.payerId());
        User payee = this.userService.findUserById(transaction.payeeId());

        userService.validateTransaction(payer, transaction.value());

        verifyAuthorization(payer, transaction.value());

        Transaction newTransaction = executePayment(payer, payee, transaction.value());

        try{
            sendNotification(payer, payee);
        } catch (Exception e){
            throw new NotificationException("Erro ao enviar notificação");
        }

        return newTransaction;

    }

    @Transactional
    public Transaction executePayment(User payer, User payee, BigDecimal value){
        Transaction newTransaction = new Transaction();
        newTransaction.setAmount(value);
        newTransaction.setPayer(payer);
        newTransaction.setPayee(payee);
        newTransaction.setTimestamp(LocalDateTime.now());

        payer.setBalance(payer.getBalance().subtract(value));
        payee.setBalance(payee.getBalance().add(value));

        this.transactionRepository.save(newTransaction);
        this.userService.saveUser(payer);
        this.userService.saveUser(payee);

        return newTransaction;

    }

    public void verifyAuthorization(User payer, BigDecimal value){
        if(!(authorizeTransaction(payer, value)))
            throw new TransactionNotAuthorizedException("A transação não foi autorizada.");
    }

    public void sendNotification(User payer, User payee){
        this.notificationService.sendNotification(payer, "Transação enviada com sucesso");
        this.notificationService.sendNotification(payee, "Transação recebida com sucesso");
    }

    public boolean authorizeTransaction(User payer, BigDecimal value){
        ResponseEntity<Map> authorizationResponse = restTemplate.getForEntity(authorizationURL, Map.class);
        if(authorizationResponse.getStatusCode() == HttpStatus.OK){
            String message = (String) authorizationResponse.getBody().get("message");
            return "Autorizado".equalsIgnoreCase(message);
        } else return false;
    }

}
