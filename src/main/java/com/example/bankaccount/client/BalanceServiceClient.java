package com.example.bankaccount.client;

import com.example.bankaccount.service.BalanceService;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class BalanceServiceClient {
    private static final Random random = new Random();

    private final BalanceService balanceService;
    private final int threadCount;
    private final double readQuota;
    private final double writeQuota;
    private final List<Long> readIdList;
    private final List<Long> writeIdList;

    public BalanceServiceClient(BalanceService balanceService, int threadCount,
                                double readQuota, double writeQuota, List<Long> readIdList, List<Long> writeIdList) {
        this.balanceService = balanceService;
        this.threadCount = threadCount;
        this.readQuota = readQuota;
        this.writeQuota = writeQuota;
        this.readIdList = readIdList;
        this.writeIdList = writeIdList;
    }

    public void run() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        for (int i = 0; i < threadCount; i++) {
            executor.submit(new ClientTask());
        }
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.DAYS);
    }

    private class ClientTask implements Runnable {
        @Override
        public void run() {
            while (true) {
                double readProbability = readQuota / (readQuota + writeQuota);

                if (random.nextDouble() < readProbability) {
                    balanceService.setReadCount();
                    balanceService.getBalance(randomFromList(readIdList));
                } else {
                    balanceService.changeBalance(randomFromList(writeIdList), 1L);
                }
            }
        }

        private Long randomFromList(List<Long> list) {
            return list.get(random.nextInt(list.size()));
        }
    }
}