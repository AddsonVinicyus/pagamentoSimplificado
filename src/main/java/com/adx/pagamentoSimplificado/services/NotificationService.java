package com.adx.pagamentoSimplificado.services;

import com.adx.pagamentoSimplificado.domain.user.User;
import com.adx.pagamentoSimplificado.dto.NotificationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NotificationService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${notificationURL}")
    private String notificationURL;

    public void sendNotification(User user, String message){
        String email = user.getEmail();
        NotificationDTO notificationRequest = new NotificationDTO(email, message);

//        ResponseEntity<String> notificationResponse = restTemplate.postForEntity(notificationURL,
//                notificationRequest, String.class);
//
//        if(!(notificationResponse.getStatusCode() == HttpStatus.OK))
//            throw new RuntimeException("Serviço de notificação indisponível");

        System.out.println("Notificação enviada!");

    }

}
