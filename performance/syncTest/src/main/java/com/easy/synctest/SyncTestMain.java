package com.easy.synctest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class SyncTestMain {
    private static final Logger logger = LoggerFactory.getLogger(SyncTestMain.class);

    public static void main(String[] args) {
        for (int n = 0; n < 100; n++) {
            long startTime = System.currentTimeMillis();
            for (int i = 0; i < 100000; i++) {
                lock();
            }
            long endTime = System.currentTimeMillis();
            logger.info("usedTime = {}(ms)", endTime - startTime);
        }

        List<Thread> threads = new ArrayList<>();
        for (int c=0; c<100; c++) {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int n = 0; n < 100; n++) {
                        long startTime = System.currentTimeMillis();
                        for (int i = 0; i < 100000; i++) {
                            lock();
                        }
                        long endTime = System.currentTimeMillis();
                        logger.info("usedTime = {}(ms)", endTime - startTime);
                    }
                }
            });
            t.setName("test-"+c);
            threads.add(t);
            t.start();
        }
    }

    private static void lock() {
        synchronized (SyncTestMain.class) {

        }
    }
}
