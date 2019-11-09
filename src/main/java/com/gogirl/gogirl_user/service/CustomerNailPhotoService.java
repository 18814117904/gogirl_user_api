package com.gogirl.gogirl_user.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.gogirl.gogirl_user.dao.CustomerNailPhotoMapper;
import com.gogirl.gogirl_user.entity.CustomerNailPhoto;

@Service
public class CustomerNailPhotoService {
	@Resource
	CustomerNailPhotoMapper customerNailPhotoMapper;
	
	public List<CustomerNailPhoto> selectByCustomerNailPhoto(CustomerNailPhoto record){
		return customerNailPhotoMapper.selectByCustomerNailPhoto(record);
	}
	public CustomerNailPhoto selectByPrimaryKey(Integer id){
		return customerNailPhotoMapper.selectByPrimaryKey(id);
	}
	public int insertSelective(CustomerNailPhoto record){
		record.setCreateTime(new Date());
		record.setUpdateTime(new Date());
		return customerNailPhotoMapper.insertSelective(record);
	}
	public int updateByPrimaryKeySelective(CustomerNailPhoto record){
		record.setUpdateTime(new Date());
		return customerNailPhotoMapper.updateByPrimaryKeySelective(record);
	}
	public int deleteByPrimaryKey(Integer id){
		return customerNailPhotoMapper.deleteByPrimaryKey(id);
	}

}
