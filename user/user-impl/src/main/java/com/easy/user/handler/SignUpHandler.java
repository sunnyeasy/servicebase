package com.easy.user.handler;

import com.alibaba.fastjson.JSON;
import com.easy.common.handler.HandlerMapping;
import com.easy.common.rpcvo.BaseRecordRpcVo;
import com.easy.common.rpcvo.BaseRpcVo;
import com.easy.common.transport.packet.gateway.AppRequest;
import com.easy.user.vo.UserVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@HandlerMapping("/user")
public class SignUpHandler {
    private static final Logger logger = LoggerFactory.getLogger(SignUpHandler.class);

//    @HandlerMapping("/signUp")
//    public BaseRecordRpcVo<UserVo> signUp(AppRequest req) {
//        logger.info("req={}", JSON.toJSONString(req));
//
//        BaseRecordRpcVo<UserVo> rpcVo = new BaseRecordRpcVo<>();
//        UserVo data=new UserVo();
//        data.setName("1060@qq.com");
//        rpcVo.setData(data);
//        return rpcVo;
//    }

    @HandlerMapping("/signUp")
    public BaseRecordRpcVo<BaseRpcVo> signUp(AppRequest req) {
        logger.info("req={}", JSON.toJSONString(req));

        BaseRecordRpcVo<BaseRpcVo> rpcVo = new BaseRecordRpcVo<>();
        BaseRpcVo data=new BaseRpcVo();
        rpcVo.setData(data);
        return rpcVo;
    }

}
