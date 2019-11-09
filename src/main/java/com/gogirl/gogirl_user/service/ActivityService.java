package com.gogirl.gogirl_user.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gogirl.gogirl_user.dao.ActivityChanceCustomerMapper;
import com.gogirl.gogirl_user.dao.ActivityHelpMapper;
import com.gogirl.gogirl_user.dao.ActivityPrizeCustomerMapper;
import com.gogirl.gogirl_user.dao.ActivityPrizeMapper;
import com.gogirl.gogirl_user.entity.ActivityChanceCustomer;
import com.gogirl.gogirl_user.entity.ActivityHelp;
import com.gogirl.gogirl_user.entity.ActivityPrize;
import com.gogirl.gogirl_user.entity.ActivityPrizeCustomer;
import com.gogirl.gogirl_user.entity.Customer;
import com.gogirl.gogirl_user.entity.CustomerBalance;
import com.gogirl.gogirl_user.entity.CustomerWeibo;
import com.gogirl.gogirl_user.entity.JsonResult;

@Service
public class ActivityService {
	Logger logger = LoggerFactory.getLogger(ActivityService.class);
	@Resource
	ActivityPrizeMapper activityPrizeDao;
	@Resource
	ActivityPrizeCustomerMapper activityPrizeCustomerDao;
	@Resource
	ActivityChanceCustomerMapper activityChanceCustomerDao;
	@Resource
	ActivityHelpMapper activityHelpDao;
//	查询奖品列表接口
	public List<ActivityPrize> selectByActivityPrize() {
		return activityPrizeDao.selectByActivityPrize(null);
	}
//	查询奖品列表接口
	public List<ActivityPrize> selectAllPrize() {
		return activityPrizeDao.selectAllPrize();
	}
//	更变奖品领取状态
	public int setPrizeState(Integer id,Integer status) {
		ActivityPrizeCustomer activityPrizeCustomer = new ActivityPrizeCustomer();
		activityPrizeCustomer.setId(id);
		activityPrizeCustomer.setStatus(status);
		return activityPrizeCustomerDao.updateByPrimaryKeySelective(activityPrizeCustomer);
	}
	
//	签到接口
	public ActivityPrize dailySign(int customerId) {
		ActivityChanceCustomer record = activityChanceCustomerDao.selectByPrimaryKey(customerId);
		if(record==null){//插入一条活动数据
			record = new ActivityChanceCustomer();
			record.setCustomerId(customerId);
			record.setLotteryTime(new Date());
			record.setIsBlessRegist(0);
			record.setGirlfriendsHelp(0);
			record.setIsSignIn(1);
			record.setTotalLottery(0);
			record.setUsedLottery(0);
			record.setBigLottery(0);
			activityChanceCustomerDao.insert(record);
			ActivityPrize selectePrize = extract(1,isTowPeiQi(customerId));
			int randomCode = (int)(Math.random()*900000+100000);
			//记录抽奖
			ActivityPrizeCustomer activityPrizeCustomer = new ActivityPrizeCustomer();
			activityPrizeCustomer.setCreateTime(new Date());
			activityPrizeCustomer.setCustomerId(customerId);
			activityPrizeCustomer.setPrizeId(selectePrize.getId());
			activityPrizeCustomer.setStatus(1);
			activityPrizeCustomer.setPrizeCode(randomCode);
			activityPrizeCustomerDao.insertSelective(activityPrizeCustomer);
	        return selectePrize;
		}else{//更新签到次数,且增加抽奖机会
			SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
			Date nowdate = null;
			try {
				nowdate = format.parse(format.format(new Date()));
			} catch (ParseException e) {
			}
			if(record.getLotteryTime()!=null&&(record.getLotteryTime().getTime()<nowdate.getTime()||record.getIsSignIn()==0)){
				//签到
				record.setLotteryTime(new Date());
				record.setGirlfriendsHelp(0);
				record.setIsSignIn(1);
				record.setTotalLottery(0);
				record.setUsedLottery(0);
				activityChanceCustomerDao.updateByPrimaryKeySelective(record);
				ActivityPrize selectePrize = extract(1,isTowPeiQi(customerId));
				int randomCode = (int)(Math.random()*900000+100000);
				//记录抽奖
				ActivityPrizeCustomer activityPrizeCustomer = new ActivityPrizeCustomer();
				activityPrizeCustomer.setCreateTime(new Date());
				activityPrizeCustomer.setCustomerId(customerId);
				activityPrizeCustomer.setPrizeId(selectePrize.getId());
				activityPrizeCustomer.setStatus(1);
				activityPrizeCustomer.setPrizeCode(randomCode);
				activityPrizeCustomerDao.insertSelective(activityPrizeCustomer);
		        return selectePrize;
			}else{
				//今日已经签到
				return null;
			}
		}
	}
	
//	助力接口
	public int girlFriendsHelp(int customerId,int accept_id){
		ActivityHelp activityHelp = new ActivityHelp();
		activityHelp.setHelpId(customerId);
		activityHelp.setAcceptId(accept_id);
		ActivityHelp activityHelpSelect = activityHelpDao.selectByActivityHelp(activityHelp);
		if(activityHelpSelect!=null){
			return -2;
		}
		ActivityChanceCustomer record = activityChanceCustomerDao.selectByPrimaryKey(accept_id);
		if(record==null){//插入一条活动数据
			record = new ActivityChanceCustomer();
			record.setCustomerId(accept_id);
			record.setLotteryTime(new Date());
			record.setIsBlessRegist(0);
			record.setGirlfriendsHelp(1);
			record.setIsSignIn(0);
			record.setTotalLottery(1);
			record.setUsedLottery(0);
			record.setBigLottery(0);
			return activityChanceCustomerDao.insert(record);
		}else{//更新助力次数,且增加抽奖机会
			SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
			Date nowdate = null;
			try {
				nowdate = format.parse(format.format(new Date()));
			} catch (ParseException e) {
			}
			if(record.getLotteryTime()!=null&&record.getLotteryTime().getTime()<nowdate.getTime()){
				//助力
				record.setLotteryTime(new Date());
				record.setGirlfriendsHelp(1);
				record.setIsSignIn(0);
				record.setTotalLottery(1);
				record.setUsedLottery(0);
				return activityChanceCustomerDao.updateByPrimaryKeySelective(record);
			}else if(record.getLotteryTime()!=null&&record.getLotteryTime().getTime()>nowdate.getTime()&&record.getGirlfriendsHelp()<30){
				//5次以内的助力
				record.setGirlfriendsHelp(record.getGirlfriendsHelp()+1);
				record.setTotalLottery(record.getTotalLottery()+1);
				return activityChanceCustomerDao.updateByPrimaryKeySelective(record);
			}else if(record.getLotteryTime()!=null&&record.getLotteryTime().getTime()>nowdate.getTime()&&record.getGirlfriendsHelp()>=30){
				//助力超过5次
				return -1;
			}else{
				return 0;
			}
		}
	}

//	记录谁帮谁助力接口	
	public int GirlFriendsHelpRecord(int customerId,int accept_id){
		ActivityHelp record = new ActivityHelp();
		record.setHelpId(customerId);
		record.setAcceptId(accept_id);
		record.setHelpTime(new Date());
		record.setActivityId(1);
		return activityHelpDao.insertSelective(record);
	}

//	查询开福次数
	public ActivityChanceCustomer getActivityChanceCustomer(int customerId){
		return activityChanceCustomerDao.selectByPrimaryKey(customerId);
	}
//	开福接口
	public JsonResult openFoka(int customerId) {
		//判断是否有抽奖机会
		ActivityChanceCustomer activityChanceCustomer = activityChanceCustomerDao.selectByPrimaryKey(customerId);
		if(activityChanceCustomer!=null){
			int chance =0;
			if(activityChanceCustomer.getIsBlessRegist()==-1){
				chance =activityChanceCustomer.getTotalLottery()-activityChanceCustomer.getUsedLottery();
			}else{
				chance =activityChanceCustomer.getIsBlessRegist()+ activityChanceCustomer.getTotalLottery()-activityChanceCustomer.getUsedLottery();
			}
			
			if(activityChanceCustomer.getUsedLottery()>=30&&activityChanceCustomer.getTotalLottery()>=30){
				return new JsonResult(false,"今日助力次数已用完", null);
			}else if(chance>0){
				
			}else{
				return new JsonResult(false,"福气耗尽,快去邀请闺蜜助我开福", null);
			}
		}else{
			return new JsonResult(false,"邀请闺蜜助力", null);
		}
		//判断是否删除佩奇概率
//		activityChanceCustomer.get
		//调用抽卡方法
		ActivityPrize selectePrize = extract(1,isTowPeiQi(customerId));
		int randomCode = (int)(Math.random()*900000+100000);
		selectePrize.setPrizeRemark(String.format(selectePrize.getPrizeRemark(),randomCode));
		if(activityChanceCustomer.getIsBlessRegist()==1){
			//福气加特可抽次数
			activityChanceCustomer.setIsBlessRegist(-1);;
			activityChanceCustomerDao.updateByPrimaryKeySelective(activityChanceCustomer);
		}else if(activityChanceCustomer.getIsBlessRegist()>0){
			activityChanceCustomer.setIsBlessRegist(activityChanceCustomer.getIsBlessRegist()-1);
			activityChanceCustomerDao.updateByPrimaryKeySelective(activityChanceCustomer);
		}else{
			//扣减闺蜜助力可抽次数
			activityChanceCustomer.setUsedLottery(activityChanceCustomer.getUsedLottery()+1);
			activityChanceCustomerDao.updateByPrimaryKeySelective(activityChanceCustomer);
		}
		//记录抽奖
		ActivityPrizeCustomer activityPrizeCustomer = new ActivityPrizeCustomer();
		activityPrizeCustomer.setCreateTime(new Date());
		activityPrizeCustomer.setCustomerId(customerId);
		activityPrizeCustomer.setPrizeId(selectePrize.getId());
		activityPrizeCustomer.setStatus(1);
		activityPrizeCustomer.setPrizeCode(randomCode);
		activityPrizeCustomerDao.insertSelective(activityPrizeCustomer);
        return new JsonResult(true, JsonResult.APP_DEFINE_SUC, selectePrize);
	}
//	开五福大奖
	public JsonResult openBigFoka(int customerId) {
		//判断是否有抽奖机会
		ActivityChanceCustomer activityChanceCustomer = activityChanceCustomerDao.selectByPrimaryKey(customerId);
		if(activityChanceCustomer!=null&&activityChanceCustomer.getBigLottery()>0){
			
		}else{
			return new JsonResult(false,"您没有可打开的五福大奖.", null);
		}
		//调用抽卡方法
		ActivityPrize selectePrize = extract(2,false);
		int randomCode = (int)(Math.random()*900000+100000);
		selectePrize.setPrizeRemark(String.format(selectePrize.getPrizeRemark(),randomCode));
		activityChanceCustomer.setBigLottery(activityChanceCustomer.getBigLottery()-1);
		activityChanceCustomerDao.updateByPrimaryKeySelective(activityChanceCustomer);
		//设置一张五福卡为状态为3
		activityPrizeCustomerDao.setFokaState3(customerId, 23);
		//插入一张已开奖
		ActivityPrizeCustomer activityPrizeYikai = new ActivityPrizeCustomer();
		activityPrizeYikai.setCustomerId(customerId);
		activityPrizeYikai.setPrizeId(24);
		activityPrizeYikai.setCreateTime(new Date());
		activityPrizeYikai.setStatus(1);
		activityPrizeCustomerDao.insertSelective(activityPrizeYikai);
		
		//记录抽奖
		ActivityPrizeCustomer activityPrizeCustomer = new ActivityPrizeCustomer();
		activityPrizeCustomer.setCreateTime(new Date());
		activityPrizeCustomer.setCustomerId(customerId);
		activityPrizeCustomer.setPrizeId(selectePrize.getId());
		activityPrizeCustomer.setStatus(1);
		activityPrizeCustomer.setPrizeCode(randomCode);
		activityPrizeCustomerDao.insertSelective(activityPrizeCustomer);
        return new JsonResult(true, JsonResult.APP_DEFINE_SUC, selectePrize);
	}
    
