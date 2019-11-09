package com.gogirl.gogirl_user.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import weibo4j.Oauth;
import weibo4j.Users;
import weibo4j.http.AccessToken;
import weibo4j.model.User;
import weibo4j.model.WeiboException;
import weibo4j.util.BareBonesBrowserLaunch;

import com.gogirl.gogirl_user.entity.CustomerWeibo;
import com.gogirl.gogirl_user.entity.JsonResult;
import com.gogirl.gogirl_user.service.CustomerWeiboService;

@Controller
public class WeiboController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Resource
	CustomerWeiboService customerWeiboService;
	
	@ResponseBody
	@RequestMapping("/weiboLogin")
	public JsonResult weiboLogin(String customerId,String targetUrl) {
		Oauth oauth = new Oauth();
		String url =null;
		try {
			url = oauth.authorize("code",customerId+","+targetUrl,"all");
//			BareBonesBrowserLaunch.openURL(url);
		} catch (WeiboException e1) {
			e1.printStackTrace();
			return new JsonResult(false,e1.getMessage(),null);
		}
		System.out.print("Hit enter when it's done.[Enter]:");
//		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		return new JsonResult(true ,"操作成功",url);
	}
	@RequestMapping("/weiboInfo")
	public String weiboInfo(String code,String state){
		Integer customerId = null;
		String page = "err";
		if(state==null&&!state.isEmpty()){
			logger.info("*****customerId is null");
		}else{
			logger.info("*****state:"+state);
			String [] arr= state.split(",");
			if(arr.length!=2){
				logger.info("state传参不对");
			}
			page = arr[1];
			try {
				customerId=Integer.parseInt(arr[0]);
			} catch (Exception e) {
				return "err";
			}
		}
		logger.info("code: " + code);
		Oauth oauth = new Oauth();
		AccessToken access_token = null;
		try{
			access_token = oauth.getAccessTokenByCode(code);
			logger.info(access_token.toString());
		} catch (WeiboException e) {
			if(401 == e.getStatusCode()){
				logger.info("Unable to get the access token.");
			}else{
				e.printStackTrace();
			}
		}		
		if(access_token==null){
//			return new JsonResult(false,"access_token is null",null);
			return "err";
		}
//		"2.00UO6D_H0KidH51ca8f50e2bcaN2IB";
		String uid = access_token.getUid();
		logger.info(uid);
		User user = null;
		Users um = new Users(access_token.getAccessToken());
		try {
			user = um.showUserById(uid);
			logger.info("*****************:"+user.toString());
		} catch (WeiboException e) {
			e.printStackTrace();
//			return  new JsonResult(false,e.getMessage(),null);
			return "err";
		}
		//保存数据到gogirl
		CustomerWeibo record = new CustomerWeibo();
		record.setCustomerId(customerId);
		record.setScreenName(user.getScreenName());
		record.setName( user.getName());
		record.setProfileimageUrl(user.getProfileImageUrl());
		record.setFollowMe(user.isFollowMe());
		CustomerWeibo customerWeibo = customerWeiboService.selectByPrimaryKey(customerId);
		int result = 0;
		if(customerWeibo==null){
			result = customerWeiboService.insertSelective(record);
			logger.info("插入微博数据："+record);
		}else{
			result = customerWeiboService.updateByPrimaryKeySelective(record);
			logger.info("更新微博数据："+record);
		}
		
		logger.info("end");
		Map<String, Object> map = new HashMap<>();
		map.put("screenName", user.getScreenName());
		map.put("name", user.getName());
		map.put("profileImageUrl", user.getProfileImageUrl());
		map.put("followMe", user.isFollowMe());
//		return new JsonResult(true,"操作成功",map);
		return page;
	}
}
