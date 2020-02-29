package com.easy.user.model.mysql.dao;

import com.easy.user.model.mysql.po.SignUp;

public interface SignUpMapper {
    int deleteByPrimaryKey(Long uid);

    int insert(SignUp record);

    int insertSelective(SignUp record);

    SignUp selectByPrimaryKey(Long uid);

    int updateByPrimaryKeySelective(SignUp record);

    int updateByPrimaryKey(SignUp record);
}