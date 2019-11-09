package com.gogirl.gogirl_user.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gogirl.gogirl_user.constant.GogirlProperties;
import com.gogirl.gogirl_user.dao.CustomerBalanceRecordMapper;
import com.gogirl.gogirl_user.dao.StoreUserMapper;
import com.gogirl.gogirl_user.entity.CouponCustomerRelevance;
import com.gogirl.gogirl_user.entity.Customer;
import com.gogirl.gogirl_user.entity.CustomerBalance;
import com.gogirl.gogirl_user.entity.CustomerBalanceRecord;
import com.gogirl.gogirl_user.entity.CustomerDepartmentRelevance;
import com.gogirl.gogirl_user.entity.CustomerDepartmentRelevanceKey;
import com.gogirl.gogirl_user.entity.DiscountConfig;
import com.gogirl.gogirl_user.entity.JsonResult;
import com.gogirl.gogirl_user.entity.StoreUser;
import com.gogirl.gogirl_user.service.CouponService;
import com.gogirl.gogirl_user.service.CustomerBalanceService;
import com.gogirl.gogirl_user.service.CustomerDepartmentRelevanceService;
import com.gogirl.gogirl_user.service.CustomerService;
import com.gogirl.gogirl_user.service.DiscountConfigService;
import com.gogirl.gogirl_user.service.RedirectService;
import com.gogirl.gogirl_user.service.balance.ChargeBalance;
import com.gogirl.gogirl_user.service.balance.ChargeBalanceNoBestow;
import com.gogirl.gogirl_user.service.balance.ConsumeBalance;
import com.gogirl.gogirl_user.service.balance.ConsumeWechat;

