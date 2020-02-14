package com.easy.user.boot;

import com.easy.common.motan.MotanBeanConfig;
import com.easy.common.motan.MotanServers;
import com.easy.common.network.ServerPorts;
import com.easy.user.rpcapi.PushAuthRpcService;
import com.weibo.api.motan.config.springsupport.BasicServiceConfigBean;
import com.weibo.api.motan.config.springsupport.ServiceConfigBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserMotanServiceConfig {

    @Bean
    public BasicServiceConfigBean basicServiceConfig() {
        BasicServiceConfigBean bean = new BasicServiceConfigBean();
        bean.setExport(MotanBeanConfig.MOTAN_RPC_SERVERPROTOCOL_NAME + ":" + ServerPorts.userMotanPort.getPort());
        bean.setGroup(MotanServers.groupName(MotanServers.user));
        bean.setAccessLog(false);
        bean.setShareChannel(true);
        bean.setModule(MotanServers.user.name());
        bean.setApplication(MotanServers.user.name());
        bean.setRegistry(MotanBeanConfig.MOTAN_RPC_REGISTRY_NAME);
        return bean;
    }

    @Bean
    public ServiceConfigBean<PushAuthRpcService> pushRpcService(@Autowired PushAuthRpcService pushAuthRpcService) {
        ServiceConfigBean<PushAuthRpcService> bean = new ServiceConfigBean<PushAuthRpcService>();
        bean.setInterface(PushAuthRpcService.class);
        bean.setVersion(PushAuthRpcService.VERSION);
        bean.setRef(pushAuthRpcService);
//        bean.setFilter("serverExceptionFilter");
        return bean;
    }

}
