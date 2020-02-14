package com.easy.common.motan;

public enum MotanServers {
    user;


    public static String groupName(MotanServers server) {
        String group = server.name();
        return group;
    }
}
