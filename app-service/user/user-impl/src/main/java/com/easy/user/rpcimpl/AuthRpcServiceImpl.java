package com.easy.user.rpcimpl;

import com.alibaba.fastjson.JSON;
import com.easy.common.errorcode.ResponseCode;
import com.easy.common.exception.BusinessException;
import com.easy.common.rpcao.AuthRpcAo;
import com.easy.common.rpcvo.AuthRpcVo;
import com.easy.user.model.mysql.dao.SignInMapper;
import com.easy.user.model.mysql.po.SignIn;
import com.easy.user.model.mysql.po.SignInKey;
import com.easy.user.rpcapi.AuthRpcService;
import com.easy.user.token.Token;
import com.weibo.api.motan.config.springsupport.annotation.MotanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Base64;

@MotanService
public class AuthRpcServiceImpl implements AuthRpcService {
    private static final Logger logger = LoggerFactory.getLogger(AuthRpcServiceImpl.class);

    @Autowired
    private SignInMapper signInMapper;

    @Override
    public AuthRpcVo auth(AuthRpcAo ao) {
        logger.debug("用户认证请求,ao={}", JSON.toJSONString(ao));

        AuthRpcVo rpcVo = null;
        try {
            rpcVo = doAuth(ao);
        } catch (Exception e) {
            logger.error("用户认证异常,rpcAo={}", JSON.toJSONString(ao), e);
            rpcVo = new AuthRpcVo();
            ResponseCode code = BusinessException.parseResponseCode(e);
            rpcVo.setCode(code);
        }
        logger.info("用户认证结果,rpcAo={}, rpcVo={}", JSON.toJSONString(ao), JSON.toJSONString(rpcVo));
        return rpcVo;
    }

    private AuthRpcVo doAuth(AuthRpcAo rpcAo) {
        String tokenJson = new String(Base64.getDecoder().decode(rpcAo.getToken()));
        Token token = JSON.parseObject(tokenJson, Token.class);

        SignInKey signInKey = new SignInKey();
        signInKey.setUid(token.getUid());
        signInKey.setBusinessType(rpcAo.getBusinessType().name());
        signInKey.setAgentMode(rpcAo.getAgentMode().name());

        SignIn signIn = signInMapper.selectByPrimaryKey(signInKey);
        if (null == signIn) {
            throw new BusinessException(ResponseCode.UNAUTHORIZED_PARAMETERS_ERROR);
        }

        if (!signIn.getToken().equals(rpcAo.getToken())) {
            throw new BusinessException(ResponseCode.UNAUTHORIZED_PARAMETERS_ERROR.build("token错误"));
        }

//        if (!signIn.getDeviceId().equals(rpcAo.getDeviceId())) {
//            throw new BusinessException(ResponseCode.UNAUTHORIZED_PARAMETERS_ERROR.build("deviceId错误"));
//        }

        AuthRpcVo vo = new AuthRpcVo();
        vo.setUid(token.getUid());
        return vo;
    }
}
