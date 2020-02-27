package com.easy.gateway.transport.netty4;

import com.alibaba.fastjson.JSON;
import com.easy.common.errorcode.ResponseCode;
import com.easy.common.transport.packet.gateway.AppResponse;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

public class AppResponseBuilder {
    public static FullHttpResponse build(ResponseCode code) {
        AppResponse appResponse = new AppResponse();
        appResponse.setCode(code);

        ByteBuf body = Unpooled.copiedBuffer(JSON.toJSONString(appResponse), CharsetUtil.UTF_8);
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, body);
        return response;
    }

    public static FullHttpResponse build(ResponseCode code, String data) {
        AppResponse appResponse = new AppResponse();
        appResponse.setCode(code);
        appResponse.setData(data);

        ByteBuf body = Unpooled.copiedBuffer(JSON.toJSONString(appResponse), CharsetUtil.UTF_8);
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, body);
        return response;
    }

    public static FullHttpResponse build(ResponseCode code, String data, String params) {
        AppResponse appResponse = new AppResponse();
        appResponse.setCode(code);
        appResponse.setData(data);
        appResponse.setParams(params);

        ByteBuf body = Unpooled.copiedBuffer(JSON.toJSONString(appResponse), CharsetUtil.UTF_8);
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, body);
        return response;
    }

}
