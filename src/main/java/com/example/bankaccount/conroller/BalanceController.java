package com.example.bankaccount.conroller;

import com.example.bankaccount.service.serviceImplementation.BalanceServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

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
        balanceService.setReadCount();
        model.addAttribute("id", id);
        try {
            Optional<Long> amount = balanceService.getBalance(Long.valueOf(id));
            if (amount.isPresent()) {
                model.addAttribute("balance", amount.get());
            } else {
                model.addAttribute("balance", "");
            }
        } catch (NumberFormatException ignored) {
            model.addAttribute("balance", "");
        }
        model.addAttribute("readCount", balanceService.getReadCount());
        model.addAttribute("writeCount", balanceService.getWriteCount());
        model.addAttribute("allReadCount", balanceService.getAllReadCount());

        return "balance";
    }

    @PostMapping("/balance/get_balance/{id}")
    public String changeBalance(
            @RequestParam String amount,
            @PathVariable String id,
            Model model
    ) {
        balanceService.changeBalance(Long.valueOf(id), Long.valueOf(amount));
        model.addAttribute("balance", balanceService.getBalance(Long.valueOf(id)).get());
        model.addAttribute("readCount", balanceService.getReadCount());
        model.addAttribute("writeCount", balanceService.getWriteCount());
        model.addAttribute("allReadCount", balanceService.getAllReadCount());

        return "balance";
    }
}
