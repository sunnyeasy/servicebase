package com.easy.common.id;

/**
 * long 64位: 48位时间戳(yyyy-MM-dd HH:mm:ss.SSS), 5位serverIdBit, 11位seqBit
 * 每豪秒间隔容纳id数: 2048
 * @author Administrator
 *
 */

class IdImpl implements Id {
	
	private final long serverId;
	private final long serverIdBits;
	private long lastTimestampBits = -1L;
	private long sequenceBits;
	private static final long SERVER_ID_MASK = 0x1fL; 
	private static final long SEQUENCE_MASK = 0X7ffL;
	private static final long TIMESTAMP_LEFT_MOVE_BITS = 16L;
	private static final long SERVER_ID_LEFT_MOVE_BITS = 11L;
	
	public IdImpl(long serverId) {
		this.serverId = serverId;
		serverIdBits = this.serverId & SERVER_ID_MASK;
		sequenceBits = 0L;
	}
	
	@Override
	public synchronized long createId() {
        long timestampBits = timeGen();

        //如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过这个时候应当抛出异常
        if (timestampBits < lastTimestampBits) {
            throw new RuntimeException(
                    String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestampBits - timestampBits));
        }

        //如果是同一时间生成的，则进行毫秒内序列
        if (lastTimestampBits == timestampBits) {
        	sequenceBits = (sequenceBits+1) & SEQUENCE_MASK;
            //毫秒内序列溢出
            if (0 == sequenceBits) {
                //阻塞到下一个毫秒,获得新的时间戳
                timestampBits = tilNextMillis(lastTimestampBits);
                sequenceBits = 0L;
            }
        } else {//时间戳改变，毫秒内序列重置
            sequenceBits = 0L;
        }

        //上次生成ID的时间截
        lastTimestampBits = timestampBits;

		return (timestampBits << TIMESTAMP_LEFT_MOVE_BITS)
                | (serverIdBits << SERVER_ID_LEFT_MOVE_BITS)
				| sequenceBits;
	}

    /**
     * 阻塞到下一个毫秒，直到获得新的时间戳
     * @param lastTimestamp 上次生成ID的时间截
     * @return 当前时间戳
     */
    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    /**
     * 返回以毫秒为单位的当前时间
     * @return 当前时间(毫秒)
     */
    protected long timeGen() {
        return System.currentTimeMillis();
    }

}
