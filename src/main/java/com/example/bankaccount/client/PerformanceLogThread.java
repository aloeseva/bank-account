package com.example.bankaccount.client;

import com.example.bankaccount.service.BalanceService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PerformanceLogThread extends Thread {
    private final BalanceService balanceService;

    public PerformanceLogThread(BalanceService balanceService) {
        this.balanceService = balanceService;
    }

    @Override
    public void run() {
        long readCount = 0;
        long writeCount = 0;
        long allReadCount = 0;
        long totalCount;
        long startTime = System.currentTimeMillis();
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            long readDelta = balanceService.getReadCount().intValue() - readCount;
            long writeDelta = balanceService.getWriteCount().intValue() - writeCount;
            long allReadDelta = balanceService.getAllReadCount().intValue() - allReadCount;
            readCount += readDelta;
            writeCount += writeDelta;
            allReadCount += allReadDelta;
            totalCount = readDelta + writeDelta + allReadDelta;
            long endTime = System.currentTimeMillis();
            double elapsedSeconds = (endTime - startTime) / 1000.0;
            double readPerSecond = readDelta / elapsedSeconds;
            double allReadPerSecond = allReadDelta / elapsedSeconds;
            double writePerSecond = writeDelta / elapsedSeconds;
            double totalPerSecond = totalCount / elapsedSeconds;
            log.info("{} reads/s, {} allReads/s, {} writes/s, {} total/s", (long) readPerSecond, (long) allReadPerSecond, (long) writePerSecond, (long) totalPerSecond);
            startTime = endTime;
        }
    }
}