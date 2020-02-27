package com.easy.game.gateway;

import java.util.HashSet;
import java.util.Set;

public class GameGatewayUrlConfig {
    private static final Set<String> noAuthUrls = new HashSet<>();

    static {
        //user服务
        noAuthUrls.add("/user/signUp");
    }

    public static boolean isNoAuthUrl(String url) {
        return noAuthUrls.contains(url);
    }
}
