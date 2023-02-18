package com.example.bankaccount.service.serviceImplementation;

import com.example.bankaccount.entity.Balance;
import com.example.bankaccount.repos.BalanceRepo;
import com.example.bankaccount.service.BalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class BalanceServiceImpl implements BalanceService {
    private final BalanceRepo balanceRepo;
    // Переменные для хранения статистики
    private final AtomicInteger getBalanceCount = new AtomicInteger(0);
    private final AtomicInteger changeBalanceCount = new AtomicInteger(0);
    private final AtomicInteger allChangeBalanceCount = new AtomicInteger(0);

    @Autowired
    public BalanceServiceImpl(BalanceRepo balanceRepo) {
        this.balanceRepo = balanceRepo;
    }

    @Override
    @Cacheable(value = "balances", key = "#id")
    public Optional<Long> getBalance(Long id) {
        // Инкрементируем счетчик запросов
        getBalanceCount.incrementAndGet();
        // Выполняем операцию
        return balanceRepo.findById(id).map(Balance::getAmount);
    }

    @Override
    @CacheEvict(value = "balances", key = "#id")
    public void changeBalance(Long id, Long amount) {
        // Инкрементируем счетчик запросов
        changeBalanceCount.incrementAndGet();
        // Выполняем операцию
        synchronized (this) {
            Optional<Balance> balanceOptional = balanceRepo.findById(id);
            Balance balance = balanceOptional.orElse(new Balance(id, 0L));
            balance.setAmount(balance.getAmount() + amount);
            balanceRepo.save(balance);
        }
    }

    @Override
    public AtomicInteger getReadCount() {
        return this.getBalanceCount;
    }

    @Override
    public void setReadCount() {
        this.allChangeBalanceCount.incrementAndGet();
    }

    @Override
    public AtomicInteger getAllReadCount() {
        return this.allChangeBalanceCount;
    }

    @Override
    public AtomicInteger getWriteCount() {
        return this.changeBalanceCount;
    }
}
