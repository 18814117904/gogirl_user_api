package com.gogirl.gogirl_user.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gogirl.gogirl_user.entity.CouponCustomerRelevance;
import com.gogirl.gogirl_user.entity.Customer;
import com.gogirl.gogirl_user.entity.DiscountConfig;
import com.gogirl.gogirl_user.entity.JsonResult;
import com.gogirl.gogirl_user.service.CouponService;
import com.gogirl.gogirl_user.service.DiscountConfigService;

@RestController
public class DiscountConfigController {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	@Resource
	DiscountConfigService DiscountConfigService;
	@Resource
	CouponService couponService;
	
	@RequestMapping(value="/deleteByPrimaryKey")
    public JsonResult deleteByPrimaryKey(Integer id){
		int row = DiscountConfigService.deleteByPrimaryKey(id);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("row", row);
		return new JsonResult(true, JsonResult.APP_DEFINE_SUC, map);
    }
//	@RequestMapping(value="/insertSelective")
//    public JsonResult insertSelective(@RequestParam Integer totalCharge,Double discount,String level,String remark){
//		DiscountConfig discountConfig = new DiscountConfig();
//		
//		discountConfig.setTotalCharge(totalCharge);
//		discountConfig.setDiscount(discount);
//		if(level!=null&&!level.isEmpty()){
//			discountConfig.setLevel(level);
//		}
//		if(remark!=null&&!remark.isEmpty()){
//			discountConfig.setRemark(remark);
//		}
//    	int row = DiscountConfigService.insertSelective(discountConfig);
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("row", row);
//		return new JsonResult(true, JsonResult.APP_DEFINE_SUC, map);
//    }
	@RequestMapping(value="/insertSelective")
    public JsonResult insertSelective(DiscountConfig record){
		JsonResult jsonResult = checkParm(record);
		if(!jsonResult.getSuccess()){
			return jsonResult;
		}
    	int row = DiscountConfigService.insertSelective(record);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("row", row);
		return new JsonResult(true, JsonResult.APP_DEFINE_SUC, map);
    }

