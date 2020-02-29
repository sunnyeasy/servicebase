package com.easy.common.id;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * flowId
 * 【注意】不可用
 * @author Administrator
 *
 */
class IdUtilNot {
	private String serverId;
	private int nextId = 0;
	private int maxId = 10000000;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

	public IdUtilNot(long serverId, int initNextId) {
		this.serverId = String.format("%02d", serverId);
		nextId = initNextId;
	}

	/**
	 * 8位日期，2位serverId，7位数字id，2位后缀
	 * 
	 * @param suffix
	 * @return
	 */
	public synchronized long createFlowId(String suffix) {
		StringBuilder sb = new StringBuilder();
		String date = sdf.format(new Date());
		sb.append(date);
		sb.append(serverId);

		int num = nextId++;
		if (nextId == maxId) {
			nextId = 0;
		}
		sb.append(String.format("%07d", num));
		sb.append(suffix);

		return Long.parseLong(sb.toString());
	}

}
