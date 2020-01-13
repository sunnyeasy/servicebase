create database user;

#用户信息表
create table t_user
(
    `clientId` bigint not null comment 'clientId',
    `mobile` varchar(20) default null comment '手机号',
    `mobile_status` varchar(40) not null comment '手机号状态: disable-未启用; enable-已启用; lock-锁定',
    `email` varchar(200) default null comment '邮箱',
    `email_status` varchar(40) not null comment '邮箱状态: disable-未启用; enable-已启用; lock-锁定',
    `real_name_status` varchar(40) default null comment '实名状态: unRealName-未实名; realName-已实名',
    `password` varchar(80) not null comment '登陆密码',
    `user_status` varchar(40) not null comment '状态: normal-正常; lock-锁定; deleted-删除;',
    `nick_name` varchar(64) default null comment '昵称',
    `head_picture_url` varchar(256) default null comment '头像地址',
    `sign_up_mode` varchar(40) not null comment '注册类型: browser; ios; andriod',
    `create_time` datetime not null comment '创建时间',
    `update_time` datetime not null comment '更新时间',
    primary key (`clientId`),
    unique key uk_mobile (`mobile`),
    unique key uk_email (`email`)
) engine=InnoDB default charset=utf8 comment '用户表';

#用户登录日志
create table t_sign_in_log
(
    `id` bigint not null comment '登陆id',
    `clientId` bigint not null comment 'clientId',
    `account_mode` varchar(40) not null comment '登陆账号类型:mobile,email',
    `ip` varchar(64) not null comment '登录ip',
    `lon` varchar(64) not null comment '登录经度',
    `lat` varchar(64) not null comment '登录纬度',
    `agent_mode` varchar(40) default '0' comment 'app类型: html; ios; andriod',
    `result` varchar(40) not null comment '登录状态: fail-失败; successful-成功',
    `fail_code` varchar(32) default null comment '登录失败错误码',
    `fail_reason` varchar(128) default null comment '登录失败原因',
    `create_time` datetime default null comment '创建时间',
    `update_time` datetime default null comment '更新时间',
    primary key(`id`),
    key k_uid (`clientId`)
)engine=InnoDB default charset=utf8 comment '用户登录日志';