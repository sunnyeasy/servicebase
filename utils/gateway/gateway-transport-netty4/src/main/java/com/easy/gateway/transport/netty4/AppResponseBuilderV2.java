package com.easy.gateway.transport.netty4;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.easy.common.errorcode.ResponseCode;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppResponseBuilderV2 {
    private static final Logger logger= LoggerFactory.getLogger(AppResponseBuilderV2.class);

    public static FullHttpResponse build(ResponseCode code) {
        JSONObject root = new JSONObject();

        JSONObject rootCode = new JSONObject();
        rootCode.put("errorCode", code.getErrorCode());
        rootCode.put("message", code.failReason());
        root.put("code", rootCode);

        String json=root.toJSONString();
        if (logger.isDebugEnabled()) {
            logger.debug("response body={}", json);
        }
        ByteBuf body = Unpooled.copiedBuffer(json, CharsetUtil.UTF_8);
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, body);
        return response;
    }

    public static FullHttpResponse build(ResponseCode code, String data) {
        JSONObject root = new JSONObject();

        JSONObject rootCode = new JSONObject();
        rootCode.put("errorCode", code.getErrorCode());
        rootCode.put("message", code.failReason());
        root.put("code", rootCode);

        JSONObject rootData = JSON.parseObject(data);
        root.put("data", rootData);

        String json=root.toJSONString();
        if (logger.isDebugEnabled()) {
            logger.debug("response body={}", json);
        }
        ByteBuf body = Unpooled.copiedBuffer(json, CharsetUtil.UTF_8);
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, body);
        return response;
    }

    public static FullHttpResponse build(ResponseCode code, String data, String params) {
        JSONObject root = new JSONObject();

        JSONObject rootCode = new JSONObject();
        rootCode.put("errorCode", code.getErrorCode());
        rootCode.put("message", code.failReason());
        root.put("code", rootCode);

        JSONObject rootData = JSON.parseObject(data);
        root.put("data", rootData);

        root.put("params", params);

        String json=root.toJSONString();
        if (logger.isDebugEnabled()) {
            logger.debug("response body={}", json);
        }
        ByteBuf body = Unpooled.copiedBuffer(json, CharsetUtil.UTF_8);
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, body);
        return response;
    }

}
