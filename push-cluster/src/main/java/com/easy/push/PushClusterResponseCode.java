package com.easy.push;

import com.easy.common.code.ResponseCode;

public class PushClusterResponseCode {
    public static final ResponseCode CONNECT_TO_CLUSTER_SERVER_FAIL = new ResponseCode(41000, "连接推送集群服务节点失败");
    public static final ResponseCode PUBLISH_MESSAGE_TO_CLUSTER_SERVER_FAIL = new ResponseCode(41000, "向推送集群服务节点投递消息失败");

}
