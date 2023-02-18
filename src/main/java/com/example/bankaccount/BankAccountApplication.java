package com.example.bankaccount;

import com.example.bankaccount.client.BalanceServiceClient;
import com.example.bankaccount.client.PerformanceLogThread;
import com.example.bankaccount.service.BalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
@EnableCaching
public class BankAccountApplication {
    private static BalanceService balanceService;

    @Autowired
    public BankAccountApplication(BalanceService balanceService) {
        BankAccountApplication.balanceService = balanceService;
    }

    public static void main(String[] args) {
        SpringApplication.run(BankAccountApplication.class, args);
        startClientTest();
    }

    private static void startClientTest() {
        // Параметры настройки клиента
        int threadCount = 4;
        double readQuota = 0.8;
        double writeQuota = 0.2;
        List<Long> readIdList = Arrays.asList(1L, 2L, 3L);
        List<Long> writeIdList = Arrays.asList(1L, 2L, 3L);

        // Запуск потока для записи логов производительности
        PerformanceLogThread performanceLogThread = new PerformanceLogThread(balanceService);
        performanceLogThread.start();

        // Создание и запуск клиентских потоков
        BalanceServiceClient balanceServiceClient = new BalanceServiceClient(balanceService, threadCount, readQuota, writeQuota, readIdList, writeIdList);

        try {
            balanceServiceClient.run();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Ожидание завершения клиентских потоков и потока записи логов
        try {
            performanceLogThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
