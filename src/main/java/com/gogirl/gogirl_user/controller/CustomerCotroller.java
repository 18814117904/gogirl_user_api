package com.gogirl.gogirl_user.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gogirl.gogirl_user.dao.CustomerDetailMapper;
import com.gogirl.gogirl_user.dao.StoreUserMapper;
import com.gogirl.gogirl_user.entity.Customer;
import com.gogirl.gogirl_user.entity.CustomerDepartmentRelevance;
import com.gogirl.gogirl_user.entity.CustomerDepartmentRelevanceKey;
import com.gogirl.gogirl_user.entity.CustomerDetail;
import com.gogirl.gogirl_user.entity.JsonResult;
import com.gogirl.gogirl_user.service.CustomerDepartmentRelevanceService;
import com.gogirl.gogirl_user.service.CustomerDetailService;
import com.gogirl.gogirl_user.service.CustomerService;

@Controller
public class CustomerCotroller {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Resource
	private CustomerService customerService;
	@Resource
	private CustomerDetailService customerDetailService;
	@Resource
	private CustomerDepartmentRelevanceService customerDepartmentRelevanceService;
//	@ResponseBody
//	@RequestMapping(value="/insertCustomer")
//	public JsonResult insertCustomer(
//			@RequestParam(required=false) String id,
//			@RequestParam(required=false)String openid,
//			@RequestParam(required=false)String phone,
//			@RequestParam(required=false)String nickname,
//			@RequestParam(required=false)String password,
//			@RequestParam(required=false)String sex,
//			@RequestParam(required=false)String country,
//			@RequestParam(required=false)String province,
//			@RequestParam(required=false)String city,
//			@RequestParam(required=false)String headimgurl,
//			@RequestParam(required=false)String privilege,
//			@RequestParam(required=false)String state,
//			@RequestParam(required=false)Customer customer
//			) {
//		//入参检查
//		Customer customerResult = new Customer();
//		if(openid    !=null){
//		customerResult.setOpenid(openid);        
//		}
//		if(phone     !=null){
//		customerResult.setPhone(phone);          
//		}
//		if(nickname  !=null){
//			customerResult.setNickname(nickname);
//		}
//		if(password  !=null){
//		customerResult.setPassword(password);    
//		}
//		if(sex       !=null){
//		customerResult.setSex(sex);              
//		}
//		if(country   !=null){
//		customerResult.setCountry(country);      
//		}
//		if(province  !=null){
//		customerResult.setProvince(province);    
//		}
//		if(city      !=null){
//		customerResult.setCity(city);            
//		}
//		if(headimgurl!=null){
//		customerResult.setHeadimgurl(headimgurl);
//		}
//		if(privilege !=null){
//		customerResult.setPrivilege(privilege);  
//		}
//		if(state     !=null){
//		customerResult.setState(state);          
//		}
//		customerResult.setUpdateTime(new Date());          
//		
//		
//		int result = customerService.insert(customerResult);
//		Map<String, Object> map = new HashMap<>();
//		map.put("id", result);
//		return new JsonResult(true, "", map);
//	}
	@ResponseBody
	@RequestMapping(value="/insertCustomer")
	public JsonResult insertCustomer(Customer customer) {
		//入参检查
		customer.setRegisterTime(new Date());
		customer.setUpdateTime(new Date());
		int result = customerService.insert(customer);
		Map<String, Object> map = new HashMap<>();
		map.put("id", result);
		return new JsonResult(true, "", map);
	}
	@ResponseBody
	@RequestMapping(value="/insertOrUpdateCustomer")//订单系统和预约系统调用
	public JsonResult insertOrUpdateCustomer(Customer customer,Integer oldId,String openOrderUser) {
		//入参处理
		if(customer!=null&&customer.getStoreRecordRealName()!=null&&customer.getStoreRecordRealName().isEmpty()){
			customer.setStoreRecordRealName(null);
		}
		//入参处理
		Customer queryCustomer = null;
		if(customer!=null&&customer.getId()!=null){
			queryCustomer = customerService.selectByPrimaryKey(customer.getId());
		}else if(customer!=null&&customer.getPhone()!=null){
			if(customer.getPhone().equals("无")){
				customer.setPhone(null);
			}else{
				queryCustomer = customerService.selectByPhone(customer.getPhone());
			}
		}
//		else if(customer!=null&&customer.getSource()!=null&&customer.getSource()==3){
//			return new JsonResult(false,"微信预约或订单未传入id或电话号码。",null);
//		}
//		else if((customer!=null&&customer.getSource()!=null&&customer.getSource()==1)||(customer!=null&&customer.getSource()==2)){
//			return new JsonResult(false,"店铺预约或订单未传入id或电话号码。",null);
//		}
		else{
			//其他来源用户，默认插入
		}
		if(queryCustomer!=null){
			if((oldId==null||!oldId.equals(queryCustomer.getId()))&&customer.getSource()!=null&&(customer.getSource()==1||customer.getSource()==3)){
				customer.setScheduledTimes(queryCustomer.getScheduledTimes()+1);
			}
			if((oldId==null||!oldId.equals(queryCustomer.getId()))&&customer.getSource()!=null&&customer.getSource()==2){
				customer.setOrderTimes(queryCustomer.getOrderTimes()+1);
			}
			//插入店铺关联
			if(queryCustomer.getId()!=null&&customer.getRegisterDepartment()!=null){
				insertDepartmentRelevanceIfNotExist(queryCustomer.getId() ,customer.getRegisterDepartment(),customer.getSource(),new Date());
			}
			customer.setRegisterDepartment(null);//除了第一次插入，修改一律不修改注册店铺
			customer.setSource(null);//除了第一次插入，修改一律不修改source
			customer.setId(queryCustomer.getId());
			if((queryCustomer.getResponsiblePerson()==null||queryCustomer.getResponsiblePerson().isEmpty())&&openOrderUser!=null&&!openOrderUser.isEmpty()){//设置开单人
				customer.setResponsiblePerson(openOrderUser);
			}
			int result = customerService.updateByPrimaryKeySelective(customer);
			Map<String, Object> map = new HashMap<>();
			map.put("id", queryCustomer.getId());
			map.put("openid", queryCustomer.getOpenid());
			return new JsonResult(true, "", map);			
		}else{
			if(customer.getSource()!=null&&customer.getSource()==1){
				customer.setScheduledTimes(1);
			}
			if(customer.getSource()!=null&&customer.getSource()==2){
				customer.setOrderTimes(1);
			}
			if(customer==null||customer.getStoreRecordRealName()==null||customer.getStoreRecordRealName().equals("")){
				customer.setStoreRecordRealName("无");
			}
			if(openOrderUser!=null&&!openOrderUser.isEmpty()){//设置开单人
				customer.setResponsiblePerson(openOrderUser);
			}
			int result = customerService.insertSelective(customer);
			//插入店铺关联
			if(result!=0&&customer.getRegisterDepartment()!=null){
				insertDepartmentRelevanceIfNotExist(result ,customer.getRegisterDepartment(),customer.getSource(),new Date());
			}
			Map<String, Object> map = new HashMap<>();
			map.put("id", result);
			map.put("openid", null);
			return new JsonResult(true, "", map);
		}
	}
	//增加订单次数
	@ResponseBody
	@RequestMapping(value="/addOrderTimes")
	public JsonResult addOrderTimes(Integer customerId,String phone,String openOrderUser) {
		Customer queryCustomer = null;
		if(customerId!=null){
			queryCustomer = customerService.selectByPrimaryKey(customerId);
		}else if(phone!=null){
			queryCustomer = customerService.selectByPhone(phone);
		}
		if(queryCustomer==null){
			return new JsonResult(false,"无该用户",null);
		}else{
			if((queryCustomer.getResponsiblePerson()==null||queryCustomer.getResponsiblePerson().isEmpty())&&openOrderUser!=null&&!openOrderUser.isEmpty()){//设置开单人
				queryCustomer.setResponsiblePerson(openOrderUser);
			}
			queryCustomer.setOrderTimes(queryCustomer.getOrderTimes()+1);
		}
		int row = customerService.updateByPrimaryKeySelective(queryCustomer);
		return new JsonResult(true,JsonResult.APP_DEFINE_SUC,row);
	}
	//
	@ResponseBody
	@RequestMapping(value="/minusOrderTimes")
	public JsonResult minusOrderTimes(Integer customerId,String phone) {
		Customer queryCustomer = null;
		if(customerId!=null){
			queryCustomer = customerService.selectByPrimaryKey(customerId);
		}else if(phone!=null){
			queryCustomer = customerService.selectByPhone(phone);
		}
		int row  = 0;
		if(queryCustomer==null){
			return new JsonResult(false,"无该用户",null);
		}else if(queryCustomer.getOrderTimes()!=null&&queryCustomer.getOrderTimes()>0){
			queryCustomer.setOrderTimes(queryCustomer.getOrderTimes()-1);
			row= customerService.updateByPrimaryKeySelective(queryCustomer);
		}
		return new JsonResult(true,JsonResult.APP_DEFINE_SUC,row);
	}
	@ResponseBody
	@RequestMapping(value="/minusScheduledTimes")
	public JsonResult minusScheduledTimes(Integer customerId,String phone) {
		Customer queryCustomer = null;
		if(customerId!=null){
			queryCustomer = customerService.selectByPrimaryKey(customerId);
		}else if(phone!=null){
			queryCustomer = customerService.selectByPhone(phone);
		}
		int row  = 0;
		if(queryCustomer==null){
			return new JsonResult(false,"无该用户",null);
		}else if(queryCustomer.getScheduledTimes()!=null&&queryCustomer.getScheduledTimes()>0){
			queryCustomer.setScheduledTimes(queryCustomer.getScheduledTimes()-1);
			row= customerService.updateByPrimaryKeySelective(queryCustomer);
		}
		return new JsonResult(true,JsonResult.APP_DEFINE_SUC,row);
	}
		
