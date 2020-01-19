package com.easy.common.container;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    private static final ReentrantLock LOCK = new ReentrantLock();

    private static final Condition STOP = LOCK.newCondition();

    public static void await(String applicationName) {
        try {
            LOCK.lock();
            STOP.await();
        } catch (InterruptedException e) {
            logger.warn("{} stopped, interrupted by other thread!", applicationName, e);
        } finally {
            LOCK.unlock();
        }
    }

}
