package com.adx.pagamentoSimplificado.dto;

import com.adx.pagamentoSimplificado.domain.user.UserType;

import java.math.BigDecimal;

public record UserDTO(
        String firstName,
        String lastName,
        String document,
        BigDecimal balance,
        UserType userType,
        String email,
        String password
) {
}
