package com.easy.terminal.client;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.easy.common.container.Main;
import com.easy.common.enums.AgentMode;
import com.easy.common.enums.BusinessType;
import com.easy.common.errorcode.ResponseCode;
import com.easy.common.exception.BusinessException;
import com.easy.common.transport.NetworkConstants;
import com.easy.common.transport.packet.gateway.AppRequest;
import com.easy.constant.enums.user.model.SignInLogEnums;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TerminalClient {
    private static final Logger logger = LoggerFactory.getLogger(TerminalClient.class);

    public static void main(String[] args) {
        String url = "http://127.0.0.1:15200/user";

        AppRequest appRequest = new AppRequest();
        appRequest.setUrl("/user/signIn/do");
        appRequest.setVersion("1.0");
        appRequest.setBusinessType(BusinessType.GAME);
        appRequest.setAgentMode(AgentMode.HTML);
        appRequest.setDeviceId("123456789");
        appRequest.setImei("1234567890");
        appRequest.setParams("params");

        JSONObject req = JSON.parseObject(JSON.toJSONString(appRequest));

        SignInAo ao = new SignInAo();
        ao.setSignInType(SignInLogEnums.SignInType.password);
        ao.setMobile("123456790");
        ao.setPassword("123456");

        req.put("data", ao);

        String body = req.toJSONString();
        logger.info("req={}", body);

        String resp = HttpUtil.post(url, body);
        logger.info("resp={}", resp);

        ResponseVo<SignInVo> vo = JSON.parseObject(resp, ResponseVo.class);
        if (vo.getCode().getErrorCode() != ResponseCode.SUCESSFUL.getErrorCode()) {
            logger.error("登陆失败, ao={}, vo={}");
            throw new BusinessException(new ResponseCode(vo.getCode().getErrorCode(), vo.getCode().getMessage()));
        }

        JSONObject json = JSON.parseObject(resp);
        SignInVo data = json.getObject("data", SignInVo.class);
        vo.setData(data);

        MemoryPersistence persistence = new MemoryPersistence();
        try {
            String uri = "tcp://" + vo.getData().getPushServer().getHostname() + ":" + vo.getData().getPushServer().getPort();
            MqttClient mqttClient = new MqttClient(uri, String.valueOf(System.currentTimeMillis()), persistence);

            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(false);
            options.setUserName("easyfun");

            AppRequest password =new AppRequest();
            password.setToken(vo.getData().getToken());
            password.setBusinessType(appRequest.getBusinessType());
            password.setAgentMode(appRequest.getAgentMode());
            password.setDeviceId(appRequest.getDeviceId());
            password.setImei(appRequest.getImei());
            password.setVersion(appRequest.getVersion());

            options.setPassword(JSON.toJSONString(password).toCharArray());
            options.setKeepAliveInterval(20);

            mqttClient.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable throwable) {
                    logger.info("Disconnect");
                }

                @Override
                public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                    logger.info("topic={}", s);
                    logger.info("qoS={}", mqttMessage.getQos());
                    logger.info("payload={}", new String(mqttMessage.getPayload(), NetworkConstants.UTF8));
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                    logger.info("delivery complete, isComplete={}", iMqttDeliveryToken.isComplete());
                }
            });

            mqttClient.connect(options);

//            MqttMessage message = new MqttMessage("hello easyfun".getBytes());
//            message.setQos(1);
//
//            mqttClient.publish("mqtt/test", message);

        } catch (Exception e) {
            logger.error("请求失败, ", e);
        }

        Main.await("Mqtt client");
    }
}
