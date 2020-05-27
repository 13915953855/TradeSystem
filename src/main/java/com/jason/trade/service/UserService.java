package com.jason.trade.service;

import com.jason.trade.mapper.UserInfoMapper;
import com.jason.trade.model.UserInfo;
import com.jason.trade.shiro.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService{

	//注入Mapper接口
	@Autowired
	private UserInfoMapper userMapper;

	public User findByName(String name) {
		UserInfo userInfo = userMapper.selectByAccount(name);
		User user = new User();
		user.setId(userInfo.getId());
		user.setName(userInfo.getAccount());
		user.setPassword(userInfo.getPasswd());
		user.setPerms(userInfo.getLevelName());
		return user;
	}
}
