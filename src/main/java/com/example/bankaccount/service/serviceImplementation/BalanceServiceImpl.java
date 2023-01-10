package com.example.bankaccount.service.serviceImplementation;

import com.example.bankaccount.entity.Balance;
import com.example.bankaccount.repos.BalanceRepo;
import com.example.bankaccount.service.BalanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Optional;

@Slf4j
@Service
public class BalanceServiceImpl implements BalanceService {
    private final BalanceRepo balanceRepo;

    @Autowired
    public BalanceServiceImpl(BalanceRepo balanceRepo) {
        this.balanceRepo = balanceRepo;
    }

    @Override
    public Optional<Balance> getBalance(Long id) {
        return balanceRepo.findById(id);
    }

    @Override
    public void changeBalance(Long id, Long amount) {
        final Optional<Balance> optionalBalance = getBalance(id);
        optionalBalance.ifPresent(
                balance -> {
                    balance.setAmount(balance.getAmount() + amount);
                    save(balance);
                });
    }

    @CacheEvict(value="balances", allEntries=true)
    public LinkedHashSet<Balance> addBalance(String amount, Model model) {
        LinkedHashSet<Balance> balances = new LinkedHashSet<>();

        try {
            Long amountL = Long.parseLong(amount);
            Balance balance = new Balance(amountL);
            save(balance);
            balances.add(balance);
            log.info("Баланс сохранен по ID: {} и со значением: {}", balance.getId(), balance.getAmount());
        } catch (NumberFormatException ex) {
            log.error("При добавлении баланса возникла ошибка: {}", ex.getMessage());
            model.addAttribute("addError", "При добавлении баланса возникла ошибка");
        }

        return balances;
    }

    @Cacheable(value = "balance", key = "#id")
    public LinkedHashSet<Balance> getBalance(String id, Model model) {
        LinkedHashSet<Balance> balances = new LinkedHashSet<>();
        try {
            Long idL = Long.parseLong(id);
            if (getBalance(idL).isPresent()) {
                log.info("Получение баланса по ID: {}", idL);
                balances.add(getBalance(idL).get());
            } else {
                log.info("Баланс по ID: {} не найден", idL);
                model.addAttribute("getError", "При получении баланса возникла ошибка");
            }
        } catch (NumberFormatException ex) {
            log.error("При получении баланса возникла ошибка: {}", ex.getMessage());
            model.addAttribute("getError", "При получении баланса возникла ошибка");
        }

        return balances;
    }

    @Caching(evict = {@CacheEvict(value="balances", allEntries=true)},
            put = {@CachePut(value = "balance", key = "#id")})
    public LinkedHashSet<Balance> changeBalance(String amount, String id, Model model) {

        try {
            Long amountL = Long.parseLong(amount);
            Long idL = Long.parseLong(id);
            log.info("Обновление баланса по ID: {}", idL);
            changeBalance(idL, amountL);
        } catch (NumberFormatException ex) {
            log.error("При изменении баланса возникла ошибка: {}", ex.getMessage());
            model.addAttribute("changeError", "При изменении баланса возникла ошибка");
        }

        return getBalance(id, model);
    }

    @Cacheable(value = "balances")
    public LinkedHashSet<Balance> findAll() {
        return new LinkedHashSet<>(balanceRepo.findAll());
    }

    public void save(Balance balance) {
        balanceRepo.save(balance);
    }
}
