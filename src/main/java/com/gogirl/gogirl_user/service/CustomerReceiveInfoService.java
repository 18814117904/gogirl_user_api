package com.gogirl.gogirl_user.service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gogirl.gogirl_user.dao.CustomerReceiveInfoMapper;
import com.gogirl.gogirl_user.dao.CustomerWeiboMapper;
import com.gogirl.gogirl_user.entity.CustomerBalance;
import com.gogirl.gogirl_user.entity.CustomerReceiveInfo;
import com.gogirl.gogirl_user.entity.CustomerWeibo;

@Service
public class CustomerReceiveInfoService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	CustomerReceiveInfoMapper customerReceiveInfoMapper;

    
    
    public int insertSelective(CustomerReceiveInfo record){
    	return customerReceiveInfoMapper.insertSelective(record);
    }

    public CustomerReceiveInfo selectByPrimaryKey(Integer customerId){
    	return customerReceiveInfoMapper.selectByPrimaryKey(customerId);
    }

    public int updateByPrimaryKeySelective(CustomerReceiveInfo record){
    	return customerReceiveInfoMapper.updateByPrimaryKeySelective(record);
    }
    public int  mergeCustomer(int fromCustomerId,int toCustomerId) {
    	CustomerReceiveInfo c = customerReceiveInfoMapper.selectByPrimaryKey(toCustomerId);
    	if(c!=null){
    		logger.error("两目标customerId已经存在.");
    		return 0;
    	}else{
    		CustomerBalance cc = new CustomerBalance();
    		cc.setCustomerId(fromCustomerId);
    		return customerReceiveInfoMapper.mergeCustomer(fromCustomerId,toCustomerId);
    	}
    }

}
