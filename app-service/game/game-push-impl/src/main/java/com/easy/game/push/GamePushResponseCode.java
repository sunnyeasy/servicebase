package com.easy.game.push;

import com.easy.common.errorcode.ResponseCode;

public class GamePushResponseCode {
    public static final ResponseCode NOT_MATCH_PUSH_NODE = new ResponseCode(4000000, "未匹配到推送服务节点");
    public static final ResponseCode SAVE_PUSH_MESSAGE_ERROR = new ResponseCode(40000001, "保存消息失败");
}
