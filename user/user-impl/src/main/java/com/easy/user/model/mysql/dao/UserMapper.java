package com.easy.user.model.mysql.dao;

import com.easy.user.model.mysql.dao.defined.UserDefinedMapper;
import com.easy.user.model.mysql.po.User;

public interface UserMapper extends UserDefinedMapper {
    int deleteByPrimaryKey(Long uid);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Long uid);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
}