@Controller
public class BalanceController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Resource
	CustomerService customerService;
	@Autowired
	CustomerBalanceService balanceService;
	@Autowired
	ChargeBalance chargeBalance;
	@Autowired
	ChargeBalanceNoBestow chargeBalanceNoBestow;
	
	@Autowired
	ConsumeBalance consumeBalance;
	@Autowired
	ConsumeWechat consumeWechat;
	@Resource
	DiscountConfigService discountConfigService;
	@Resource
	RedirectService redirectService;
	@Resource
	GogirlProperties gogirlProperties;
	@Autowired
	CustomerBalanceRecordMapper balanceRecordDao;
	@Resource
	StoreUserMapper storeUserMapper;
	@Resource
	CouponService couponService;
	@Resource
	private CustomerDepartmentRelevanceService customerDepartmentRelevanceService;

	//充值余额接口
	@ResponseBody
	@RequestMapping("/withdrawBalance")
	public JsonResult withdrawBalance(String orderId,Integer couponRelevanceId) {
		if(orderId==null){
			return new JsonResult(false,String.format(JsonResult.APP_DEFINE_PARAM_ERR,"orderId"),null);
		}
		JsonResult jsonresult = balanceService.withdrawBalance(orderId);
		if(couponRelevanceId!=0&&jsonresult!=null&&jsonresult.getSuccess()){
			//改优惠券状态
			CouponCustomerRelevance couponCustomerRelevance = new CouponCustomerRelevance();
			couponCustomerRelevance.setId(couponRelevanceId);
			couponCustomerRelevance.setState(1);
			couponService.updateRelevanceByPrimaryKeySelective(couponCustomerRelevance);
		}
		return jsonresult;
	}
	
	//充值余额接口
	@ResponseBody
	@RequestMapping("/chargeBalance")
	public JsonResult chargeBalance(HttpServletRequest request,int customerId,Double orderAmount,Double bestowAmount
			,Integer source,String refereeId,String remark ,Integer departmentId) {
		Customer customer = customerService.selectByPrimaryKey(customerId);
		if(orderAmount==null){
			return new JsonResult(false,String.format(JsonResult.APP_DEFINE_PARAM_ERR,"金额"),null);
		}
		if(customer==null){//新建会员
			return new JsonResult(false,"充值失败,无该用户",null);
		}
			
		//判断有无会员卡
		CustomerBalance card = balanceService.getCustomerBalance2(customer.getId());
//		if(card!=null){//该用户存在会员卡,提示已有会员卡
//			return new JsonResult(true,JsonResult.APP_CUSTOMER_HAVE_CARD,null);
//		}
		if(card==null){
			return new JsonResult(true,"该会员无会员卡,请先开卡后再充值",null);
		}
		//设置余额和订单id
		int a = (int) Math.ceil(orderAmount*100)+(int) Math.ceil(bestowAmount*100);
		String orderId = getOrderIdByTime();
		int row = 0;
		if(orderAmount!=null){
			chargeBalanceNoBestow.setOrderAmount((int) Math.ceil(orderAmount*100));
		}
		if(bestowAmount!=null){
			chargeBalanceNoBestow.setBestowAmount((int) Math.ceil(bestowAmount*100));
		}
		chargeBalanceNoBestow.setSource(source);
		chargeBalanceNoBestow.setReferee(refereeId);
		chargeBalanceNoBestow.setRemark(remark);
		chargeBalanceNoBestow.setType(1); 
		chargeBalanceNoBestow.setDepartmentId(departmentId);
		try {
			row = balanceService.balanceChange(request,customer.getId(), a, chargeBalanceNoBestow, 1,orderId,null);
		} catch (Exception e) {
			logger.info(e.getMessage());
			return new JsonResult(false,e.getMessage(),null);
		}
		if(row<1){
			return new JsonResult(false,"充值失败，请重试",null);
		}
		
		//查询用户信息
		if(customer!=null&&customer.getOpenid()!=null){
			String openid = customer.getOpenid();
			//时间
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日HH时mm分");
			String time = simpleDateFormat.format(new Date());
			//余额和消费金额
			CustomerBalanceRecord customerBalanceRecord = balanceRecordDao.selectByPrimaryKey(chargeBalanceNoBestow.getRecordId());
			if(customerBalanceRecord!=null){
				String realAmount = customerBalanceRecord.getOrderAmount()==null?"0":String.valueOf(customerBalanceRecord.getOrderAmount()/100.00);
				String realBestow = customerBalanceRecord.getBestowAmount()==null?"0":String.valueOf(customerBalanceRecord.getBestowAmount()/100.00);
				String currentBalance = customerBalanceRecord.getCurrentBalance()==null?"0":String.valueOf(customerBalanceRecord.getCurrentBalance()/100.00);
				//姓名
				String customerName = "无名字";
				if(customer.getStoreRecordRealName()!=null&&!customer.getStoreRecordRealName().isEmpty()){
					customerName = customer.getStoreRecordRealName();
				}else if(customer.getRealName()!=null&&!customer.getRealName().isEmpty()){
					customerName = customer.getRealName();
				}else if(customer.getNickname()!=null&&!customer.getNickname().isEmpty()){
					customerName = customer.getNickname();
				}else if(customer.getPhone()!=null&&!customer.getPhone().isEmpty()){
					customerName = customer.getPhone();
				}
				//发送模板消息
//				sendconsumeTemplateMsg(request,openid,storeName,realAmount+"元","无",time,currentBalance);
				sendChargeTemplateMsg(request,openid,time,customerName,"砖石会员卡","充值￥"+realAmount+"，送￥"+realBestow+"","余额￥"+currentBalance+"");
			}
		}
		
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("row", row);
		return new JsonResult(true,"充值成功",map);
	}
	//不送的充值
