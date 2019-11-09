package com.gogirl.gogirl_user.service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gogirl.gogirl_user.dao.CustomerWeiboMapper;
import com.gogirl.gogirl_user.entity.CustomerBalance;
import com.gogirl.gogirl_user.entity.CustomerWeibo;

@Service
public class CustomerWeiboService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	CustomerWeiboMapper customerWeiboMapper;

    
    
    public int insertSelective(CustomerWeibo record){
    	return customerWeiboMapper.insertSelective(record);
    }

    public CustomerWeibo selectByPrimaryKey(Integer customerId){
    	
    	return customerWeiboMapper.selectByPrimaryKey(customerId);
    }

    public int updateByPrimaryKeySelective(CustomerWeibo record){
    	return customerWeiboMapper.updateByPrimaryKeySelective(record);
    }
    public int  mergeCustomer(int fromCustomerId,int toCustomerId) {
    	CustomerWeibo c = customerWeiboMapper.selectByPrimaryKey(toCustomerId);
    	if(c!=null){
    		logger.error("两目标customerId已经存在.");
    		return 0;
    	}else{
    		CustomerBalance cc = new CustomerBalance();
    		cc.setCustomerId(fromCustomerId);
    		return customerWeiboMapper.mergeCustomer(fromCustomerId,toCustomerId);
    	}
    }

}