	private void insertDepartmentRelevanceIfNotExist(Integer customerId ,Integer departmentId,Integer source,Date relevanceTime) {
		CustomerDepartmentRelevanceKey key = new CustomerDepartmentRelevanceKey();
		key.setCustomerId(customerId);
		key.setDepartmentId(departmentId);
		CustomerDepartmentRelevance re = customerDepartmentRelevanceService.selectByPrimaryKey(key);
		if(re!=null){//不做更新
			
		}else{//插入
			CustomerDepartmentRelevance record = new CustomerDepartmentRelevance();
			record.setCustomerId(customerId);
			record.setDepartmentId(departmentId);
			record.setRelevanceSource(source);
			record.setRelevanceTime(relevanceTime);
			customerDepartmentRelevanceService.insertSelective(record);
		}
	}
	//电话绑定处用到,现在想规范
	@ResponseBody
	@RequestMapping("/mergeRemoveCustomer/{id}")
	public JsonResult mergeRemoveCustomer(@PathVariable(value = "id") String id) {
		//入参检查
		logger.info("合并删除用户id:"+id);
		if(!isInteger(id)){
			return new JsonResult(false, "id格式不正确", null);
		}
		Map<String, Object> map = new HashMap<>();
		map.put("row", customerService.deleteByPrimaryKey(Integer.parseInt(id)));
		return new JsonResult(true, "", map);
	}
	@ResponseBody
	@RequestMapping("/mergeDeleteCustomerByPhone/{phone}")
	public JsonResult mergeDeleteCustomerByPhone(@PathVariable(value = "phone") String phone) {
		//入参检查
		logger.info("合并删除用户phone:"+phone);
		Map<String, Object> map = new HashMap<>();
		map.put("row", customerService.deleteByPhone(phone));
		return new JsonResult(true, "", map);
	}
	
//	@ResponseBody
//	@RequestMapping("/deleteCustomerByPrimaryKey/{id}")
//	public JsonResult deleteCustomerByPrimaryKey(@PathVariable(value = "id") String id) {
//		//入参检查
//		logger.info("获取用户信息id:"+id);
//		Customer customer = new Customer();
//		customer.setId(Integer.parseInt(id));
//		customer.setState("2");
//		Map<String, Object> map = new HashMap<>();
//		map.put("row", customerService.updateByPrimaryKeySelective(customer));
//		return new JsonResult(true, "", map);
//	}
	

