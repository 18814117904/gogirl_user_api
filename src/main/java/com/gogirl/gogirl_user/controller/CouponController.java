package com.gogirl.gogirl_user.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gogirl.gogirl_user.constant.GogirlProperties;
import com.gogirl.gogirl_user.entity.Coupon;
import com.gogirl.gogirl_user.entity.CouponCustomerRelevance;
import com.gogirl.gogirl_user.entity.Customer;
import com.gogirl.gogirl_user.entity.DiscountConfig;
import com.gogirl.gogirl_user.entity.JsonResult;
import com.gogirl.gogirl_user.service.CouponService;
import com.gogirl.gogirl_user.service.CustomerService;
import com.gogirl.gogirl_user.service.DiscountConfigService;
import com.gogirl.gogirl_user.service.RedirectService;

@RestController
public class CouponController {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	@Resource
	CouponService couponService;
	@Resource
	CustomerService customerService;
	@Resource
	RedirectService redirectService;
	@Resource
	GogirlProperties gogirlProperties;
	@Resource
	public RestTemplate restTemplate;
	
	//新增优惠券
	@RequestMapping(value="/insertCoupon")
    public JsonResult insertCoupon(Coupon coupon){
		if(coupon==null){
			return new JsonResult(false,String.format(JsonResult.APP_DEFINE_PARAM_ERR, "coupon"),null);
		}
		logger.info("新增优惠券:"+coupon.toString());
		int row = couponService.insertSelective(coupon);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("row", row);
		return new JsonResult(true, JsonResult.APP_DEFINE_SUC, map);
    }
	//查询优惠券详情
	@RequestMapping(value="/selectCouponByPrimaryKey")
    public JsonResult selectCouponByPrimaryKey(Integer id){
		Coupon coupon = couponService.selectByPrimaryKey(id);
		return new JsonResult(true, JsonResult.APP_DEFINE_SUC, coupon);
    }
	//查询用户领取详情
	@RequestMapping(value="/selectRelevanceByPrimaryKey")
    public JsonResult selectRelevanceByPrimaryKey(Integer id){
		CouponCustomerRelevance couponCustomerRelevance = couponService.selectRelevanceByPrimaryKey(id);
		return new JsonResult(true, JsonResult.APP_DEFINE_SUC, couponCustomerRelevance);
    }
	
	
	//查询优惠券列表,最后一次修改时间排序
	@RequestMapping(value="/selectByCoupon")
    public JsonResult selectByCoupon(Coupon coupon,Integer pageNum,Integer pageSize){
		if(pageNum==null){
			return new JsonResult(false,String.format(JsonResult.APP_DEFINE_PARAM_ERR, "pageNum"),null);
		}
		if(pageSize==null){
			return new JsonResult(false,String.format(JsonResult.APP_DEFINE_PARAM_ERR, "pageSize"),null);
		}
 		logger.info("查询优惠券列表:"+coupon.toString());
        PageHelper.startPage(pageNum,pageSize);
		List<Coupon> list = couponService.selectByCoupon(coupon);
		PageInfo<Coupon> pageInfo = new PageInfo<Coupon>(list);
		return new JsonResult(true, JsonResult.APP_DEFINE_SUC, pageInfo);
    }
	//修改优惠券
	@RequestMapping(value="/updateCoupon")
    public JsonResult updateByPrimaryKeySelective(Coupon coupon){
		if(coupon==null||coupon.getId()==null){
			return new JsonResult(false,String.format(JsonResult.APP_DEFINE_PARAM_ERR, "id"),null);
		}
		logger.info("修改优惠券:"+coupon.toString());
		int row = couponService.updateByPrimaryKeySelective(coupon);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("row", row);
		return new JsonResult(true, JsonResult.APP_DEFINE_SUC, map);
    }
	//启用和停止优惠券
	@RequestMapping(value="/enablingStoppingCoupon")
    public JsonResult enablingStoppingCoupon(Integer couponId){
		if(couponId==null){
			return new JsonResult(false,String.format(JsonResult.APP_DEFINE_PARAM_ERR, "couponId"),null);
		}
		logger.info("启用和停止优惠券:"+couponId.toString());
		Coupon coupon = couponService.selectByPrimaryKey(couponId);
		if(coupon==null){
			return new JsonResult(false,"找不到该优惠券",null);
		}else{
			if(coupon.getState()!=null&&coupon.getState()==1) coupon.setState(2);
			else coupon.setState(1);
		}
		int row = couponService.updateByPrimaryKeySelective(coupon);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("row", row);
		return new JsonResult(true, JsonResult.APP_DEFINE_SUC, map);
    }
	//关联,查询领取优惠券的用户列表,领取时间排序
	@RequestMapping(value="/selectByCouponCustomerRelevance")
    public JsonResult selectByCouponCustomerRelevance(CouponCustomerRelevance record,String phone,String username,Integer pageNum,Integer pageSize){
		if(pageNum==null){
			return new JsonResult(false,String.format(JsonResult.APP_DEFINE_PARAM_ERR, "pageNum"),null);
		}
		if(pageSize==null){
			return new JsonResult(false,String.format(JsonResult.APP_DEFINE_PARAM_ERR, "pageSize"),null);
		}
		if(record.getCode()!=null&&record.getCode().isEmpty()){
			record.setCode(null);
		}
		if(phone!=null&&phone.isEmpty()){
			phone=null;
		}
		if(username!=null&&username.isEmpty()){
			username=null;
		}
 		logger.info("查询领取优惠券的用户列表CouponCustomerRelevance:"+record.toString());
        PageHelper.startPage(pageNum,pageSize);
		List<CouponCustomerRelevance> list = couponService.selectByCouponCustomerRelevance(record,phone,username);
		long nowTime = new Date().getTime();
		int listSize = list.size();
		for(int i=0;i<listSize;i++){
			CouponCustomerRelevance couponCustomerRelevance = list.get(i);
			if(nowTime>couponCustomerRelevance.getValidEndTime().getTime()&&couponCustomerRelevance.getState()==1){
				couponCustomerRelevance.setState(3);
			}
		}
		PageInfo<CouponCustomerRelevance> pageInfo = new PageInfo<CouponCustomerRelevance>(list);
		return new JsonResult(true, JsonResult.APP_DEFINE_SUC, pageInfo);
    }
	//关联,发放优惠券
	@RequestMapping(value="/insertCouponCustomerRelevance")
    public JsonResult insertCouponCustomerRelevance(Integer customerId,Integer couponId,Integer couponId2){
		/*临时加上,查询优惠券id*/
		String couponIdString = couponService.getCouponIdFromConfig();
		String coupon2String = couponIdString.substring(3);
		couponIdString = couponIdString.substring(0,2);
		try {
			couponId = Integer.parseInt(couponIdString);
		} catch (Exception e) {
			return new JsonResult(false,"优惠券已经领完,谢谢参与",null);
		}
		if(couponId2!=null){
			couponId = couponId2;
		}
		logger.info("发放优惠券");
		if(customerId==null){
			return new JsonResult(false,String.format(JsonResult.APP_DEFINE_PARAM_ERR, "customerId"),null);
		}
		if(couponId==null){
			return new JsonResult(false,String.format(JsonResult.APP_DEFINE_PARAM_ERR, "couponId"),null);
		}
		logger.info("customerId:"+customerId.toString());
		logger.info("couponId:"+couponId.toString());
		
		Customer customer = customerService.selectByPrimaryKey(customerId);
		if(customer==null){
			return new JsonResult(false,"找不到该用户",null);
		}
		
		//返回消息
		Map<String, Object> map = new HashMap<String, Object>();
		JsonResult jr = sendCoupon(couponId,customerId,customer);
		if(jr.getSuccess()){
			map.put("id", jr.getData());
		}else{
			return jr;
		}
		if(coupon2String!=null&&!coupon2String.isEmpty()){
			JsonResult jr2 = sendCoupon(Integer.parseInt(coupon2String),customerId,customer);
			if(jr2.getSuccess()){
				map.put("id2", jr2.getData());
			}else{
				return jr2;
			}
		}
		return new JsonResult(true, JsonResult.APP_DEFINE_SUC, map);
    }
	//关联,发放优惠券
	@RequestMapping(value="/registerSendCoupon")
    public JsonResult registerSendCoupon(Integer customerId,Integer couponId){
		logger.info("发放优惠券");
		if(customerId==null){
			return new JsonResult(false,String.format(JsonResult.APP_DEFINE_PARAM_ERR, "customerId"),null);
		}
		if(couponId==null){
			return new JsonResult(false,String.format(JsonResult.APP_DEFINE_PARAM_ERR, "couponId"),null);
		}
		logger.info("customerId:"+customerId.toString());
		logger.info("couponId:"+couponId.toString());
		
		Customer customer = customerService.selectByPrimaryKey(customerId);
		if(customer==null){
			return new JsonResult(false,"找不到该用户",null);
		}
		
		//返回消息
		Map<String, Object> map = new HashMap<String, Object>();
		JsonResult jr = sendCoupon(couponId,customerId,customer);
		if(jr.getSuccess()){
			map.put("id", jr.getData());
		}else{
			return jr;
		}
		return new JsonResult(true, JsonResult.APP_DEFINE_SUC, map);
    }
	public JsonResult sendCoupon(Integer couponId,Integer customerId,Customer customer) {
		Coupon coupon = couponService.selectByPrimaryKey(couponId);
		if(coupon==null){
			return new JsonResult(false,"找不到该优惠券",null);
		}
		//判断优惠券是否还有
		if(coupon.getAllQuantity()<=coupon.getReceiveQuantity()){
			return new JsonResult(false,"优惠券已经领完",null);
		}
		//判断该用户是否限领,null过,0过,有但限制不过,不限制过
		CouponCustomerRelevance record = new CouponCustomerRelevance();
		record.setCustomerId(customerId);
		record.setCouponId(couponId);
		int row = couponService.countRelevanceNum(record);
		if(coupon.getLimitQuantity()!=null&&coupon.getLimitQuantity()!=0&&coupon.getLimitQuantity()<=row){
			return new JsonResult(false,"抱歉,该优惠券最多可领取"+coupon.getLimitQuantity()+"张,你已有"+row+"张该优惠券.",null);
		}
		
		CouponCustomerRelevance couponCustomerRelevance= new CouponCustomerRelevance();
		couponCustomerRelevance.setCustomerId(customerId);
		couponCustomerRelevance.setCouponId(couponId);
		couponCustomerRelevance.setState(1);
		couponCustomerRelevance.setCode(couponService.getRandomCode());
		couponCustomerRelevance.setReceiveTime(new Date());
		if(coupon.getValidType()==1){
			couponCustomerRelevance.setValidStartTime(coupon.getValidStartTime());//优惠券原有开始和结束时间
			couponCustomerRelevance.setValidEndTime(coupon.getValidEndTime());
		}else if(coupon.getValidType()==2){
			long today = new Date().getTime();
			long day7 = today+new Long(86400000)*coupon.getValidDate();
			couponCustomerRelevance.setValidStartTime(new Date(today));//从现在开始
			couponCustomerRelevance.setValidEndTime(new Date(day7));//七天后过期
		}
		int id = couponService.insertSelective(coupon,couponCustomerRelevance);
		
    	//发送模板消息
		Map<Integer, String> mapType = new HashMap<>();
		mapType.put(1, "现金抵扣券");
		mapType.put(2, "免单券");
		mapType.put(3, "满减券");
		Map<String, Object> mapParm	= new HashMap<>();
		mapParm.put("openid", customer.getOpenid());
		mapParm.put("storeName", "gogirl美甲美睫沙龙所有门店");
		mapParm.put("type", mapType.get(coupon.getType()));
		mapParm.put("amount", coupon.getName()==null?coupon.getDiscountAmount()+"元":coupon.getName());
		mapParm.put("code", "无需验证");
		redirectService.myHttpPost.httpRequest(gogirlProperties.getGOGIRLMP()+"gogirl_mp/wx/template/getTicketMsg", mapParm);
		return new JsonResult(true, JsonResult.APP_DEFINE_SUC, id);
	}
	//关联,查询领取优惠券的用户列表,领取时间排序
	@RequestMapping(value="/selectMyCoupon")
    public JsonResult selectMyCoupon(Integer customerId,Integer pageNum,Integer pageSize){
		if(pageNum==null){
			return new JsonResult(false,String.format(JsonResult.APP_DEFINE_PARAM_ERR, "pageNum"),null);
		}
		if(pageSize==null){
			return new JsonResult(false,String.format(JsonResult.APP_DEFINE_PARAM_ERR, "pageSize"),null);
		}
 		logger.info("查询领取优惠券的用户列表CouponCustomerRelevance:"+customerId.toString());
        PageHelper.startPage(pageNum,pageSize);
		List<CouponCustomerRelevance> list = couponService.selectMyCoupon(customerId);
		long nowTime = new Date().getTime();
		int listSize = list.size();
		for(int i=0;i<listSize;i++){
			CouponCustomerRelevance couponCustomerRelevance = list.get(i);
			if(nowTime>couponCustomerRelevance.getValidEndTime().getTime()&&couponCustomerRelevance.getState()==1){
				couponCustomerRelevance.setState(3);
			}
		}
		PageInfo<CouponCustomerRelevance> pageInfo = new PageInfo<CouponCustomerRelevance>(list);
		return new JsonResult(true, JsonResult.APP_DEFINE_SUC, pageInfo);
    }
    //订单结束后调用,根据订单金额插入优惠券且发消息模板
    @RequestMapping("/sendTicketAfterOrder")
	public JsonResult sendTicketAfterOrder(HttpServletRequest request,String openid,Integer customerId,
			Double orderAmount
			){
    	Boolean sentMsg = true;
    	if(openid==null||openid.isEmpty()){
    		if(customerId!=null){
    			Customer c = customerService.selectByPrimaryKey(customerId);
    			if(c!=null){
    				if(c.getOpenid()!=null&&!c.getOpenid().isEmpty()){
    					openid = c.getOpenid();
    				}else{
    					sentMsg = false;
    				}
    			}else{
    				return new JsonResult(false,"找不到该用户customerid:"+customerId,null);
    			}
    		}else{
    			return new JsonResult(false,String.format(JsonResult.APP_DEFINE_PARAM_ERR,"openid和customerId"),null);
    		}
    	}else{
    		Customer customer = customerService.selectByOpenid(openid);
    		if(customer!=null){
    			customerId = customer.getId();
    		}else{
    			return new JsonResult(false,"找不到该用户openid:"+openid,null);
    		}
    	}
    	String storeName="gogirl美甲美睫沙龙所有门店";
    	String type="";
    	String amount="";
    	String code="无需验证";
		Map<Integer, String> map = new HashMap<>();
		map.put(1, "现金抵扣券");
		map.put(2, "免单券");
		Coupon c = null;
    	//根据订单金额插入优惠券
    	if(orderAmount>=300){
    		logger.info("送50券");
    		c = couponService.selectByPrimaryKey(36);
    	}else if(orderAmount>=200){
    		logger.info("送30券");
    		c = couponService.selectByPrimaryKey(37);
    	}else if(orderAmount>=100){
    		logger.info("送10券");
    		c = couponService.selectByPrimaryKey(38);
    	}else{
    		logger.info("不送券");
    	}
    	
    	int row =0;
    	if(c!=null){
    		type = map.get(c.getType());
    		if(type==null||type.isEmpty()){
    			type = c.getDiscountAmount()+"元";
    		}
    		amount = c.getName();
    		//插入关联
    		CouponCustomerRelevance ccr = new CouponCustomerRelevance();
    		ccr.setCouponId(c.getId());
    		ccr.setCustomerId(customerId);
    		row = couponService.insertSelective(c,ccr);
    	}else{
    		return new JsonResult(false,"找不到优惠券",null);
    	}
    	if(sentMsg){//是否发送消息模板
        	//发送模板消息
    		Map<Integer, String> mapType = new HashMap<>();
    		mapType.put(1, "现金抵扣券");
    		mapType.put(2, "免单券");
    		Map<String, Object> mapParm	= new HashMap<>();
    		mapParm.put("storeName", storeName);
    		mapParm.put("code", code);
    		mapParm.put("openid", openid);
    		mapParm.put("type", type);
    		mapParm.put("amount", amount);
    		redirectService.myHttpPost.httpRequest(gogirlProperties.getGOGIRLMP()+"gogirl_mp/wx/template/getTicketMsg", mapParm);
    	}
		return  new JsonResult(true,JsonResult.APP_DEFINE_SUC,row);
    }

