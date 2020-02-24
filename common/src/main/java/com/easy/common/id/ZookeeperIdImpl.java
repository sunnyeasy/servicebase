package com.easy.common.id;

import java.util.concurrent.atomic.AtomicLong;

public class ZookeeperIdImpl implements SuffixId {
	private long serverId;
	private AtomicLong nextSessionId = new AtomicLong();
	
	public ZookeeperIdImpl(long serverId) {
		this.serverId = serverId;
		nextSessionId.set(initializeNextSessionId());
	}
	
	@Override
	public long createId(String suffix) {
		long sessionID=nextSessionId.getAndIncrement();
		return sessionID;
	}
	
    private long initializeNextSessionId() {
        long nextSid;
        nextSid = (System.nanoTime() << 24) >>> 8;
        nextSid =  nextSid | (serverId <<56);
        return nextSid;
    }

}