	@ResponseBody
	@RequestMapping(value="/updateCustomerSelective")
	public JsonResult updateCustomerSelective(Customer customer) {
		logger.info("修改用户信息："+customer.toString());
		if(customer==null||customer.getId()==null||customer.getId().equals("0")){
			return new JsonResult(false,"id不能为空",null);
		}
		if(customer!=null&&customer.getPhone()!=null){
			Customer c = customerService.selectByPhone(customer.getPhone());
			if(c!=null&&c.getOpenid()!=null&&!c.getOpenid().isEmpty()&&!c.getId().equals(customer.getId())){
				logger.info("id是否相等:"+(c.getId().equals(customer.getId())));
				return new  JsonResult(false,JsonResult.APP_PHONE_BE_BIND_ERR,null);
			}
		}
		int result = customerService.updateByPrimaryKeySelective(customer);
		Map<String, Object> map = new HashMap<>();
		map.put("row", result);
		return new JsonResult(true, "修改成功!", map);
	}
	/*updateCustomer该方法已经被弃用,随时可能删除*/
	@ResponseBody
	@RequestMapping("/updateCustomer")
	public JsonResult updateCustomer(Customer customer) {
		logger.info("修改用户信息："+customer.toString());
		if(customer==null||customer.getId()==null||customer.getId().equals("0")){
			return new JsonResult(true,"id不能为空",null);
		}
		if(customer!=null&&customer.getPhone()!=null){
			Customer c = customerService.selectByPhone(customer.getPhone());
			if(c!=null&&c.getOpenid()!=null&&!c.getOpenid().isEmpty()&&!c.getId().equals(customer.getId())){
				logger.info("id是否相等:"+(c.getId().equals(customer.getId())));
				return new  JsonResult(false,JsonResult.APP_PHONE_BE_BIND_ERR,null);
			}
		}
		//入参检查
		int result = customerService.updateByPrimaryKey(customer);
		Map<String, Object> map = new HashMap<>();
		map.put("row", result);
		return new JsonResult(true, "修改成功!", map);
	}

