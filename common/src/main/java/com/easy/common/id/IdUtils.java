package com.easy.common.id;

public class IdUtils {
    public static final IdUtils ID_UTILS = new IdUtils();
    private long serverId;
    private Id sessionIdUtil;
    private Id uidUtil;
    private Id primaryKeyIdUtil;
    private Id pushMessageIdUtil;

    private SuffixId flowIdUtil;

    private IdUtils() {
        serverId = Long.valueOf(System.getProperty("serverId", "00"));
        sessionIdUtil = new IdImpl(serverId);
        uidUtil = new IdImpl(serverId);
        primaryKeyIdUtil = new IdImpl(serverId);
        pushMessageIdUtil = new IdImpl(serverId);
        flowIdUtil = new SuffixIdImpl(serverId);
    }

    public static IdUtils getInstance() {
        return ID_UTILS;
    }

    public long createSessionId() {
        return sessionIdUtil.createId();
    }

    public long createUid() {
        return uidUtil.createId();
    }

    public long createFlowId(String suffix) {
        return flowIdUtil.createId(suffix);
    }

    public long createPushMessageId() {
        return pushMessageIdUtil.createId();
    }

    public long createPrimaryKeyId() {
        return primaryKeyIdUtil.createId();
    }
}
