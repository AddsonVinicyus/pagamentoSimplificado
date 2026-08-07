package com.adx.pagamentoSimplificado.repositories;

import com.adx.pagamentoSimplificado.domain.transaction.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