	@ResponseBody
	@RequestMapping("/getCustomerByPrimaryKey/{id}")
	public JsonResult getCustomerByPrimaryKey(@PathVariable(value = "id") String id) {
		//入参检查
		logger.info("获取用户信息id:"+id);
		Map<String, Object> map = new HashMap<>();
		Customer customer = customerService.selectByPrimaryKey(Integer.parseInt(id));
		map.put("user", customer);
		return new JsonResult(true, "", map);
	}
	@ResponseBody
	@RequestMapping("/getCustomerByPrimaryKey2")
	public JsonResult getCustomerByPrimaryKey2(String id) {
		//入参检查
		logger.info("获取用户信息id:"+id);
		Map<String, Object> map = new HashMap<>();
		Customer customer = customerService.selectByPrimaryKey(Integer.parseInt(id));
		map.put("user", customer);
		return new JsonResult(true, "", map);
	}
	//TODO 入参的方式实现获得用户
	
	@ResponseBody
	@RequestMapping("/getCustomerByOpenid/{openid}")
	public JsonResult getCustomerByOpenid(@PathVariable(value = "openid") String openid) {
		//入参检查
		logger.info("获取用户信息openid:"+openid);
		Map<String, Object> map = new HashMap<>();
		Customer customer = customerService.selectByOpenid(openid);
		map.put("user",customer );
		return new JsonResult(true, "", map);
	}
	//TODO 入参的方式实现获得用户
	@ResponseBody
	@RequestMapping("/getCustomerByPhone/{phone}")
	public JsonResult getCustomerByPhone(@PathVariable(value = "phone") String phone) {
		//入参检查
		logger.info("获取用户信息phone:"+phone);
		Map<String, Object> map = new HashMap<>();
		Customer customer = customerService.selectByPhone(phone);
		map.put("user", customer);
		return new JsonResult(true, "", map);
	}
	//TODO 入参的方式实现获得用户
	