    //查询所有优惠券接口
	@RequestMapping(value="/getAllCoupon")
    public JsonResult getAllCoupon(){
 		logger.info("查询所有券");
		List<Coupon> list = couponService.getAllUseCoupon();
		return new JsonResult(true, JsonResult.APP_DEFINE_SUC, list);
    }
    
    //总后台发券接口:issueType发放类型，groupType分组类型，phone电话号码，couponId优惠券id，amount数量
    //issueType发放类型：1.单个用户；2.群发用户
    //groupType分组类型：1.注册用户;2.订单用户;3.次订单用户;4.会员卡用户
	@RequestMapping(value="/sendCoupons")
    public JsonResult sendCoupons (HttpServletRequest request,Integer issueType,String groupType,String phone,Integer couponId,Integer amount){
//入参判断
		if(issueType==null){
			return new JsonResult(false,String.format(JsonResult.APP_DEFINE_PARAM_ERR,"issueType"),null);
		}
		if(couponId==null){
			return new JsonResult(false,String.format(JsonResult.APP_DEFINE_PARAM_ERR,"couponId"),null);
		}
		if(amount==null){
			return new JsonResult(false,String.format(JsonResult.APP_DEFINE_PARAM_ERR,"issueType"),null);
		}
		logger.info("发券："+issueType,groupType,phone,couponId,amount);
		//查券
		Coupon coupon = couponService.selectByPrimaryKey(couponId);
		if(coupon==null){
			return new JsonResult(false,"找不到该优惠券",null);
		}
		
		//查用户
		List<Customer> listCustomer = new ArrayList<>();
 		if(issueType==1){//单个用户
 			if(phone==null){
 				return new JsonResult(false,String.format(JsonResult.APP_DEFINE_PARAM_ERR,"phone"),null);
 			}
 			//查询电话号码用户
 			Customer customer = customerService.selectByPhone(phone);
 			if(customer!=null){
 	 			listCustomer.add(customer);
 			}else{
 				return new JsonResult(false, "找不到该用户phone="+phone, null);
 			}
 		}else if(issueType==2){
 			if(groupType==null){
 				return new JsonResult(false,String.format(JsonResult.APP_DEFINE_PARAM_ERR,"groupType"),null);
 			}
 			//groupType拆分成四个参数，调用四段条件查询,通过or关联。查询用户id,openid
 			Integer havePhone= null;
 			Integer orderTimes= null;
 			Integer haveBalance= null;
 			if(groupType.contains("1")){
 				havePhone = 1;//随便赋值
 			}
 			if(groupType.contains("3")){
 				orderTimes = 2;//两次订单以上
 			}
 			if(groupType.contains("2")){
 				orderTimes = 1;//一次订单以上
 			}
 			if(groupType.contains("4")){
 				haveBalance = 1;//随便赋值
 			}
 			listCustomer = customerService.getGroupCustomer(havePhone, orderTimes, haveBalance);
 		}else{
 			logger.info("未知发放类型");
 			return new JsonResult(false,"未知发放类型issueType="+issueType,null);
 		}
 		//判断是否足够券，增加优惠券总数
 		int addCouponNum = 0;//新增了几张优惠券
 		int listCustomerSize = listCustomer.size();
		if(coupon.getAllQuantity()-coupon.getReceiveQuantity()<listCustomerSize){
			addCouponNum = coupon.getReceiveQuantity()+listCustomerSize-coupon.getAllQuantity();
			coupon.setAllQuantity(coupon.getReceiveQuantity()+listCustomerSize);//增加优惠券总数
//			coupon.setReceiveQuantity(coupon.getAllQuantity());//设置所有优惠券都被领了//批量发券时再改
			couponService.updateByPrimaryKeySelective(coupon);
		}
		String[] openidList = new String[listCustomerSize];
		//批量插入关联数据
		List<CouponCustomerRelevance> list = new ArrayList<>();
		
		for(int i=0;i<listCustomerSize;i++){
			CouponCustomerRelevance ccr = new CouponCustomerRelevance();
			ccr.setCustomerId(listCustomer.get(i).getId());
			ccr.setCouponId(coupon.getId());
			list.add(ccr);
			if(listCustomer.get(i)!=null&&listCustomer.get(i).getOpenid()!=null){//所有有openid的账号
				openidList[i]=listCustomer.get(i).getOpenid();
			}else{
				
			}
		}
		int insertRow = couponService.insertSelectiveList(coupon, list,amount);
		try {
			// 批量发起模板消息
			Map<Integer, String> mapType = new HashMap<>();
			mapType.put(1, "现金抵扣券");
			mapType.put(2, "免单券");
//			Map<String, Object> mapParm	= new HashMap<>();
//			mapParm.put("openidList", String.valueOf(openidList));
//			logger.info(openidList.toString());
//			mapParm.put("storeName", "gogirl美甲美睫沙龙所有门店");
//			mapParm.put("type", mapType.get(coupon.getType()));
//			mapParm.put("amount", coupon.getName()==null?coupon.getDiscountAmount()+"元":coupon.getName());
//			mapParm.put("code", "无需验证");
			restTemplate.postForObject(gogirlProperties.getGOGIRLMP()+"gogirl_mp/wx/template/getTicketMsgOpenidList?storeName=gogirl美甲美睫沙龙所有门店&code=无需验证&type="+
					 mapType.get(coupon.getType())+"&amount="+ (coupon.getName()==null?coupon.getDiscountAmount()+"元":coupon.getName()), 
					openidList, JsonResult.class );
//						redirectService.myHttpPost.httpRequest(gogirlProperties.getGOGIRLMP()+"gogirl_mp/wx/template/getTicketMsgOpenidList", mapParm);
//			redirectService.myHttpPost.restTemplate.postForObject(gogirlProperties.getGOGIRLMP()+"gogirl_mp/wx/template/getTicketMsgOpenidList", 
//					request, JsonResult.class,listOpenid );
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
		
		
 		//TODO 返回哪些用户没有发送成功
		Map<String, Object> map = new HashMap<>();
		map.put("addCouponNum",addCouponNum );
		map.put("insertRow",insertRow );
 		return new JsonResult(true, "群发成功", map);
    }
}