	private JsonResult checkParm(DiscountConfig record) {
		if(record==null){
			return new JsonResult(false,String.format(JsonResult.APP_DEFINE_PARAM_ERR,"所有参数"),null);
		}
		if(record.getChargeAmount()==null){
			return new JsonResult(false,String.format(JsonResult.APP_DEFINE_PARAM_ERR,"chargeAmount"),null);
		}
		if(record.getBestowAmount()==null){
			return new JsonResult(false,String.format(JsonResult.APP_DEFINE_PARAM_ERR,"bestowAmount"),null);
		}
		if(record.getDiscount()==null){
			return new JsonResult(false,String.format(JsonResult.APP_DEFINE_PARAM_ERR,"discount"),null);
		}
		return new JsonResult(true,"",null);
	}
	@RequestMapping(value="/selectByPrimaryKey")
    public JsonResult selectByPrimaryKey(Integer id){
    	DiscountConfig discountConfig =  DiscountConfigService.selectByPrimaryKey(id);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("discountConfig", discountConfig);
		return new JsonResult(true, JsonResult.APP_DEFINE_SUC, map);
    }
	@RequestMapping(value="/updateByPrimaryKeySelective")
    public JsonResult updateByPrimaryKeySelective(DiscountConfig record){
    	int row = DiscountConfigService.updateByPrimaryKeySelective(record);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("row", row);
		return new JsonResult(true, JsonResult.APP_DEFINE_SUC, map);
    }
	@RequestMapping(value="/selectAllDiscount")
	public JsonResult selectAllDiscount() {
		List<DiscountConfig> list =  DiscountConfigService.selectAllDiscount();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		return new JsonResult(true, JsonResult.APP_DEFINE_SUC, map);
	}
	@RequestMapping(value="/selectDiscount")
	public JsonResult selectDiscount(Integer pageNum,Integer pageSize) {
		PageHelper.startPage(pageNum,pageSize);
		List<DiscountConfig> list =  DiscountConfigService.selectAllDiscount();
		PageInfo<DiscountConfig> pageInfo = new PageInfo<DiscountConfig>(list);
		return new JsonResult(true, JsonResult.APP_DEFINE_SUC, pageInfo);
	}
	@RequestMapping(value="/selectByTotalCharge")
	public JsonResult selectByTotalCharge(int totalCharge) {
		DiscountConfig discountConfig =  DiscountConfigService.selectByTotalCharge(totalCharge);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("discountConfig", discountConfig);
		return new JsonResult(true, JsonResult.APP_DEFINE_SUC, map);
	}
	@RequestMapping(value="/selectByCharge")
	public JsonResult selectByCharge(int amount) {
		DiscountConfig discountConfig =  DiscountConfigService.selectByCharge(amount);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("discountConfig", discountConfig);
		return new JsonResult(true, JsonResult.APP_DEFINE_SUC, map);
	}
	@RequestMapping(value="/couponUse")
	public JsonResult couponUse(Integer couponRelevanceId) {
		if(couponRelevanceId==null){
			return new JsonResult(false,String.format(JsonResult.APP_DEFINE_PARAM_ERR, "couponRelevanceId"),null);
		}
		
		CouponCustomerRelevance record = new CouponCustomerRelevance();
		record.setId(couponRelevanceId);
		record.setState(2);
		record.setUseDate(new Date());
		int row = couponService.updateRelevanceByPrimaryKeySelective(record);
		return new JsonResult(true,JsonResult.APP_DEFINE_SUC,row);
	}
	@RequestMapping(value="/count")
	public JsonResult count(Integer customerId,Integer couponRelevanceId ,Double price) {
		return countPrice(customerId,couponRelevanceId ,price);
	}
	@RequestMapping(value="/countPay")
	public JsonResult countPay(Integer customerId,Integer couponRelevanceId ,Double price) {
		JsonResult result= countPrice(customerId,couponRelevanceId ,price);
		if(result.getSuccess()){
			
			Map<String, Object>map = (Map<String, Object>) result.getData();
			if(map.containsKey("couponId")){
				CouponCustomerRelevance record = new CouponCustomerRelevance();
				record.setId(couponRelevanceId);
				record.setState(2);
				record.setUseDate(new Date());
				record.setCouponId((Integer) map.get("couponId"));
				couponService.updateRelevanceByPrimaryKeySelective(record);
			}
		}
		return result;
	}
	public JsonResult countPrice(Integer customerId,Integer couponRelevanceId ,Double price) {
	Map<String, Object> map = new HashMap<String, Object>();
	if(customerId==null){
		logger.info("非会员无折扣");
		map.put("discountPrice", 0.0);
		return new JsonResult(true,"找不到该会员,不打折",map);
	}
	if(couponRelevanceId==null){
		logger.info("无折扣id,不打折");
		map.put("discountPrice", 0.0);
		return new JsonResult(true,"折扣id为空,couponRelevanceId",map);
	}
	
	//确定券存在
	CouponCustomerRelevance couponCustomerRelevance = couponService.selectRelevanceByPrimaryKey(couponRelevanceId);
	if(couponCustomerRelevance==null){
		logger.info("抱歉,优惠券不存在.");
		map.put("discountPrice", 0.0);
		return new JsonResult(false, "抱歉,优惠券不存在.", map);
	}
	//判断券主人和使用者,是同一个人
	if(!couponCustomerRelevance.getCustomerId().equals(customerId)){
		logger.info("优惠券主人couponCustomerRelevance.getCustomerId():"+couponCustomerRelevance.getCustomerId());
		logger.info("使用者customerId:"+customerId);
		map.put("discountPrice", 0.0);
		return new JsonResult(false, "抱歉,这不是您的优惠券", map);
	}
	//判断有效期
	long nowTime= new Date().getTime();
	if(couponCustomerRelevance.getValidStartTime().getTime()>nowTime){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
		logger.info("getValidStartTime:"+sdf.format(couponCustomerRelevance.getValidStartTime()));
		map.put("discountPrice", 0.0);
		return new JsonResult(false, "该优惠暂未开始,谢谢关注.", map);
	}
	if(couponCustomerRelevance.getValidEndTime().getTime()<nowTime){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
		logger.info("getValidEndTime:"+sdf.format(couponCustomerRelevance.getValidEndTime()));
		map.put("discountPrice", 0.0);
		return new JsonResult(false, "优惠券已经过期.", map);
	}
	//如果是满减券,判断是否满,是否减
	if(couponCustomerRelevance.getCoupon().getType().equals(3)){
		if(couponCustomerRelevance.getCoupon().getReachingAmount()==null){
			map.put("discountPrice", 0.0);
			return new JsonResult(false, "该满减券无'满'金额.", map);
		}
		if(couponCustomerRelevance.getCoupon().getReachingAmount()>price){
			map.put("discountPrice", 0.0);
			return new JsonResult(false, "订单金额未满足满减条件.", map);
		}
	}
	
	Double discountedPrice = DiscountConfigService.count(couponCustomerRelevance,price);
	logger.info(String.format("计算折扣：折前:%s,折后:%s",price,discountedPrice));
	map.put("discountPrice", new BigDecimal(price).subtract(new BigDecimal(discountedPrice)).doubleValue());
	map.put("couponId",couponCustomerRelevance.getCouponId() );
	return new JsonResult(true,JsonResult.APP_DEFINE_SUC,map);
	}
}
