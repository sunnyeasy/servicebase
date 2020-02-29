package com.easy.common.zookeeper;

import org.springframework.core.env.Environment;

public class ZkConfigFactory {
    public static ZkConfig build(Environment environment) {
        ZkConfig zkConfig = new ZkConfig();

        String v = environment.getProperty("zookeeper.address");
        zkConfig.setAddress(v);

        return zkConfig;
    }
}
