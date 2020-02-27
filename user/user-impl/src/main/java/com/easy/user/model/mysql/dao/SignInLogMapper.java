package com.easy.user.model.mysql.dao;

import com.easy.user.model.mysql.po.SignInLog;

public interface SignInLogMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SignInLog record);

    int insertSelective(SignInLog record);

    SignInLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SignInLog record);

    int updateByPrimaryKey(SignInLog record);
}