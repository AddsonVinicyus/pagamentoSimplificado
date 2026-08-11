package com.adx.pagamentoSimplificado.services;

import com.adx.pagamentoSimplificado.domain.user.User;
import com.adx.pagamentoSimplificado.infra.exceptions.TransactionNotAuthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class AuthorizationService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${authorizationURL}")
    private String authorizationURL;

    public void verifyAuthorization(User payer, BigDecimal value){
        if(!(authorizeTransaction(payer, value)))
            throw new TransactionNotAuthorizedException("A transação não foi autorizada.");
    }

    public boolean authorizeTransaction(User payer, BigDecimal value){
        ResponseEntity<Map> authorizationResponse = restTemplate.getForEntity(authorizationURL, Map.class);
        if(authorizationResponse.getStatusCode() == HttpStatus.OK){
            String message = (String) authorizationResponse.getBody().get("message");
            return "Autorizado".equalsIgnoreCase(message);
        } else return false;
    }

}
