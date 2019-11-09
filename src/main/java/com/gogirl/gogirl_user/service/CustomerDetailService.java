package com.gogirl.gogirl_user.service;
import java.math.BigDecimal;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gogirl.gogirl_user.dao.CustomerDetailMapper;
import com.gogirl.gogirl_user.dao.CustomerMapper;
import com.gogirl.gogirl_user.entity.Customer;
import com.gogirl.gogirl_user.entity.CustomerDetail;
import com.gogirl.gogirl_user.util.CountCustomerRate;

@Service
public class CustomerDetailService {
	@Resource
	CustomerDetailMapper customerDetailDao;
	@Resource
	CustomerMapper customerDao;

    public int insertSelective(CustomerDetail record){
    	Customer customerQ = customerDao.selectByPrimaryKey(record.getCustomerId());

    	Customer c = new Customer();
    	c.setId(record.getCustomerId());
    	c.setDataIntegrity(new CountCustomerRate().countDataCompleteRate(customerQ,record));
    	customerDao.updateByPrimaryKeySelective(c);
    	return customerDetailDao.insertSelective(record);
    }

    public CustomerDetail selectByPrimaryKey(Integer customerId){
    	
    	return customerDetailDao.selectByPrimaryKey(customerId);
    }

    public int updateByPrimaryKeySelective(CustomerDetail record){
    	Customer customerQ = customerDao.selectByPrimaryKey(record.getCustomerId());
    	
    	Customer c = new Customer();
    	c.setId(record.getCustomerId());
    	c.setDataIntegrity(new CountCustomerRate().countDataCompleteRate(customerQ,record));
    	customerDao.updateByPrimaryKeySelective(c);
    	return customerDetailDao.updateByPrimaryKeySelective(record);
    }

}
