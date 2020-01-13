package com.easy.user.model.mysql.dao.base;

import com.easy.user.model.mysql.po.base.User;

public interface UserMapper {
    int deleteByPrimaryKey(Long uid);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Long uid);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
}