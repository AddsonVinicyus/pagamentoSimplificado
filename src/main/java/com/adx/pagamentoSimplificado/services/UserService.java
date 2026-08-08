package com.adx.pagamentoSimplificado.services;

import com.adx.pagamentoSimplificado.domain.user.User;
import com.adx.pagamentoSimplificado.domain.user.UserType;
import com.adx.pagamentoSimplificado.dto.UserDTO;
import com.adx.pagamentoSimplificado.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void validateTransaction(User payer, BigDecimal amount) throws RuntimeException{
        if(payer.getUserType() == UserType.MERCHANT)
            throw new RuntimeException("Um usuário do tipo Lojista não pode realizar transações!");

        if(payer.getBalance().compareTo(amount) < 0)
            throw new RuntimeException("Saldo insuficiente");

    }

    public User findUserById(Long id){
        return this.userRepository.findUserById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado.") );
    }

    public void saveUser(User user){
        this.userRepository.save(user);
    }

    public User createUser(UserDTO userDTO) {
        User newUser = new User(userDTO);
        this.saveUser(newUser);

        return newUser;
    }

    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }
}
