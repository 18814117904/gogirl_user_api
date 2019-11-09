package com.gogirl.gogirl_user.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.javassist.expr.NewArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gogirl.gogirl_user.entity.JsonResult;
import com.gogirl.gogirl_user.service.CountService;
import com.gogirl.gogirl_user.service.CustomerService;

@Controller
public class CountCotroller {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private CountService countService;
	@Autowired
	private CustomerService customerService;

	@ResponseBody
	@RequestMapping("/countCustomerInfo")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	public JsonResult countCustomerInfo(Integer departmentId ,@DateTimeFormat(pattern="yyyy-MM-dd") Date startTime,@DateTimeFormat(pattern="yyyy-MM-dd") Date endTime) {
//		if(departmentId==null){
//			return new JsonResult(false,String.format(JsonResult.APP_DEFINE_PARAM_ERR, "departmentId"),null);
//		}
//		if(startTime==null){
//			return new JsonResult(false,String.format(JsonResult.APP_DEFINE_PARAM_ERR, "startTime"),null);
//		}
//		if(endTime==null){
//			return new JsonResult(false,String.format(JsonResult.APP_DEFINE_PARAM_ERR, "endTime"),null);
//		}
		
		if(endTime!=null){//时间加一天,一天的毫秒数86400000
			endTime.setTime(endTime.getTime()+new Long(86400000));
		}
		//查询会员卡＋开卡金额＋开卡余额＋新增客户＋新增用户
		Integer customerBalanceNum = 0;
		customerBalanceNum = countService.countCustomerBalanceNum(departmentId,startTime,endTime);
		Integer sumCharge = 0;
		sumCharge = countService.countSumCharge(departmentId,startTime,endTime);
		double sumChargeDouble = 0;
		if(sumCharge!=null&&sumCharge!=0){
			sumChargeDouble = sumCharge/100.0;
		}
		Integer sumBalance = 0;
		sumBalance = countService.countSumBalance(departmentId,startTime,endTime);
		double sumBalanceDouble = 0;
		if(sumBalance!=null&&sumBalance!=0){
			sumBalanceDouble = sumBalance/100.0;
		}
		Integer consumeCustomerNum = 0;
		consumeCustomerNum = countService.countConsumeCustomerNum(departmentId,startTime,endTime);
		Integer customerNum = 0;
		customerNum = countService.countCustomerNum(departmentId,startTime,endTime);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("customerBalanceNum",customerBalanceNum==null?0:customerBalanceNum );
		map.put("sumCharge",sumChargeDouble);
		map.put("sumBalance", sumBalanceDouble);
		map.put("consumeCustomerNum", consumeCustomerNum==null?0:consumeCustomerNum);
		map.put("customerNum",customerNum==null?0:customerNum );
		return new JsonResult(true,JsonResult.APP_DEFINE_SUC,map);
	}
	@ResponseBody
	@RequestMapping("/countCategorySalesRatio")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	public JsonResult countCategorySalesRatio(Integer departmentId ,@DateTimeFormat(pattern="yyyy-MM-dd") Date startTime,@DateTimeFormat(pattern="yyyy-MM-dd") Date endTime) {
		if(endTime!=null){//时间加一天,一天的毫秒数86400000
			endTime.setTime(endTime.getTime()+new Long(86400000));
		}
		//查询类别销售比例
		List<Map<String, Object>> list= countService.countCategorySalesRatio(departmentId,startTime,endTime);
		int sum = 0;
		for(int i=0;i<list.size();i++){
			sum += list.get(i).get("num")!=null?(long)list.get(i).get("num"):0;
		}
		NumberFormat percent = NumberFormat.getPercentInstance();//化为百分比
		percent.setMaximumFractionDigits(2);//百分比后两位小数
		for(int i=0;i<list.size();i++){
			if(sum!=0){
				BigDecimal itemDecimal = new BigDecimal(list.get(i).get("num")!=null?(long)list.get(i).get("num"):0);
				BigDecimal sumDecimal = new BigDecimal(sum);
				BigDecimal divide = itemDecimal.divide(sumDecimal, 4, BigDecimal.ROUND_HALF_UP);
				list.get(i).put("ratio", percent.format(divide));
			}else{
				list.get(i).put("ratio", percent.format(new BigDecimal("0.0000")));
			}
		}
		return new JsonResult(true,JsonResult.APP_DEFINE_SUC,list);
	}
	@ResponseBody
	@RequestMapping("/countRepeatPurchaseRate")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	public JsonResult countRepeatPurchaseRate(Integer departmentId ,@DateTimeFormat(pattern="yyyy-MM-dd") Date startTime,@DateTimeFormat(pattern="yyyy-MM-dd") Date endTime) {
		if(endTime!=null){//时间加一天,一天的毫秒数86400000
			endTime.setTime(endTime.getTime()+new Long(86400000));
		}
		//查询复购率
		Double rate = countService.countRepeatPurchaseRate(departmentId,startTime,endTime);
		NumberFormat percent = NumberFormat.getPercentInstance();//化为百分比
		percent.setMaximumFractionDigits(2);//百分比后两位小数
		return new JsonResult(true,JsonResult.APP_DEFINE_SUC,percent.format(new BigDecimal(rate==null?0.0000:rate).setScale(4,BigDecimal.ROUND_HALF_UP)));
	}
	@ResponseBody
	@RequestMapping("/countCustomerUnitPrice")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	public JsonResult countCustomerUnitPrice(Integer departmentId ,@DateTimeFormat(pattern="yyyy-MM-dd") Date startTime,@DateTimeFormat(pattern="yyyy-MM-dd") Date endTime) {
		if(endTime!=null){//时间加一天,一天的毫秒数86400000
			endTime.setTime(endTime.getTime()+new Long(86400000));
		}
		BigDecimal sumPrice = new BigDecimal("0");
		int count = 0;
		//计算客单价
		List<Map<String, Object>> list = countService.countCustomerUnitPrice(departmentId,startTime,endTime);
		int listSize = list.size();
		for(int i=0;i<listSize;i++){
			sumPrice = sumPrice.add(new BigDecimal(String.valueOf(list.get(i).get("total_price"))));
			sumPrice = sumPrice.add(new BigDecimal(String.valueOf(list.get(i).get("change_price"))));
			sumPrice = sumPrice.subtract(new BigDecimal(String.valueOf(list.get(i).get("discount_price"))));
			count++;
		}
		if(count==0){
			return new JsonResult(true,JsonResult.APP_DEFINE_SUC,new BigDecimal("0").setScale(2,BigDecimal.ROUND_HALF_UP));
		}
		BigDecimal unitPrice = sumPrice.divide(new BigDecimal(count),2,BigDecimal.ROUND_HALF_UP);
		return new JsonResult(true,JsonResult.APP_DEFINE_SUC,unitPrice);
	}
	//查询待录入资料会员数
	@ResponseBody
	@RequestMapping("/countUnRecordCustomerInfoNum")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	public JsonResult countUnRecordCustomerInfoNum(Integer departmentId) {
		Integer count = countService.countUnRecordCustomerInfoNum(departmentId);
		return new JsonResult(true,JsonResult.APP_DEFINE_SUC,count==null?0:count);
	}
}