//	@ResponseBody
//	@RequestMapping("/chargeBalanceNoBestow")
//	public JsonResult chargeBalanceNoBestow(HttpServletRequest request,
//			@RequestParam String openid,
//			@RequestParam int amount,
//			@RequestParam String orderId) {
//		Customer customer = customerService.selectByOpenid(openid);
//		if(customer==null){
//			return new JsonResult(false,JsonResult.APP_OPENID_ERR,null);
//		}
//		
//		int row = 0;
//		if(orderId.isEmpty()){
//			return new JsonResult(false,String.format(JsonResult.APP_DEFINE_PARAM_ERR,"orderId"),null);
//		}
//		try {
//			row = balanceService.balanceChange(customer.getId(), amount, chargeBalanceNoBestow, 1,orderId);
//		} catch (Exception e) {
//			logger.info(e.getMessage());
//			return new JsonResult(false,e.getMessage(),null);
//		}
//		if(row<1){
//			return new JsonResult(false,"充值失败，请重试",null);
//		}
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("row", row);
//		return new JsonResult(true,"",map);
//	}
	//充值余额接口
	@ResponseBody
	@RequestMapping("/chargeBalanceWechat")
	public JsonResult chargeBalanceWechat(HttpServletRequest request,
			@RequestParam String openid,
			@RequestParam int amount,
			@RequestParam String orderId) {
		Customer customer = customerService.selectByOpenid(openid);
		if(customer==null){
			return new JsonResult(false,JsonResult.APP_OPENID_ERR,null);
		}
		
		int row = 0;
		if(orderId.isEmpty()){
			return new JsonResult(false,String.format(JsonResult.APP_DEFINE_PARAM_ERR,"orderId"),null);
		}
		try {
			row = balanceService.balanceChange(request,customer.getId(), amount, chargeBalance, 1,orderId,null);
		} catch (Exception e) {
			logger.info(e.getMessage());
			return new JsonResult(false,e.getMessage(),null);
		}
		if(row<1){
			return new JsonResult(false,"充值失败，请重试",null);
		}
		

		//时间
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日HH时mm分");
		String time = simpleDateFormat.format(new Date());
		//余额和消费金额
		CustomerBalanceRecord customerBalanceRecord = balanceRecordDao.selectByPrimaryKey(chargeBalance.getRecordId());
		if(customerBalanceRecord!=null){
			String realAmount = customerBalanceRecord.getOrderAmount()==null?"0":String.valueOf(customerBalanceRecord.getOrderAmount()/100.00);
			String realBestow = customerBalanceRecord.getBestowAmount()==null?"0":String.valueOf(customerBalanceRecord.getBestowAmount()/100.00);
			String currentBalance = customerBalanceRecord.getCurrentBalance()==null?"0":String.valueOf(customerBalanceRecord.getCurrentBalance()/100.00);
			//姓名
			String customerName = "无名字";
			if(customer.getStoreRecordRealName()!=null&&!customer.getStoreRecordRealName().isEmpty()){
				customerName = customer.getStoreRecordRealName();
			}else if(customer.getRealName()!=null&&!customer.getRealName().isEmpty()){
				customerName = customer.getRealName();
			}else if(customer.getNickname()!=null&&!customer.getNickname().isEmpty()){
				customerName = customer.getNickname();
			}else if(customer.getPhone()!=null&&!customer.getPhone().isEmpty()){
				customerName = customer.getPhone();
			}
			//发送模板消息
			sendChargeTemplateMsg(request,openid,time,customerName,"砖石会员卡","充值￥"+realAmount+"，送￥"+realBestow+"","余额￥"+currentBalance+"");
		}
		
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("row", row);
		return new JsonResult(true,"",map);
	}
	
	//消费后扣费接口customerid
	@ResponseBody
	@RequestMapping("/consumeBalance")
	public JsonResult consumeBalance(HttpServletRequest request,@RequestParam int customerId,@RequestParam int amount,@RequestParam String orderId,Integer couponRelevanceId
			,Integer departmentId) {
		int row = 0;
		consumeBalance.setDepartmentId(departmentId);
		try {
			row = balanceService.balanceChange(request,customerId, amount,consumeBalance , -1,orderId,couponRelevanceId);
		} catch (Exception e) {
			logger.info(e.getMessage());
			return new JsonResult(false,e.getMessage(),null);
		}
		if(row<1){
			return new JsonResult(false,"扣费失败，请重试",null);
		}
		
		//查询用户信息
		Customer customer = customerService.selectByPrimaryKey(customerId);
		if(customer!=null&&customer.getOpenid()!=null){
			String openid = customer.getOpenid();
			//查询店铺名称
			String storeName = getOrderStore(request,orderId);
			//时间
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日HH时mm分");
			String time = simpleDateFormat.format(new Date());
			//余额和消费金额
			CustomerBalanceRecord customerBalanceRecord = balanceRecordDao.selectByPrimaryKey(consumeBalance.getRecordId());
			if(customerBalanceRecord!=null){
				String realAmount = customerBalanceRecord.getOrderAmount()==null?"0.00":String.valueOf(new BigDecimal(customerBalanceRecord.getOrderAmount()/100.00).setScale(2, BigDecimal.ROUND_HALF_UP));
				String currentBalance = customerBalanceRecord.getCurrentBalance()==null?"0":String.valueOf(customerBalanceRecord.getCurrentBalance()/100.00);
				//发送模板消息
				try {
					sendconsumeTemplateMsg(request,openid,storeName,"￥"+realAmount+"元","无",time,currentBalance);
				} catch (Exception e) {
					logger.info("发送模板消息报错:"+e.getMessage());
				}
			}
		}

		
		
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("row", row);
		map.put("discountPrice",consumeBalance.getDiscountPrice());
		return new JsonResult(true,"",map);
	}
	//消费后扣费接口openid
	@ResponseBody
	@RequestMapping("/consumeBalanceOpenid")
	public JsonResult consumeBalanceOpenid(HttpServletRequest request,@RequestParam String openid,@RequestParam int amount,@RequestParam String orderId
			,Integer couponRelevanceId,Integer departmentId) {
		logger.info("使用余额支付"+openid+"  "+amount+"  "+orderId);
		Customer customer = customerService.selectByOpenid(openid);
		if(customer==null){
			return new JsonResult(false,JsonResult.APP_OPENID_ERR,null);
		}
		int row = 0;
		consumeBalance.setDepartmentId(departmentId);
		try {
			row = balanceService.balanceChange(request,customer.getId(), amount,consumeBalance , -1,orderId,couponRelevanceId);
		} catch (Exception e) {
			logger.info(e.getMessage());
			return new JsonResult(false,e.getMessage(),null);
		}
		if(row<1){
			return new JsonResult(false,"扣费失败，请重试",null);
		}
		
		//calculateBalance.getOrderAmount()
		//查询用户信息

			//查询店铺名称
			String storeName = getOrderStore(request,orderId);
			//时间
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日HH时mm分");
			String time = simpleDateFormat.format(new Date());
			//余额和消费金额
			CustomerBalanceRecord customerBalanceRecord = balanceRecordDao.selectByPrimaryKey(consumeBalance.getRecordId());
			if(customerBalanceRecord!=null){
				String realAmount = customerBalanceRecord.getOrderAmount()==null?"0":String.valueOf(customerBalanceRecord.getOrderAmount()/100.00);
				String currentBalance = customerBalanceRecord.getCurrentBalance()==null?"0":String.valueOf(customerBalanceRecord.getCurrentBalance()/100.00);
				//发送模板消息
				try {
					sendconsumeTemplateMsg(request,openid,storeName,"￥"+realAmount+"元","无",time,currentBalance);
				} catch (Exception e) {
					logger.info("发送模板消息报错:"+e.getMessage());
				}
			}

		
		
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("row", row);
		map.put("discountPrice",consumeBalance.getDiscountPrice());
		return new JsonResult(true,"",map);
	}
	//微信支付
	@ResponseBody
	@RequestMapping("/consumeWechat")
	public JsonResult consumeWechat(HttpServletRequest request,@RequestParam String openid,@RequestParam Integer amount,@RequestParam String orderId,Integer couponRelevanceId) {
		logger.info("使用微信支付"+openid+"  "+amount+"  "+orderId);
		Customer customer = customerService.selectByOpenid(openid);
		if(customer==null){
			return new JsonResult(false,JsonResult.APP_OPENID_ERR,null);
		}
		int row = 0;
		try {
			row = balanceService.balanceChange(request,customer.getId(), amount,consumeWechat , -1,orderId,couponRelevanceId);
		} catch (Exception e) {
			logger.info(e.getMessage());
			return new JsonResult(false,e.getMessage(),null);
		}
		if(row<1){
			return new JsonResult(false,"扣费失败，请重试",null);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("row", row);
		map.put("discountPrice",consumeBalance.getDiscountPrice());
		return new JsonResult(true,"",map);
	}
	
	//查看余额接口
	@ResponseBody
	@RequestMapping("/getBalance")
	public JsonResult getBalance(HttpServletRequest request,@RequestParam int customerId) {
		CustomerBalance balance = balanceService.getCustomerBalance(customerId);
		if(balance==null){
			return new JsonResult(false,"查询失败，请重试",null);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("balance", balance);
		DiscountConfig discountConfig = discountConfigService.selectByTotalCharge(balance.getTotalCharge());
		map.put("discountConfig", discountConfig);
		return new JsonResult(true,"",map);
	}
	//查看消费记录
	@ResponseBody
	@RequestMapping("/getBalanceRecord")
	public JsonResult getBalanceRecord(CustomerBalanceRecord customerBalanceRecord,int pageNum,int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
		List<CustomerBalanceRecord> list=balanceService.getBalanceRecord(customerBalanceRecord);
		list = setStoreUser(list);//设置推荐人名字
		PageInfo<CustomerBalanceRecord> pageInfo = new PageInfo<CustomerBalanceRecord>(list);
		return new JsonResult(true,JsonResult.APP_DEFINE_SUC,pageInfo);
	}
	//查看消费记录
	@ResponseBody
	@RequestMapping("/getBalanceRecordCard")
	public JsonResult getBalanceRecordCard(CustomerBalanceRecord customerBalanceRecord,int pageNum,int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
		List<CustomerBalanceRecord> list=balanceService.getBalanceRecordCard(customerBalanceRecord);
		list = setStoreUser(list);//设置推荐人名字
		PageInfo<CustomerBalanceRecord> pageInfo = new PageInfo<CustomerBalanceRecord>(list);
		return new JsonResult(true,JsonResult.APP_DEFINE_SUC,pageInfo);
	}
	//总后台开卡
	@ResponseBody
	@RequestMapping("/newCustomerCard")
	public JsonResult newCustomerCard(HttpServletRequest request,String name,String phone ,Double amount,Double orderAmount,Double bestowAmount,Integer source,Integer type,String refereeId,String remark,Integer departmentId) {
		Customer customer = customerService.selectByPhone(phone);
		if(amount==null){
			return new JsonResult(false,String.format(JsonResult.APP_DEFINE_PARAM_ERR,"金额"),null);
		}
		if(customer==null){//新建会员
			customer = new Customer();
			customer.setStoreRecordRealName(name);
			customer.setPhone(phone);
			int id = customerService.insertSelective(customer);
			//插入店铺关联
			if(id!=0&&departmentId!=null){
				insertDepartmentRelevanceIfNotExist(id ,departmentId,4,new Date());
			}

			if(id==0){
				return new JsonResult(false,"新增会员失败.",null);
			}else{
				customer.setId(id);
			}
		}else{
			Customer record = new Customer();
			record.setId(customer.getId());
			record.setStoreRecordRealName(name);
			customerService.updateByPrimaryKey(record);
		}
		//判断有无会员卡
		CustomerBalance card = balanceService.getCustomerBalance2(customer.getId());
		if(card!=null){//该用户存在会员卡,提示已有会员卡
			return new JsonResult(false,JsonResult.APP_CUSTOMER_HAVE_CARD,null);
		}
		//设置余额和订单id
		int a = (int) Math.ceil(amount*100);
		String orderId = getOrderIdByTime();
		int row = 0;
		if(orderAmount!=null){
			chargeBalanceNoBestow.setOrderAmount((int) Math.ceil(orderAmount*100));
		}
		if(bestowAmount!=null){
			chargeBalanceNoBestow.setBestowAmount((int) Math.ceil(bestowAmount*100));
		}
		chargeBalanceNoBestow.setSource(source);
		chargeBalanceNoBestow.setReferee(refereeId);
		chargeBalanceNoBestow.setRemark(remark);
		chargeBalanceNoBestow.setType(2); 
		chargeBalanceNoBestow.setDepartmentId(departmentId);
		try {
			row = balanceService.balanceChange(request,customer.getId(), a, chargeBalanceNoBestow, 1,orderId,null);
		} catch (Exception e) {
			logger.info(e.getMessage());
			return new JsonResult(false,e.getMessage(),null);
		}
		if(row<1){
			return new JsonResult(false,"建卡失败，请重试",null);
		}
		
		//查询用户信息
		if(customer!=null&&customer.getOpenid()!=null){
			String openid = customer.getOpenid();
			//时间
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日HH时mm分");
			String time = simpleDateFormat.format(new Date());
			//余额和消费金额
			CustomerBalanceRecord customerBalanceRecord = balanceRecordDao.selectByPrimaryKey(chargeBalanceNoBestow.getRecordId());
			if(customerBalanceRecord!=null){
				String realAmount = customerBalanceRecord.getOrderAmount()==null?"0":String.valueOf(customerBalanceRecord.getOrderAmount()/100.00);
				String realBestow = customerBalanceRecord.getBestowAmount()==null?"0":String.valueOf(customerBalanceRecord.getBestowAmount()/100.00);
				String currentBalance = customerBalanceRecord.getCurrentBalance()==null?"0":String.valueOf(customerBalanceRecord.getCurrentBalance()/100.00);
				//姓名
				String customerName = "无名字";
				if(customer.getStoreRecordRealName()!=null&&!customer.getStoreRecordRealName().isEmpty()){
					customerName = customer.getStoreRecordRealName();
				}else if(customer.getRealName()!=null&&!customer.getRealName().isEmpty()){
					customerName = customer.getRealName();
				}else if(customer.getNickname()!=null&&!customer.getNickname().isEmpty()){
					customerName = customer.getNickname();
				}else if(customer.getPhone()!=null&&!customer.getPhone().isEmpty()){
					customerName = customer.getPhone();
				}
				//发送模板消息
//				sendconsumeTemplateMsg(request,openid,storeName,realAmount+"元","无",time,currentBalance);
				sendChargeTemplateMsg(request,openid,time,customerName,"砖石会员卡","充值"+realAmount+"￥，送"+realBestow+"￥","余额"+currentBalance+"￥");
			}
		}
		
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("row", row);
		return new JsonResult(true,"建卡成功",map);
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
	//根据时间生成订单id
	public static String getOrderIdByTime() {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
		String newDate=sdf.format(new Date());
		String result="";
		Random random=new Random();
		for(int i=0;i<3;i++){
		result+=random.nextInt(10);
		}
		return newDate+result;
	}
	public Boolean sendconsumeTemplateMsg(HttpServletRequest request,String openid,String storeName,String amount,String integral,String time,String balance) {
		Map<String, Object> map = new HashMap<>();
//		map.put("openid", "oNzJP1YN_Tf6SP8tBWkIgztCpl6s");
		if(request.getParameter("openid")==null){
			map.put("openid", openid);
		}
		map.put("storeName", storeName);
		map.put("amount", amount);
		map.put("integral", integral);
		map.put("time", time);
		map.put("balance", balance);
//		JsonResult customerJsonResult = redirectService.redirect(request,map,"http://test.begogirls.com/gogirl_mp/wx/template/consumeRemindMsg");
		logger.info(map.toString());
		JsonResult customerJsonResult = redirectService.redirect(request,map,gogirlProperties.getGOGIRLMP()+"gogirl_mp/wx/template/consumeRemindMsg");
		if(customerJsonResult!=null&&customerJsonResult.getSuccess()){
			return true;
		}else{
			return false;
		}
	}	
	public Boolean sendChargeTemplateMsg(HttpServletRequest request,String openid,String time,String customerName,String type,String chargeDetail,String accountDetail) {
		Map<String, Object> map = new HashMap<>();
//		map.put("openid", "oNzJP1YN_Tf6SP8tBWkIgztCpl6s");
		if(request.getParameter("openid")==null){
			map.put("openid", openid);
		}
		map.put("time", time);
		map.put("customerName", customerName);
		map.put("type", type);
		map.put("chargeDetail", chargeDetail);
		map.put("accountDetail", accountDetail);
//		JsonResult customerJsonResult = redirectService.redirect(request,map,"http://test.begogirls.com/gogirl_mp/wx/template/chargeRemindMsg");
		logger.info(map.toString());
		JsonResult customerJsonResult = redirectService.redirect(request,map,gogirlProperties.getGOGIRLMP()+"gogirl_mp/wx/template/chargeRemindMsg");
		if(customerJsonResult!=null&&customerJsonResult.getSuccess()){
			return true;
		}else{
			return false;
		}
	}	
	public String getOrderStore(HttpServletRequest request,String id) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		String departmentName = "gogirl美甲美睫沙龙";
		JsonResult customerJsonResult = redirectService.redirect(request,map,gogirlProperties.getGOGIRLORDER()+"gogirl-order/ordermanage/queryOrderForDetail");
		if(customerJsonResult!=null&&customerJsonResult.getSuccess()&&customerJsonResult.getData()!=null){
			Map<String, Object> order = (Map<String, Object>) customerJsonResult.getData();
			if(order.get("departmentName")!=null||order.get("departmentName")!=""){
				departmentName = (String) order.get("departmentName");
			}
		}
		return departmentName;
	}
	
	public List<CustomerBalanceRecord> setStoreUser(List<CustomerBalanceRecord> list) {
		List<StoreUser> allStoreUser = storeUserMapper.getAllStoreUser();
		Map<String, String> idMapName = new HashMap<>();
		int allStoreUserSize = allStoreUser.size();
		for(int i=0;i<allStoreUserSize;i++){
			idMapName.put(String.valueOf(allStoreUser.get(i).getId()), allStoreUser.get(i).getName());
		}
		
		int row = list.size();
		for(int i = 0;i<row;i++){
			String refereeId = list.get(i).getRefereeId();
			if(refereeId!=null&&!refereeId.isEmpty()){
				String []refereeIdArr = refereeId.split(",");
				String refereeName = "";
				for(int j=0;j<refereeIdArr.length;j++){
					if(refereeIdArr[j]!=null&&!refereeIdArr[j].isEmpty()){
						if(idMapName.containsKey(refereeIdArr[j])){
							refereeName=refereeName+","+idMapName.get(refereeIdArr[j]);
						}else{
							refereeName=refereeName+","+refereeIdArr[j];
						}
					}
				}
				list.get(i).setRefereeId(refereeName.substring(1));
			}
		}
		return list;
	}

}
