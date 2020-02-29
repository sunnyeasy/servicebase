package com.easy.common.motan;

public enum MotanServers {
    gamePush,
    gameGateway,
    user,
    pay;


    public static String groupName(MotanServers server) {
        String group = server.name();
        return group;
    }
}
