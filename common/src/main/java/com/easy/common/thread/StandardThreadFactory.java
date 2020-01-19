package com.easy.common.thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class StandardThreadFactory implements ThreadFactory {
    private static final AtomicInteger poolNo = new AtomicInteger(1);
    private final ThreadGroup threadGroup;
    private final AtomicInteger currentThreadNo = new AtomicInteger(1);
    private final String prefixName;
    private int priority = Thread.NORM_PRIORITY;
    private boolean isDaemon = false;

    public StandardThreadFactory(String prefix, boolean isDaemon) {
        SecurityManager s = System.getSecurityManager();
        this.threadGroup = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
        this.prefixName = prefix;
        this.isDaemon = isDaemon;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(threadGroup, r);
        thread.setName(prefixName + currentThreadNo.getAndIncrement());
        thread.setDaemon(isDaemon);
        thread.setPriority(priority);
        return null;
    }
}
