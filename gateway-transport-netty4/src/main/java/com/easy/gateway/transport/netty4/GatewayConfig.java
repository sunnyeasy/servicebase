package com.easy.gateway.transport.netty4;

public class GatewayConfig {
    private String hostname = "0.0.0.0";
    private int port;
    private int maxChannel = 100000;
    private int readerIdleTimeSeconds = 60;
    private int minWorkerThread = 40;
    private int maxWorkerThread = 200;
    private int workerQueueSize = 0;

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getMaxChannel() {
        return maxChannel;
    }

    public void setMaxChannel(int maxChannel) {
        this.maxChannel = maxChannel;
    }

    public int getReaderIdleTimeSeconds() {
        return readerIdleTimeSeconds;
    }

    public void setReaderIdleTimeSeconds(int readerIdleTimeSeconds) {
        this.readerIdleTimeSeconds = readerIdleTimeSeconds;
    }

    public int getMinWorkerThread() {
        return minWorkerThread;
    }

    public void setMinWorkerThread(int minWorkerThread) {
        this.minWorkerThread = minWorkerThread;
    }

    public int getMaxWorkerThread() {
        return maxWorkerThread;
    }

    public void setMaxWorkerThread(int maxWorkerThread) {
        this.maxWorkerThread = maxWorkerThread;
    }

    public int getWorkerQueueSize() {
        return workerQueueSize;
    }

    public void setWorkerQueueSize(int workerQueueSize) {
        this.workerQueueSize = workerQueueSize;
    }
}