	public ActivityPrize extract(int type,Boolean deletePeiqi) {
		//查出概率
		ActivityPrize ap = new ActivityPrize();
		ap.setType(type);
		List<ActivityPrize> list = activityPrizeDao.selectByActivityPrize(ap);
		int j0 = list.size();
		if(type==1&&deletePeiqi){//限制最多两个佩奇猪
			for(int i=0;i<j0;i++){
				ActivityPrize activityPrize = list.get(i);
				if(activityPrize.getId()==5){
					list.remove(i);
					break;
				}
			}
		}
		
//		把所有的概率加起来,得到总和
		Double sum = 0.0;
		int j = list.size();
		for(int i=0;i<j;i++){
			ActivityPrize activityPrize = list.get(i);
			sum+=activityPrize.getPrizeWeight();
		}
//		获取一个随机数
		Double randem = Math.random()*sum;
//		判断是在那一段就是抽中谁
		ActivityPrize  selectePrize = null;
		Double prizeWeight = 0.0;
		for(int i=0;i<j;i++){
			ActivityPrize activityPrize = list.get(i);
			if(randem<activityPrize.getPrizeWeight()+prizeWeight){
				//抽到后跳出
				selectePrize = activityPrize;
				break;
			}else{//不中
				prizeWeight+=activityPrize.getPrizeWeight();
			}
		}
		//减少福卡数量
		selectePrize.setPrizeAmount(selectePrize.getPrizeAmount()-1);
		activityPrizeDao.updateByPrimaryKeySelective(selectePrize);
		return selectePrize;
	}

	

	
	//	我的福卡列表
	public List<ActivityPrize> getMyFokas(int customerId) {
		List<ActivityPrize> list = activityPrizeDao.getMyFokas(customerId);
		return list;
	}
//	店铺查获奖列表myPrizeList
	public List<ActivityPrize>  getPrizeList(String nickname,String phone,Integer pid,Integer prizeCode) {
		return activityPrizeCustomerDao.getPrizeList(nickname,phone,pid,prizeCode);
	}
//	我的列表myPrizeList
	public List<ActivityPrize>  myPrizeList(Integer customerId) {
		return activityPrizeCustomerDao.myPrizeList(customerId);
	}
//	店铺查获奖列表
	public List<ActivityPrize>  getNewPrizeList() {
		return activityPrizeCustomerDao.getNewPrizeList();
	}
	
	
//	五张福卡合成大奖接口
	public int mergeFokas() {
		//查询所有人的所有的福卡量
		List<ActivityPrizeCustomer> list = activityPrizeCustomerDao.getAllFokas();
		//统计每个人的大奖数量
		Map<Integer,ActivityPrizeCustomer> map = new HashMap<Integer, ActivityPrizeCustomer>();
		int listsize = list.size();
		for(int i=0;i<listsize;i++){
			ActivityPrizeCustomer activityPrizeCustomer = list.get(i);
			System.out.println(activityPrizeCustomer.getCustomerId());
			if(!map.containsKey(activityPrizeCustomer.getCustomerId())){
				map.put(activityPrizeCustomer.getCustomerId(), activityPrizeCustomer);
			}
			ActivityPrizeCustomer mapActivityPrizeCustomer =  map.get(activityPrizeCustomer.getCustomerId());
			mapActivityPrizeCustomer.setPrizeTypeNum(mapActivityPrizeCustomer.getPrizeTypeNum()+1);
			if(mapActivityPrizeCustomer.getNum()>activityPrizeCustomer.getNum()){
				mapActivityPrizeCustomer.setNum(activityPrizeCustomer.getNum());
			}
		}
		//遍历去掉没有五福的用户
		
		Iterator<Integer> it = map.keySet().iterator();
		int num = 0;
		while(it.hasNext()){
			int id = it.next();
			ActivityPrizeCustomer activityPrizeCustomer = map.get(id);
			if(activityPrizeCustomer.getPrizeTypeNum()==5&&activityPrizeCustomer.getNum()>0){
				//合成且提交
				num++;
				for(int i=0;i<activityPrizeCustomer.getNum();i++){
					//福状态设为3
					
					activityPrizeCustomerDao.setFokaState3(activityPrizeCustomer.getCustomerId(),1);
					activityPrizeCustomerDao.setFokaState3(activityPrizeCustomer.getCustomerId(),2);
					activityPrizeCustomerDao.setFokaState3(activityPrizeCustomer.getCustomerId(),3);
					activityPrizeCustomerDao.setFokaState3(activityPrizeCustomer.getCustomerId(),4);
					activityPrizeCustomerDao.setFokaState3(activityPrizeCustomer.getCustomerId(),5);
					
					//添加五福未开奖
					ActivityPrizeCustomer apc = new ActivityPrizeCustomer();
					apc.setCustomerId(activityPrizeCustomer.getCustomerId());
					apc.setPrizeId(23);
					apc.setCreateTime(new Date());
					apc.setStatus(1);
					activityPrizeCustomerDao.insertSelective(apc);
				}
				ActivityChanceCustomer activityChanceCustomer =activityChanceCustomerDao.selectByPrimaryKey(id);
				activityChanceCustomer.setBigLottery(activityChanceCustomer.getBigLottery()+activityPrizeCustomer.getNum());
				activityChanceCustomerDao.updateByPrimaryKeySelective(activityChanceCustomer);
			}
		}
		return num;
	}

