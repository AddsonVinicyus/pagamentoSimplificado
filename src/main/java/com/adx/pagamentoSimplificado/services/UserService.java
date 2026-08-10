package com.adx.pagamentoSimplificado.services;

import com.adx.pagamentoSimplificado.domain.user.User;
import com.adx.pagamentoSimplificado.domain.user.UserType;
import com.adx.pagamentoSimplificado.dto.UserDTO;
import com.adx.pagamentoSimplificado.infra.exceptions.InsufficientAmountException;
import com.adx.pagamentoSimplificado.infra.exceptions.InvalidTransactionException;
import com.adx.pagamentoSimplificado.infra.exceptions.UserAlreadyExistsException;
import com.adx.pagamentoSimplificado.infra.exceptions.UserNotFoundException;
import com.adx.pagamentoSimplificado.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void validateTransaction(User payer, BigDecimal amount) throws RuntimeException{
        if(payer.getUserType() == UserType.MERCHANT)
            throw new InvalidTransactionException("Um usuário do tipo Lojista não pode realizar transações!");

        if(payer.getBalance().compareTo(amount) < 0)
            throw new InsufficientAmountException();

    }

    public User findUserById(Long id){
        return this.userRepository.findUserById(id)
                .orElseThrow(UserNotFoundException::new);
    }

    public void saveUser(User user){
        this.userRepository.save(user);
    }

    public User createUser(UserDTO userDTO) {
        User newUser = new User(userDTO);
        verifyUser(userDTO.document());
        this.saveUser(newUser);

        return newUser;
    }

    public void verifyUser(String document){
        if(userRepository.findUserByDocument(document).isPresent())
            throw new UserAlreadyExistsException();
    }

    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }
}
