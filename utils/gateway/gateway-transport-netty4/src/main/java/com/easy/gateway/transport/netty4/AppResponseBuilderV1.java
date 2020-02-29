package com.easy.gateway.transport.netty4;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.easy.common.errorcode.AppResponseCode;
import com.easy.common.errorcode.ResponseCode;
import com.easy.common.transport.packet.gateway.AppResponse;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

@Deprecated
public class AppResponseBuilderV1 {

    @Deprecated
    public static FullHttpResponse build(ResponseCode code) {
        AppResponse appResponse = new AppResponse();

        AppResponseCode appCode = new AppResponseCode(code);
        appResponse.setCode(appCode);

        ByteBuf body = Unpooled.copiedBuffer(JSON.toJSONString(appResponse), CharsetUtil.UTF_8);
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, body);
        return response;
    }

    @Deprecated
    public static FullHttpResponse build(ResponseCode code, String data) {
        AppResponse appResponse = new AppResponse();

        AppResponseCode appCode = new AppResponseCode(code);
        appResponse.setCode(appCode);

        appResponse.setData(data);

        ByteBuf body = Unpooled.copiedBuffer(JSON.toJSONString(appResponse), CharsetUtil.UTF_8);
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, body);
        return response;
    }

    @Deprecated
    public static FullHttpResponse build(ResponseCode code, String data, String params) {
        AppResponse appResponse = new AppResponse();

        AppResponseCode appCode = new AppResponseCode(code);
        appResponse.setCode(appCode);

        appResponse.setData(data);
        appResponse.setParams(params);

        ByteBuf body = Unpooled.copiedBuffer(JSON.toJSONString(appResponse), CharsetUtil.UTF_8);
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, body);
        return response;
    }

}
