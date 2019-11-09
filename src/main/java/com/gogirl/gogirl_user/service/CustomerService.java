package com.gogirl.gogirl_user.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.catalina.Store;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gogirl.gogirl_user.dao.CustomerDetailMapper;
import com.gogirl.gogirl_user.dao.CustomerMapper;
import com.gogirl.gogirl_user.dao.StoreUserMapper;
import com.gogirl.gogirl_user.entity.Customer;
import com.gogirl.gogirl_user.entity.CustomerBalanceRecord;
import com.gogirl.gogirl_user.entity.CustomerDetail;
import com.gogirl.gogirl_user.entity.StoreUser;
import com.gogirl.gogirl_user.util.CountCustomerRate;
@Service
public class CustomerService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Resource
	CustomerMapper customerMapper;
	@Resource
	CustomerDetailMapper customerDetailMapper;
	@Resource
	CustomerBalanceService customerBalanceService;
	@Resource
	CustomerWeiboService customerWeiboService;
	@Resource
	CustomerDepartmentRelevanceService customerDepartmentRelevanceService;
	@Resource
	ActivityService activityService;

	
	
	public int insert(Customer record){
		if(record!=null&&record.getState()==null){
			record.setState(String.valueOf(1));
		}
		if(record!=null&&record.getRegisterTime()==null){
			record.setRegisterTime(new Date());
		}
		if(record!=null&&record.getUpdateTime()==null){
			record.setUpdateTime(new Date());
		}
		if(record!=null&&record.getScheduledTimes()==null){
			record.setScheduledTimes(0);
		}
		if(record!=null&&record.getOrderTimes()==null){
			record.setOrderTimes(0);
		}
		customerMapper.insert(record);
		return record.getId();
    }

	public int insertSelective(Customer record){
		if(record!=null&&record.getState()==null){
			record.setState(String.valueOf(1));
		}
		if(record!=null&&record.getRegisterTime()==null){
			record.setRegisterTime(new Date());
		}
		if(record!=null&&record.getUpdateTime()==null){
			record.setUpdateTime(new Date());
		}
		if(record!=null&&record.getScheduledTimes()==null){
			record.setScheduledTimes(0);
		}
		if(record!=null&&record.getOrderTimes()==null){
			record.setOrderTimes(0);
		}
		customerMapper.insertSelective(record);
		return record.getId();
    }

	public int deleteByPrimaryKey(Integer id){
		return customerMapper.deleteByPrimaryKey(id);
    }
	public int deleteByPhone(String phone) {
		
		return customerMapper.deleteByPhone(phone);
	}
	
    public int updateByPrimaryKeySelective(Customer record){
		logger.info("更新用户信息record:"+record.toString());
    	if(record.getId()==null||record.getId()==0){
    		return 0;
    	}
    	if(record.getPhone()!=null&&!record.getPhone().isEmpty()){
    		Customer qc = customerMapper.selectByPhone(record.getPhone());
    		if(qc!=null&&!qc.getId().equals(record.getId())){
        		logger.info("查到电话用户信息为:"+qc.toString());
    			Customer qcustomer = customerMapper.selectByPrimaryKey(record.getId());
    			record = setCustomerData(record,qcustomer);//先填充微信数据
    			qc = setCustomerData(record,qc);//再填充店铺端新建数据
    			customerMapper.deleteByPrimaryKey(record.getId());
    			logger.info("保留电话用户,合并删除微信用户");
    			setCustomerLike(record.getId(), qc.getId());
    			record = qc;
    		}
    	}
		if(record!=null&&record.getUpdateTime()==null){
			record.setUpdateTime(new Date());
		}
		
		if(record.getCustomerSource()!=null&&record.getCustomerSource()!=0){//如果source变了,修改资料完整度
	    	CustomerDetail customerDetailQ = customerDetailMapper.selectByPrimaryKey(record.getId());
	    	record.setDataIntegrity(new CountCustomerRate().countDataCompleteRate(record,customerDetailQ));
		}
		return customerMapper.updateByPrimaryKeySelective(record);
    }

	public int updateByPrimaryKey(Customer record){
		logger.info("更新用户信息record:"+record.toString());
    	if(record.getId()==null||record.getId()==0){
    		return 0;
    	}
    	if(record.getPhone()!=null&&!record.getPhone().isEmpty()){
    		Customer qc = customerMapper.selectByPhone(record.getPhone());
    		logger.info("查到电话用户信息为:"+qc.toString());
    		if(qc.getId()!=record.getId()){
    			//更新微信用户的所有字段到电话用户
    			Customer qcustomer = customerMapper.selectByPrimaryKey(record.getId());
    			record = setCustomerData(record,qcustomer);//先填充微信数据
    			qc = setCustomerData(record,qc);//再填充店铺端新建数据
    			customerMapper.deleteByPrimaryKey(record.getId());
    			logger.info("保留电话用户,合并删除微信用户");
    			setCustomerLike(record.getId(), qc.getId());
    			record = qc;
    		}
    	}
		if(record.getCustomerSource()!=null&&record.getCustomerSource()!=0){//如果source变了,修改资料完整度
	    	CustomerDetail customerDetailQ = customerDetailMapper.selectByPrimaryKey(record.getId());
	    	record.setDataIntegrity(new CountCustomerRate().countDataCompleteRate(record,customerDetailQ));
		}
		if(record!=null&&record.getUpdateTime()==null){//更新时间
			record.setUpdateTime(new Date());
		}
		return customerMapper.updateByPrimaryKeySelective(record);
    }
    private void setCustomerLike(int fromId , int toId) {
		//设置余额表id
		logger.info("调用customerBalanceService.mergeCustomer修改余额信息");
    	customerBalanceService.mergeCustomer(fromId, toId);
    	//设置微博数据
		logger.info("调用customerBalanceService.mergeCustomer修改微博信息");
    	customerWeiboService.mergeCustomer(fromId, toId);
    	//设置店铺关联关系
		logger.info("调用customerBalanceService.mergeCustomer修改店铺关联信息");
    	customerDepartmentRelevanceService.mergeCustomer(fromId, toId);
    	//设置活动关联
		logger.info("调用activityService.mergeCustomer修改活动关联");
		activityService.mergeCustomer(fromId, toId);
	}


    
    private Customer setCustomerData(Customer fromCustomer,Customer toCustomer) {
    	if(fromCustomer.getOpenid             ()!=null){toCustomer.setOpenid             (fromCustomer.getOpenid             ());}
    	if(fromCustomer.getPhone              ()!=null){toCustomer.setPhone              (fromCustomer.getPhone              ());}
    	if(fromCustomer.getNickname           ()!=null){toCustomer.setNickname           (fromCustomer.getNickname           ());}
    	if(fromCustomer.getPassword           ()!=null){toCustomer.setPassword           (fromCustomer.getPassword           ());}
    	if(fromCustomer.getSex                ()!=null){toCustomer.setSex                (fromCustomer.getSex                ());}
    	if(fromCustomer.getCountry            ()!=null){toCustomer.setCountry            (fromCustomer.getCountry            ());}
    	if(fromCustomer.getProvince           ()!=null){toCustomer.setProvince           (fromCustomer.getProvince           ());}
    	if(fromCustomer.getCity               ()!=null){toCustomer.setCity               (fromCustomer.getCity               ());}
    	if(fromCustomer.getHeadimgurl         ()!=null){toCustomer.setHeadimgurl         (fromCustomer.getHeadimgurl         ());}
    	if(fromCustomer.getPrivilege          ()!=null){toCustomer.setPrivilege          (fromCustomer.getPrivilege          ());}
    	if(fromCustomer.getState              ()!=null){toCustomer.setState              (fromCustomer.getState              ());}
    	if(fromCustomer.getRegisterTime       ()!=null){toCustomer.setRegisterTime       (fromCustomer.getRegisterTime       ());}
    	if(fromCustomer.getUpdateTime         ()!=null){toCustomer.setUpdateTime         (fromCustomer.getUpdateTime         ());}
    	if(fromCustomer.getRealName           ()!=null){toCustomer.setRealName           (fromCustomer.getRealName           ());}
    	if(fromCustomer.getBirthday           ()!=null){toCustomer.setBirthday           (fromCustomer.getBirthday           ());}
    	if(fromCustomer.getRegisterDepartment ()!=null){toCustomer.setRegisterDepartment (fromCustomer.getRegisterDepartment ());}
    	if(fromCustomer.getStoreRecordRealName()!=null){toCustomer.setStoreRecordRealName(fromCustomer.getStoreRecordRealName());}
    	if(fromCustomer.getRemark             ()!=null){toCustomer.setRemark             (fromCustomer.getRemark             ());}
    	if(fromCustomer.getSource             ()!=null){toCustomer.setSource             (fromCustomer.getSource             ());}
    	if(fromCustomer.getScheduledTimes     ()!=null){toCustomer.setScheduledTimes     (fromCustomer.getScheduledTimes     ());}
    	if(fromCustomer.getOrderTimes         ()!=null){toCustomer.setOrderTimes         (fromCustomer.getOrderTimes         ());}
		
		return toCustomer;
	}

	public Customer selectByPrimaryKey(Integer id){
		return customerMapper.selectByPrimaryKey(id);
    }

	public Customer selectByOpenid(String openid) {
		return customerMapper.selectByOpenid(openid);
	}

	public Customer selectByPhone(String phone) {
		return customerMapper.selectByPhone(phone);
	}
	
	public List<Customer> selectByCustomer(Customer customer) {
		return customerMapper.selectByCustomer(customer);
	}
	public List<Customer> selectByCustomerWithStore(Customer customer,Integer departmentId) {
		return customerMapper.selectByCustomerWithStore(customer,departmentId);
	}
	public List<Customer> selectByCustomerWithStoreAndCard(Customer customer,Integer departmentId) {
		return customerMapper.selectByCustomerWithStoreAndCard(customer,departmentId);
	}
	public List<Customer> selectByCustomerCard(Customer customer,Integer departmentId) {
		return customerMapper.selectByCustomerCard(customer,departmentId);
	}
	public List<Customer> selectByCustomerWithStoreAndCardAndDetail(Customer customer,Integer departmentId,boolean isNotRecord) {
		return customerMapper.selectByCustomerWithStoreAndCardAndDetail(customer,departmentId,isNotRecord);
	}

	public List<Customer> selectCustomerWithPhone(String phone) {
		return customerMapper.selectCustomerWithPhone(phone);
	}

	public int checkActiveIsEnd() {
		return customerMapper.checkActiveIsEnd();
	}

	public List<Customer> getJoinActiveCustomer(String phone) {
		return customerMapper.getJoinActiveCustomer(phone);
	}

	public int updateRemark(Customer customer) {
		return customerMapper.updateByPrimaryKeySelective(customer);
	}

    public List<Customer> getGroupCustomer(Integer havePhone,Integer orderTimes,Integer haveBalance){
      	return customerMapper.getGroupCustomer(havePhone,orderTimes,haveBalance);
    }

}
