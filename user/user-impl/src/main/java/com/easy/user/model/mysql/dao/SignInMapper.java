package com.easy.user.model.mysql.dao;

import com.easy.user.model.mysql.po.SignIn;

public interface SignInMapper {
    int deleteByPrimaryKey(Long uid);

    int insert(SignIn record);

    int insertSelective(SignIn record);

    SignIn selectByPrimaryKey(Long uid);

    int updateByPrimaryKeySelective(SignIn record);

    int updateByPrimaryKey(SignIn record);
}