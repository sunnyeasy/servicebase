package com.easy.common.motan;

import com.weibo.api.motan.config.springsupport.BasicRefererConfigBean;
import com.weibo.api.motan.config.springsupport.ProtocolConfigBean;
import com.weibo.api.motan.config.springsupport.RegistryConfigBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
@Order(0)
public class MotanBeanConfig {
    public static final String MOTAN_RPC_REGISTRY_NAME = "motanRegistry";

    public static final String MOTAN_RPC_CLIENTPROTOCOL_NAME = "motanClientProtocol";

    public static final String MOTAN_RPC_SERVERPROTOCOL_NAME = "motanServerProtocol";

    @Value("${zookeeper.address}")
    private String zookeeperAddress;

    public static BasicRefererConfigBean getBasicRefererConfigBean(String groupName, String moduleName, String appName) {
        BasicRefererConfigBean bean = new BasicRefererConfigBean();
        bean.setProtocol(MOTAN_RPC_CLIENTPROTOCOL_NAME);
        bean.setGroup(groupName);
        bean.setModule(moduleName);
        bean.setApplication(appName);
        bean.setAsyncInitConnection(false);
        bean.setRequestTimeout(30 * 1000);
        bean.setRegistry(MOTAN_RPC_REGISTRY_NAME);
        bean.setCheck(false);
        bean.setAccessLog(false);
        bean.setRetries(0);
        bean.setThrowException(true);
        return bean;
    }

    @Bean
    public RegistryConfigBean motanRegistry() throws Exception {
        RegistryConfigBean registry = new RegistryConfigBean();
        registry.setAddress(zookeeperAddress);
        registry.setConnectTimeout(10000);
        registry.setRegProtocol("zookeeper");
        registry.setName("registry");
        return registry;
    }

    @Bean
    public ProtocolConfigBean motanClientProtocol() {
        ProtocolConfigBean bean = new ProtocolConfigBean();
        bean.setDefault(true);
        bean.setName("motan");
        bean.setHaStrategy("failover");
        bean.setLoadbalance("consistent");
        bean.setMaxClientConnection(1000);
        bean.setMinClientConnection(10);
        bean.setRequestTimeout(30 * 1000);//超时时间为10s
        bean.setMaxContentLength(10 * 1024 * 1024);
        return bean;
    }


    @Bean
    public ProtocolConfigBean motanServerProtocol() {
        ProtocolConfigBean bean = new ProtocolConfigBean();
        bean.setDefault(true);
        bean.setName("motan");
        bean.setMaxClientConnection(1000);
        bean.setMinClientConnection(10);
        bean.setMaxContentLength(1048576);
        bean.setMaxWorkerThread(1000);
        bean.setMinWorkerThread(100);
        bean.setMaxContentLength(10 * 1024 * 1024);
        return bean;
    }
}
