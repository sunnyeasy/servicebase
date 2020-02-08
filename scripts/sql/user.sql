create database user;

#用户信息表
create table t_user
(
    `uid` bigint not null comment 'uid',
    `mobile` varchar(20) not null comment '手机号',
    `password` varchar(80) not null comment '登陆密码',
    `user_status` varchar(40) not null comment '状态: normal-正常; lock-锁定; deleted-删除;',
    `nick_name` varchar(64) default null comment '昵称',
    `head_picture_url` varchar(256) default null comment '头像地址',
    `real_name_status` varchar(40) default null comment '实名状态: no-未实名; yes-已实名',
    `create_time` datetime not null comment '创建时间',
    `update_time` datetime not null comment '更新时间',
    primary key (`uid`),
    unique key `uk_mobile` (`mobile`)
) engine=InnoDB default charset=utf8 comment '用户表';

#用户注册信息表
create table t_sign_up
(
    `uid` bigint not null comment 'uid',
    `business_type` not null varchar(40) comment '业务类型',
    `agent_mode` not null varchar(40) comment 'app类型: html; ios; andriod',
    `ip` varchar(64) comment 'ip',
    `lon` varchar(64) comment '经度',
    `lat` varchar(64) comment '纬度',
    ·city_code` varchar(10) comment '注册城市',
    `device_id` varchar(100) comment '设备id',
    `referrer_uid` bigint comment '推荐人uid',
    `sign_up_id` bigint not null comment '注册id',
    `sign_up_time` datetime not null comment '注册时间',
    primary key (`uid`)
) engine=InnoDB default charset=utf8 comment '用户注册信息表';

#用户注册日志表
create table t_sign_up_log
(
    `id` bigint not null comment '注册id',
    `mobile` varchar(20) not null comment '手机号',
    `uid` bigint comment 'uid',
    `business_type` varchar(40) not null comment '业务类型',
    `agent_mode` varchar(40) not null comment 'app类型: html; ios; andriod',
    `ip` varchar(64) comment 'ip',
    `lon` varchar(64) comment '经度',
    `lat` varchar(64) comment '纬度',
    ·city_code` varchar(10) comment '注册城市',
    `device_id` varchar(100) comment '设备id',
    `referrer_uid` bigint comment '推荐人uid',
    `sign_up_time` datetime not null comment '注册时间',
    `version` varchar(40) not null comment '客户端版本号',
    `result` varchar(40) not null comment '登录状态: fail-失败; successful-成功',
    `fail_code` varchar(32) default null comment '登录失败错误码',
    `fail_reason` varchar(128) default null comment '登录失败原因',
    primary key (`id`),
    key `k_uid` (`uid`),
    key `k_mobile` (`mobile`)
) engine=InnoDB default charset=utf8 comment '用户注册日志表';

#用户登录信息表
create table t_sign_in
(
    `uid` bigint not null comment 'uid',
    `business_type` varchar(40) not null comment '业务类型',
    `agent_mode` varchar(40) not null comment 'app类型: html; ios; andriod',
    `device_id` varchar(100) comment '设备id',
    `token` varchar(500) comment '令牌',
    `sign_in_id` bigint not null comment '登录id',
    `auth_time` datetime not null comment '认证时间',
    primary key (`uid`)
) engine=InnoDB default charset=utf8 comment '用户登录信息表';

#用户登录日志表
create table t_sign_in_log
(
    `id` bigint not null comment '登陆id',
    `uid` bigint not null comment 'uid',
    `sign_in_type` varchar(40) not null comment '登陆类型:password-密码;sms-验证码',
    `business_type` varchar(40) not null comment '业务类型',
    `agent_mode` varchar(40) not null comment 'app类型: html; ios; andriod',
    `ip` varchar(64) comment 'ip',
    `lon` varchar(64) comment '经度',
    `lat` varchar(64) comment '纬度',
    ·city_code` varchar(10) comment '登录城市',
    `device_id` varchar(100) comment '设备id',
    `token` varchar(500) comment '令牌',
    `version` varchar(40) not null comment '客户端版本号',
    `result` varchar(40) not null comment '登录状态: fail-失败; successful-成功',
    `fail_code` varchar(32) default null comment '登录失败错误码',
    `fail_reason` varchar(128) default null comment '登录失败原因',
    `sign_in_time` datetime default null comment '登录时间',
    primary key(`id`),
    key `k_uid` (`uid`)
)engine=InnoDB default charset=utf8 comment '用户登录日志表';