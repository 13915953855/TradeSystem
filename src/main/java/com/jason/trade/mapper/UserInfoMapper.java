package com.jason.trade.mapper;

import com.jason.trade.model.UserInfo;
import org.springframework.stereotype.Component;

@Component
public interface UserInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserInfo record);

    int insertSelective(UserInfo record);

    UserInfo selectByPrimaryKey(Integer id);
    UserInfo selectByAccount(String account);

    int updateByPrimaryKeySelective(UserInfo record);

    int updateByPrimaryKey(UserInfo record);
}