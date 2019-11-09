package com.gogirl.gogirl_user.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.gogirl.gogirl_user.entity.Customer;
@Mapper
public interface CustomerMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Customer record);

    int insertSelective(Customer record);

    Customer selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Customer record);

    int updateByPrimaryKey(Customer record);

	int deleteByPhone(String phone);

	Customer selectByOpenid(String openid);

	Customer selectByPhone(String phone);

	List<Customer> selectByCustomer(Customer customer);
	
//	List<Customer> selectByCustomerWithStore(Customer customer);
	List<Customer> selectByCustomerWithStore(@Param(value = "customer")  Customer customer,@Param("departmentId")Integer departmentId);
	List<Customer> selectByCustomerWithStoreAndCard(@Param(value = "customer")  Customer customer,@Param("departmentId")Integer departmentId);
	List<Customer> selectByCustomerCard(@Param(value = "customer")  Customer customer,@Param("departmentId")Integer departmentId);
	List<Customer> selectByCustomerWithStoreAndCardAndDetail(@Param(value = "customer")  Customer customer,@Param("departmentId")Integer departmentId,@Param("isNotRecord")boolean isNotRecord);
	
	List<Customer> selectCustomerWithPhone(@Param("phone")String phone);

	int checkActiveIsEnd();
	
	List<Customer> getJoinActiveCustomer(String phone);
	List<Customer> getGroupCustomer(Integer havePhone,Integer orderTimes,Integer haveBalance);
}