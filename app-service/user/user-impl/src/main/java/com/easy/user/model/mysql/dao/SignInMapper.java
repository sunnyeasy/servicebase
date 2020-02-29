package com.easy.user.model.mysql.dao;

import com.easy.user.model.mysql.po.SignIn;
import com.easy.user.model.mysql.po.SignInKey;

public interface SignInMapper {
    int deleteByPrimaryKey(SignInKey key);

    int insert(SignIn record);

    int insertSelective(SignIn record);

    SignIn selectByPrimaryKey(SignInKey key);

    int updateByPrimaryKeySelective(SignIn record);

    int updateByPrimaryKey(SignIn record);
}