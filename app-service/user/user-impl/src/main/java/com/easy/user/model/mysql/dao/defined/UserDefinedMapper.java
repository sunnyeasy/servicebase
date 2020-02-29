package com.easy.user.model.mysql.dao.defined;

import com.easy.user.model.mysql.po.User;

public interface UserDefinedMapper {
    User selectByMobile(String mobile);
}
