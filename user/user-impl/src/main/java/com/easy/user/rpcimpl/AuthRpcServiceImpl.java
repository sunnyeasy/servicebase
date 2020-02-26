package com.easy.user.rpcimpl;

import com.alibaba.fastjson.JSON;
import com.easy.common.rpcao.AuthRpcAo;
import com.easy.common.rpcvo.AuthRpcVo;
import com.easy.user.rpcapi.AuthRpcService;
import com.weibo.api.motan.config.springsupport.annotation.MotanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@MotanService
public class AuthRpcServiceImpl implements AuthRpcService {
    private static final Logger logger = LoggerFactory.getLogger(AuthRpcServiceImpl.class);

    @Override
    public AuthRpcVo auth(AuthRpcAo ao) {
        logger.debug("用户认证请求,ao={}", JSON.toJSONString(ao));
        AuthRpcVo vo = new AuthRpcVo();
        vo.setUid(1000000L);

//        try {
//            Thread.sleep(1000000);
//        } catch (InterruptedException e) {
//            logger.error("sleep exception.", e);
//        }
        return vo;
    }
}
