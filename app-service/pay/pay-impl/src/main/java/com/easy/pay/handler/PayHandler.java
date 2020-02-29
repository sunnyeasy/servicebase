package com.easy.pay.handler;

import com.easy.common.handler.HandlerMapping;
import com.easy.common.rpcvo.BaseRecordRpcVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@HandlerMapping("/pay")
public class PayHandler {
    private static final Logger logger = LoggerFactory.getLogger(PayHandler.class);

    @HandlerMapping("/alipay")
    public BaseRecordRpcVo<String> aliPay() {
        BaseRecordRpcVo<String> rpcVo = new BaseRecordRpcVo<>();
        String data = "hello ali pay";
        rpcVo.setData(data);
        return rpcVo;
    }
}
