package com.gogirl.gogirl_user.controller;

import java.net.InetAddress;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gogirl.gogirl_user.entity.Coupon;
import com.gogirl.gogirl_user.entity.CustomerNailPhoto;
import com.gogirl.gogirl_user.entity.JsonResult;
import com.gogirl.gogirl_user.service.CountService;
import com.gogirl.gogirl_user.service.CustomerNailPhotoService;
import com.gogirl.gogirl_user.service.CustomerService;

@ResponseBody
@Controller
@RequestMapping("/photo")
public class CustomerNailPhotoCotroller {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Resource
	private CustomerNailPhotoService customerNailPhotoService;

	@RequestMapping("/selectByCustomerNailPhoto")
	public JsonResult selectByCustomerNailPhoto(CustomerNailPhoto record,Integer pageNum,Integer pageSize) {
		if(pageNum==null){
			return new JsonResult(false,String.format(JsonResult.APP_DEFINE_PARAM_ERR, "pageNum"),null);
		}
		if(pageSize==null){
			return new JsonResult(false,String.format(JsonResult.APP_DEFINE_PARAM_ERR, "pageSize"),null);
		}
		logger.info("查询客照列表:"+record.toString());
        PageHelper.startPage(pageNum,pageSize);
		List<CustomerNailPhoto> list = customerNailPhotoService.selectByCustomerNailPhoto(record);
		PageInfo<CustomerNailPhoto> pageInfo = new PageInfo<CustomerNailPhoto>(list);
		return new JsonResult(true, JsonResult.APP_DEFINE_SUC, pageInfo);
	}
	@RequestMapping("/insertOrUpdateSelective")
	public JsonResult insertOrUpdateSelective(CustomerNailPhoto record){
		CustomerNailPhoto isExist = new CustomerNailPhoto();
		isExist.setServiceId(record.getServiceId());
		List<CustomerNailPhoto> list = customerNailPhotoService.selectByCustomerNailPhoto(isExist);
		int row =  0;
		if(list.size()>0){
			for(int j=0;j<list.size();j++){
				record.setId(list.get(j).getId());
				row =  customerNailPhotoService.updateByPrimaryKeySelective(record);
			}
		}else{
			row =  customerNailPhotoService.insertSelective(record);
		}
		return new JsonResult(true, JsonResult.APP_DEFINE_SUC, row);
	}
//	@RequestMapping("/selectByPrimaryKey")
//	public JsonResult selectByPrimaryKey(Integer id) {
//		CustomerNailPhoto record = customerNailPhotoService.selectByPrimaryKey(id);
//		return new JsonResult(true, JsonResult.APP_DEFINE_SUC, record);
//	}
//	
//	@RequestMapping("/insertSelective")
//	public JsonResult insertSelective(CustomerNailPhoto record){
//		int row =  customerNailPhotoService.insertSelective(record);
//		return new JsonResult(true, JsonResult.APP_DEFINE_SUC, row);
//	}
//	@RequestMapping("/updateByPrimaryKeySelective")
//	public JsonResult updateByPrimaryKeySelective(CustomerNailPhoto record){
//		if(record.getId()==null){
//			return new JsonResult(false,String.format(JsonResult.APP_DEFINE_PARAM_ERR,"id"),null);
//		}
//		int row =   customerNailPhotoService.updateByPrimaryKeySelective(record);
//		return new JsonResult(true, JsonResult.APP_DEFINE_SUC, row);
//	}
	@RequestMapping("/delete")
	public JsonResult delete(CustomerNailPhoto record){
		if(record==null){
			return new JsonResult(false,String.format(JsonResult.APP_DEFINE_PARAM_ERR,"所有参数"),null);
		}
		CustomerNailPhoto q = new CustomerNailPhoto();
		q.setServiceId(record.getServiceId());
		List<CustomerNailPhoto> list = customerNailPhotoService.selectByCustomerNailPhoto(q);
		int row =  0;
		for(int i=0;i<list.size();i++){
			customerNailPhotoService.deleteByPrimaryKey(list.get(i).getId());
			row++;
		}
		return new JsonResult(true, JsonResult.APP_DEFINE_SUC, row);
	}
}
