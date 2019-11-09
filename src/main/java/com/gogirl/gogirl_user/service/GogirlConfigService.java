package com.gogirl.gogirl_user.service;


import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.gogirl.gogirl_user.dao.GogirlConfigMapper;
import com.gogirl.gogirl_user.entity.GogirlConfig;


@Service
public class GogirlConfigService {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource
	GogirlConfigMapper gogirlConfigMapper;

	public List<GogirlConfig> selectByType(String type) {
		return gogirlConfigMapper.selectByType(type);
	}
}