	@ResponseBody
	@RequestMapping("/getCustomer")
	public JsonResult getCustomer(Customer customer,HttpServletRequest request,HttpServletResponse response) {
        String origin = request.getHeader("Origin");
        response.setHeader("Access-Control-Allow-Origin", origin);
        response.setHeader("Access-Control-Allow-Credentials", "true");
		//入参检查
        if(customer!=null&&customer.getRealName()!=null&&customer.getRealName().equals("")){
        	customer.setRealName(null);
        }
        if(customer!=null&&customer.getPhone()!=null&&customer.getPhone().equals("")){
        	customer.setPhone(null);
        }
		logger.info("获取用户信息，查询条件:"+customer.toString());
		Map<String, Object> map = new HashMap<>();
		List<Customer> list = customerService.selectByCustomer(customer);
		map.put("users", list);
		return new JsonResult(true, "", map);
	}
	//查询会员信息，带店铺
	@ResponseBody
	@RequestMapping("/selectByCustomerWithStore")
	public JsonResult selectByCustomerWithStore(Integer pageNum,Integer pageSize,Customer customer,Integer departmentId,HttpServletRequest request,HttpServletResponse response) {
        String origin = request.getHeader("Origin");
        response.setHeader("Access-Control-Allow-Origin", origin);
        response.setHeader("Access-Control-Allow-Credentials", "true");
		//入参检查
        if(customer!=null&&customer.getRealName()!=null&&customer.getRealName().equals("")){
        	customer.setRealName(null);
        }
        if(customer!=null&&customer.getPhone()!=null&&customer.getPhone().equals("")){
        	customer.setPhone(null);
        }
        PageHelper.startPage(pageNum,pageSize);
		logger.info("获取用户信息，查询条件:"+customer.toString());
//		Map<String, Object> map = new HashMap<>();
		List<Customer> list = customerService.selectByCustomerWithStore(customer,departmentId);
		PageInfo<Customer> pageInfo = new PageInfo<Customer>(list);
//		map.put("users", pageInfo);
		return new JsonResult(true, "", pageInfo);
	}
	//查询会员信息带店铺且带会员卡
	@ResponseBody
	@RequestMapping("/selectByCustomerWithStoreAndCard")
	public JsonResult selectByCustomerWithStoreAndCard(String pageNum,String pageSize,Customer customer,Integer departmentId,HttpServletRequest request,HttpServletResponse response) {
        String origin = request.getHeader("Origin");
        response.setHeader("Access-Control-Allow-Origin", origin);
        response.setHeader("Access-Control-Allow-Credentials", "true");
		//入参检查
        if(customer!=null&&customer.getRealName()!=null&&customer.getRealName().equals("")){
        	customer.setRealName(null);
        }
        if(customer!=null&&customer.getPhone()!=null&&customer.getPhone().equals("")){
        	customer.setPhone(null);
        }
        
        PageHelper.startPage(Integer.parseInt(pageNum),Integer.parseInt(pageSize));
		logger.info("获取用户信息，查询条件:"+customer.toString());
//		Map<String, Object> map = new HashMap<>();
		List<Customer> list = customerService.selectByCustomerWithStoreAndCard(customer,departmentId);
		PageInfo<Customer> pageInfo = new PageInfo<Customer>(list);
//		map.put("users", pageInfo);
		return new JsonResult(true, "", pageInfo);
	}
	//查询会员信息带店铺且带会员卡
	@ResponseBody
	@RequestMapping("/selectByCustomerCard")
	public JsonResult selectByCustomerCard(String pageNum,String pageSize,Customer customer,Integer departmentId,HttpServletRequest request,HttpServletResponse response) {
        String origin = request.getHeader("Origin");
        response.setHeader("Access-Control-Allow-Origin", origin);
        response.setHeader("Access-Control-Allow-Credentials", "true");
		//入参检查
        if(customer!=null&&customer.getRealName()!=null&&customer.getRealName().equals("")){
        	customer.setRealName(null);
        }
        if(customer!=null&&customer.getPhone()!=null&&customer.getPhone().equals("")){
        	customer.setPhone(null);
        }
        PageHelper.startPage(Integer.parseInt(pageNum),Integer.parseInt(pageSize));
		logger.info("获取用户信息，查询条件:"+customer.toString());
//		Map<String, Object> map = new HashMap<>();
		List<Customer> list = customerService.selectByCustomerCard(customer,departmentId);
		
		//查询美甲师名字,设置到id
		
		
		int listSize = list.size();
		for(int i=0;i<listSize;i++){
			Customer c = list.get(i);
			
		}
		PageInfo<Customer> pageInfo = new PageInfo<Customer>(list);
//		map.put("users", pageInfo);
		return new JsonResult(true, "", pageInfo);
	}
	//查询会员信息带店铺且带会员卡
	@ResponseBody
	@RequestMapping("/selectByCustomerWithStoreAndCardAndDetail")
	public JsonResult selectByCustomerWithStoreAndCardAndDetail(String isNotRecord ,String pageNum,String pageSize,Customer customer,Integer departmentId,HttpServletRequest request,HttpServletResponse response) {
        String origin = request.getHeader("Origin");
        response.setHeader("Access-Control-Allow-Origin", origin);
        response.setHeader("Access-Control-Allow-Credentials", "true");
		//入参检查
        if(customer!=null&&customer.getRealName()!=null&&customer.getRealName().equals("")){
        	customer.setRealName(null);
        }
        if(customer!=null&&customer.getPhone()!=null&&customer.getPhone().equals("")){
        	customer.setPhone(null);
        }
        PageHelper.startPage(Integer.parseInt(pageNum),Integer.parseInt(pageSize));
		logger.info("获取用户信息，查询条件:"+customer.toString());
//		Map<String, Object> map = new HashMap<>();
		boolean isNotRecordB = false;
		if(isNotRecord!=null&&isNotRecord.equals("true")){
			isNotRecordB= true;
		}
		List<Customer> list = customerService.selectByCustomerWithStoreAndCardAndDetail(customer,departmentId,isNotRecordB);
		PageInfo<Customer> pageInfo = new PageInfo<Customer>(list);
//		map.put("users", pageInfo);
		return new JsonResult(true, "", pageInfo);
	}
	
