package com.gogirl.gogirl_user.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import com.gogirl.gogirl_user.entity.ActivityChanceCustomer;
import com.gogirl.gogirl_user.entity.ActivityPrize;
import com.gogirl.gogirl_user.entity.ActivityPrizeCustomer;
import com.gogirl.gogirl_user.entity.Customer;
import com.gogirl.gogirl_user.entity.CustomerDepartmentRelevance;
import com.gogirl.gogirl_user.entity.CustomerDepartmentRelevanceKey;
import com.gogirl.gogirl_user.entity.CustomerDetail;
import com.gogirl.gogirl_user.entity.JsonResult;
import com.gogirl.gogirl_user.service.ActivityService;
import com.gogirl.gogirl_user.service.CustomerDepartmentRelevanceService;
import com.gogirl.gogirl_user.service.CustomerDetailService;
import com.gogirl.gogirl_user.service.CustomerService;

@Controller
public class ActiveCotroller {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Resource
	private CustomerService customerService;
	@Resource
	private ActivityService activityService;

	//赠送玫瑰纯露的接口
	@ResponseBody
	@RequestMapping("/getJoinActiveCustomer")
	public JsonResult getJoinActiveCustomer(Integer pageNum,Integer pageSize,String phone) {
		if(pageNum==null||pageSize==null){
			return new JsonResult(false,"请输入pageNum和pageSize", null);
		}
		if(phone==null){
			phone = "";
		}
        PageHelper.startPage(pageNum,pageSize);
		List<Customer> list = customerService.getJoinActiveCustomer(phone);
		PageInfo<Customer> pageInfo = new PageInfo<Customer>(list);
		return new JsonResult(true, "", pageInfo);
	}
	
//	不为0的奖品列表
	@ResponseBody
	@RequestMapping("/selectByActivityPrize")
	public JsonResult selectByActivityPrize(Integer pageNum,Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<ActivityPrize> list = activityService.selectByActivityPrize();
		PageInfo<ActivityPrize> pageInfo = new PageInfo<ActivityPrize>(list);
		return new JsonResult(true, JsonResult.APP_DEFINE_SUC, pageInfo);
	}
//	查询所有奖品列表
	@ResponseBody
	@RequestMapping("/selectAllPrize")
	public JsonResult selectAllPrize() {
        List<ActivityPrize> list = activityService.selectAllPrize();
		return new JsonResult(true, JsonResult.APP_DEFINE_SUC, list);
	}
//	更变奖品领取状态
	@ResponseBody
	@RequestMapping("/setPrizeState")
	public JsonResult setPrizeState(Integer id,Integer status) {
		int row = activityService.setPrizeState(id,status);
		if(row==0){
			return new JsonResult(false,"找不到记录",row);
		}else{
			return new JsonResult(true,JsonResult.APP_DEFINE_SUC,row);
		}
	}
	
//	签到接口
	@ResponseBody
	@RequestMapping("/dailySign")
	public JsonResult dailySign(int customerId) {
        ActivityPrize activityPrize = activityService.dailySign(customerId);
        if(activityPrize!=null){
    		return new JsonResult(true, JsonResult.APP_DEFINE_SUC, activityPrize);
        }else{
        	return new JsonResult(false, "今日已经签到", null);
        }
	}
	
	
//	助力接口
	@ResponseBody
	@RequestMapping("/girlFriendsHelp")
	public JsonResult girlFriendsHelp(int customerId,int friendId) {
		//添加开福次数
        int row = activityService.girlFriendsHelp(customerId,friendId);
        if(row>0){
    		//记录谁帮谁助力
    		activityService.GirlFriendsHelpRecord(customerId,friendId);
    		return new JsonResult(true, JsonResult.APP_DEFINE_SUC, row);
        }else if(row==-2){
        	return new JsonResult(false, "您已经为该闺蜜助力,不能重复助力.", row);
        }else if(row==-1){
        	return new JsonResult(false, "今日助力次数已满30次.", row);
        }else{
        	return new JsonResult(false, "助力失败.", row);
        }
	}
	
	
//	查询开福次数
	@ResponseBody
	@RequestMapping("/getActivityChanceCustomer")
	public JsonResult getActivityChanceCustomer(int customerId) {
		ActivityChanceCustomer acc = activityService.getActivityChanceCustomer(customerId);
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		Date nowdate = null;
		try {
			nowdate = format.parse(format.format(new Date()));
		} catch (ParseException e) {
		}

		if(acc==null){
			acc = new ActivityChanceCustomer();
			acc.setBigLottery(0);
			acc.setGirlfriendsHelp(0);
			acc.setCustomerId(0);
			acc.setIsBlessRegist(0);
			acc.setIsSignIn(0);
			acc.setLotteryTime(new Date());
			acc.setTotalLottery(0);
			acc.setUsedLottery(0);
		}else{
			if(acc.getLotteryTime()!=null&&acc.getLotteryTime().getTime()<nowdate.getTime()){
				acc.setGirlfriendsHelp(0);
				acc.setIsSignIn(0);
				acc.setLotteryTime(new Date());
				acc.setTotalLottery(0);
				acc.setUsedLottery(0);
			}
		}
        return new JsonResult(true, JsonResult.APP_DEFINE_SUC, acc);
	}
	
	
	
//	开福接口
	@ResponseBody
	@RequestMapping("/openFoka")
	public JsonResult openFoka(Integer customerId) {
		return activityService.openFoka(customerId);
	}
	
	
//	我的福卡列表
	@ResponseBody
	@RequestMapping("/getMyFokas")
	public JsonResult getMyFokas(int customerId) {
		List<ActivityPrize> list = activityService.getMyFokas(customerId);
		return new JsonResult(true,JsonResult.APP_DEFINE_SUC,list);
	}
	
//	店铺查获奖列表
	@ResponseBody
	@RequestMapping("/getPrizeList")
	public JsonResult getPrizeList(String nickname,String phone,Integer pid,Integer prizeCode,Integer pageNum,Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
		List<ActivityPrize> list = activityService.getPrizeList(nickname,phone,pid,prizeCode);
		PageInfo<ActivityPrize> pageInfo = new PageInfo<ActivityPrize>(list);
		return new JsonResult(true,JsonResult.APP_DEFINE_SUC,pageInfo);
	}
//	我的礼品记录列表
	@ResponseBody
	@RequestMapping("/myPrizeList")
	public JsonResult myPrizeList(Integer customerId) {
		List<ActivityPrize> list = activityService.myPrizeList(customerId);
		return new JsonResult(true,JsonResult.APP_DEFINE_SUC,list);
	}
	
	
//	五张福卡合成大奖接口
	@ResponseBody
	@RequestMapping("/mergeFokas")
	public JsonResult mergeFokas() {
		int num = activityService.mergeFokas();
		return new JsonResult(true,JsonResult.APP_DEFINE_SUC,num);
	}
//	打开五福大奖接口
	@ResponseBody
	@RequestMapping("/openBigFoka")
	public JsonResult openBigFoka(Integer customerId) {
		return activityService.openBigFoka(customerId);
	}
	
//	判断是否关注公众号接口,在gogirl系统getSubscribeInfo
	
	//查询最新获奖列表
	@ResponseBody
	@RequestMapping("/getNewPrizeList")
	public JsonResult getNewPrizeList() {
		List<ActivityPrize> list = activityService.getNewPrizeList();
		return new JsonResult(true,JsonResult.APP_DEFINE_SUC,list);
	}
//	福气加特
	@ResponseBody
	@RequestMapping("/setJiaTe")
	public JsonResult setJiaTe(Integer customerId) {
		int row = activityService.setJiaTe(customerId);
		return new JsonResult(true,JsonResult.APP_DEFINE_SUC,row);
	}
	
}
