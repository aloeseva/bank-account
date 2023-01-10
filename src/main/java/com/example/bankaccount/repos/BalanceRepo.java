package com.example.bankaccount.repos;

import com.example.bankaccount.entity.Balance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BalanceRepo extends JpaRepository<Balance, Long> {

}