	@ResponseBody
	@RequestMapping("/getCustomerWithPhone")
	public JsonResult getCustomerWithPhone(Customer customer,HttpServletRequest request,HttpServletResponse response) {
        String origin = request.getHeader("Origin");
        response.setHeader("Access-Control-Allow-Origin", origin);
        response.setHeader("Access-Control-Allow-Credentials", "true");
		//入参检查
		Map<String, Object> map = new HashMap<>();
		String phone = null;
		if(customer!=null){
			logger.info("获取用户信息，查询条件:"+customer.toString());
			phone = customer.getPhone();
		}
		List<Customer> list = customerService.selectCustomerWithPhone(phone);
		map.put("users", list);
		return new JsonResult(true, "", map);
	}
	public boolean isInteger(String str) {
		Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");  
		return pattern.matcher(str).matches();  
	}
	@ResponseBody
	@RequestMapping("/insertOrUpdateCustomerDetail")
	public JsonResult insertOrUpdateCustomerDetail(CustomerDetail record){
		if(record==null||record.getCustomerId()==null){
			return new JsonResult(false,String.format(JsonResult.APP_DEFINE_PARAM_ERR,"customerId"),null);
		}
		CustomerDetail cd= customerDetailService.selectByPrimaryKey(record.getCustomerId());
		if(cd==null){
			return new JsonResult(true,JsonResult.APP_DEFINE_SUC,customerDetailService.insertSelective(record));
		}else{
			return new JsonResult(true,JsonResult.APP_DEFINE_SUC,customerDetailService.updateByPrimaryKeySelective(record));
		}
	}
	@ResponseBody
	@RequestMapping("/getCustomerDetailById")
	public JsonResult getCustomerDetailById(Integer customerId){
		if(customerId==null){
			return new JsonResult(false,String.format(JsonResult.APP_DEFINE_PARAM_ERR,"customerId"),null);
		}
		return new JsonResult(true,JsonResult.APP_DEFINE_SUC,customerDetailService.selectByPrimaryKey(customerId));
	}
//	@ResponseBody
//	@RequestMapping("/updateCustomerDetail")
//	public JsonResult updateByPrimaryKeySelective(CustomerDetail record){
//		if(record==null||record.getCustomerId()==null){
//			return new JsonResult(false,String.format(JsonResult.APP_DEFINE_PARAM_ERR,"customerId"),null);
//		}
//		return new JsonResult(true,JsonResult.APP_DEFINE_SUC,customerDetailService.updateByPrimaryKeySelective(record));
//	}
	@ResponseBody
	@RequestMapping("/updateRemark")
	public JsonResult updateRemark(Integer customerId,String remark,String remark2,String remark3,String remark4){
		if(customerId==null){
			return new JsonResult(false,String.format(JsonResult.APP_DEFINE_PARAM_ERR,"customerId"),null);
		}
		Customer customer = new Customer();
		customer.setId(customerId);
		customer.setRemark(remark);
		customer.setRemark2(remark2);
		customer.setRemark3(remark3);
		customer.setRemark4(remark4);
		return new JsonResult(true,JsonResult.APP_DEFINE_SUC,customerService.updateRemark(customer));
	}
	
}
