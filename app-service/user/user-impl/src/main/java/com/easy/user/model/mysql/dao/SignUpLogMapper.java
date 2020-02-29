package com.easy.user.model.mysql.dao;

import com.easy.user.model.mysql.po.SignUpLog;

public interface SignUpLogMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SignUpLog record);

    int insertSelective(SignUpLog record);

    SignUpLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SignUpLog record);

    int updateByPrimaryKey(SignUpLog record);
}