	//	判断是否关注公众号接口

//提交个人信息按钮
	//	福气加特
	public int setJiaTe(int customerId) {
		ActivityChanceCustomer activityChanceCustomer = activityChanceCustomerDao.selectByPrimaryKey(customerId);
		if(activityChanceCustomer==null){
			ActivityChanceCustomer record = new ActivityChanceCustomer();
			record.setCustomerId(customerId);
			record.setLotteryTime(new Date());
			record.setIsBlessRegist(2);
			record.setGirlfriendsHelp(0);
			record.setIsSignIn(0);
			record.setTotalLottery(0);
			record.setUsedLottery(0);
			record.setBigLottery(0);
			activityChanceCustomerDao.insertSelective(record);
			return 1;
		}else if(activityChanceCustomer.getIsBlessRegist()==0){
			activityChanceCustomer.setIsBlessRegist(2);
			return activityChanceCustomerDao.updateByPrimaryKeySelective(activityChanceCustomer);
		}else{
			return 0;
		}
	}
	public Boolean isTowPeiQi(Integer customerId) {
		ActivityPrize activityPrize = activityPrizeDao.getMyPeiQi(customerId);
		if(activityPrize.getNum()>1){
			return true;
		}else{
			return false;
		}
	}
    public int  mergeCustomer(int fromCustomerId,int toCustomerId) {
    	ActivityChanceCustomer c = activityChanceCustomerDao.selectByPrimaryKey(toCustomerId);
    	if(c!=null){
    		logger.error("两目标customerId已经存在.");
    		return 0;
    	}else{
    		CustomerBalance cc = new CustomerBalance();
    		cc.setCustomerId(fromCustomerId);
    		activityChanceCustomerDao.mergeCustomer(fromCustomerId,toCustomerId);
    	}
    	activityHelpDao.mergeCustomer(fromCustomerId,toCustomerId);
    	activityHelpDao.mergeCustomer2(fromCustomerId,toCustomerId);
    	return activityPrizeCustomerDao.mergeCustomer(fromCustomerId,toCustomerId);
    }

}
