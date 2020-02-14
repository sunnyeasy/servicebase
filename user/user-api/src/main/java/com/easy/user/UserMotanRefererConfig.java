package com.easy.user;

import com.easy.common.motan.MotanBeanConfig;
import com.easy.common.motan.MotanServers;
import com.easy.user.rpcapi.PushAuthRpcService;
import com.easy.user.rpcapi.PushAuthRpcServiceAsync;
import com.weibo.api.motan.config.springsupport.BasicRefererConfigBean;
import com.weibo.api.motan.config.springsupport.RefererConfigBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserMotanRefererConfig {
    @Bean
    public BasicRefererConfigBean userBasicRefererConfig() {
        return MotanBeanConfig.getBasicRefererConfigBean(MotanServers.groupName(MotanServers.user),
                MotanServers.user.name(), MotanServers.user.name());
    }

    @Bean
    public RefererConfigBean<PushAuthRpcServiceAsync> pushAuthRpcService(@Qualifier("userBasicRefererConfig") BasicRefererConfigBean userBasicRefererConfig) {
        RefererConfigBean<PushAuthRpcServiceAsync> bean = new RefererConfigBean<>();
        bean.setVersion(PushAuthRpcServiceAsync.VERSION);
        bean.setInterface(PushAuthRpcServiceAsync.class);
        bean.setBasicReferer(userBasicRefererConfig);
        bean.setRetries(0);
        return bean;
    }
}
