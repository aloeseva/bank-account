package com.example.bankaccount.conroller;

import com.example.bankaccount.entity.Balance;
import com.example.bankaccount.service.serviceImplementation.BalanceServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.LinkedHashSet;

@Controller
public class BalanceController {

    private final BalanceServiceImpl balanceService;

    @Autowired
    public BalanceController(BalanceServiceImpl balanceService) {
        this.balanceService = balanceService;
    }

    @GetMapping("/balance/get_balance")
    public String balance(
            @RequestParam(required = false, defaultValue = "") String id,
            Model model
    ) {
        model.addAttribute("id", id);
        if (id.isEmpty()){
            model.addAttribute("balances", balanceService.findAll());
        } else {
            model.addAttribute("balances", balanceService.getBalance(id, model));
        }

        return "balance";
    }

    @PostMapping("/balance/get_balance")
    public String balanceAdd(
            @RequestParam String amount,
            Model model
    ) {
        model.addAttribute("balances", balanceService.addBalance(amount, model));

        return "balance";
    }

    @PostMapping("/balance/get_balance/{id}")
    public String changeBalance(
            @RequestParam String amount,
            @PathVariable String id,
            Model model
    ) {
        model.addAttribute("balances", balanceService.changeBalance(amount, id, model));

        return "balance";
    }
}
