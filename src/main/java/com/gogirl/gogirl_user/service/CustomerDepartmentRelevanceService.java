package com.gogirl.gogirl_user.service;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.gogirl.gogirl_user.dao.CustomerDepartmentRelevanceMapper;
import com.gogirl.gogirl_user.entity.CustomerBalance;
import com.gogirl.gogirl_user.entity.CustomerDepartmentRelevance;
import com.gogirl.gogirl_user.entity.CustomerDepartmentRelevanceKey;

@Service
public class CustomerDepartmentRelevanceService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Resource
	CustomerDepartmentRelevanceMapper customerDepartmentRelevanceDao;
	
    public int insertSelective(CustomerDepartmentRelevance record){
    	return customerDepartmentRelevanceDao.insertSelective(record);
    }

    public CustomerDepartmentRelevance selectByPrimaryKey(CustomerDepartmentRelevanceKey key){
    	return customerDepartmentRelevanceDao.selectByPrimaryKey(key);
    }
    public int  mergeCustomer(int fromCustomerId,int toCustomerId) {
    		CustomerBalance cc = new CustomerBalance();
    		cc.setCustomerId(fromCustomerId);
    		return customerDepartmentRelevanceDao.mergeCustomer(fromCustomerId,toCustomerId);
    }
}
