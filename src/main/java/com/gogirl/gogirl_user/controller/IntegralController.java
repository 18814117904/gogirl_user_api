package com.gogirl.gogirl_user.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gogirl.gogirl_user.entity.CustomerIntegral;
import com.gogirl.gogirl_user.entity.JsonResult;
import com.gogirl.gogirl_user.service.CustomerIntegralService;
import com.gogirl.gogirl_user.service.integral.MinusIntegral;
import com.gogirl.gogirl_user.service.integral.PlusIntegral;

@Controller
public class IntegralController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	CustomerIntegralService integralService;
	@Autowired
	PlusIntegral plusIntegral;
	@Autowired
	MinusIntegral minusIntegral;
	
	//增加积分接口
	@ResponseBody
	@RequestMapping("/plusIntegral")
	public JsonResult plusIntegral(HttpServletRequest request,@RequestParam int customerId,@RequestParam int value,@RequestParam String orderId) {
		int row = 0;
		try {
			row = integralService.integralChange(customerId, value, plusIntegral, 1,orderId);
		} catch (Exception e) {
			logger.info(e.getMessage());
			return new JsonResult(false,e.getMessage(),null);
		}
		if(row<1){
			return new JsonResult(false,"增加积分失败，请重试",null);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("row", row);
		return new JsonResult(true,"",map);
	}
	
	//减少积分接口
	@ResponseBody
	@RequestMapping("/minusIntegral")
	public JsonResult minusIntegral(HttpServletRequest request,@RequestParam int customerId,@RequestParam int value,@RequestParam String orderId) {
		int row = 0;
		try {
			row = integralService.integralChange(customerId, value,minusIntegral , -1,orderId);
		} catch (Exception e) {
			logger.info(e.getMessage());
			return new JsonResult(false,e.getMessage(),null);
		}
		if(row<1){
			return new JsonResult(false,"减少积分失败，请重试",null);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("row", row);
		return new JsonResult(true,"",map);
	}
	
	//查询积分接口
	@ResponseBody
	@RequestMapping("/getIntegral")
	public JsonResult getIntegral(HttpServletRequest request,@RequestParam int customerId) {
		CustomerIntegral integral = integralService.getIntegral(customerId);
		if(integral==null){
			return new JsonResult(false,"查询积分，请重试",null);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("integral", integral);
		return new JsonResult(true,"",map);
	}

}
