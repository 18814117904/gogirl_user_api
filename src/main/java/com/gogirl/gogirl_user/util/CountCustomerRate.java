package com.gogirl.gogirl_user.util;

import java.math.BigDecimal;

import com.gogirl.gogirl_user.entity.Customer;
import com.gogirl.gogirl_user.entity.CustomerDetail;

public class CountCustomerRate {
    public Double countDataCompleteRate(Customer c,CustomerDetail record) {
    	BigDecimal rate = new BigDecimal("0");
    	BigDecimal per10 = new BigDecimal(0.1);
    	BigDecimal per4 = new BigDecimal(0.04);

    	if(c.getCustomerSource()!=null&&c.getCustomerSource()!=0){
    		rate = rate.add(per10);
    		rate = rate.add(per10);
    		rate = rate.add(per10);
    	}else{
        	if(c.getStoreRecordRealName()!=null&&!c.getStoreRecordRealName().isEmpty()){
        		rate = rate.add(per10);
        	}
        	if(c.getPhone()!=null&&!c.getPhone().isEmpty()){
        		rate = rate.add(per10);
        	}
    	}
    	if(record!=null&&record.getQuestionnaireImgUrl()!=null&&!record.getQuestionnaireImgUrl().isEmpty()){rate = rate.add(per10);}
    	
//    	if(record!=null&&record.getWechatId           ()!=null&&!record.getWechatId           ().isEmpty()){rate = rate.add(per4);}
//    	if(record!=null&&record.getReferee            ()!=null&&!record.getReferee            ().isEmpty()){rate = rate.add(per4);}
//    	if(record!=null&&record.getFirstServeTime     ()!=null&&!record.getFirstServeTime     ().isEmpty()){rate = rate.add(per4);}
//    	if(record!=null&&record.getBirthday           ()!=null&&!record.getBirthday           ().isEmpty()){rate = rate.add(per4);}
//    	if(record!=null&&record.getMaritalStatus      ()!=null&&!record.getMaritalStatus      ().isEmpty()){rate = rate.add(per4);}
//    	if(record!=null&&record.getHaveChildren       ()!=null&&!record.getHaveChildren       ().isEmpty()){rate = rate.add(per4);}
//    	if(record!=null&&record.getHobby              ()!=null&&!record.getHobby              ().isEmpty()){rate = rate.add(per4);}
//    	if(record!=null&&record.getColourOfSkin       ()!=null&&!record.getColourOfSkin       ().isEmpty()){rate = rate.add(per4);}
//    	if(record!=null&&record.getTypeOfNailBed      ()!=null&&!record.getTypeOfNailBed      ().isEmpty()){rate = rate.add(per4);}
//    	if(record!=null&&record.getNailType           ()!=null&&!record.getNailType           ().isEmpty()){rate = rate.add(per4);}
//    	if(record!=null&&record.getKeyPointsOfSalon   ()!=null&&!record.getKeyPointsOfSalon   ().isEmpty()){rate = rate.add(per4);}
//    	if(record!=null&&record.getLikeStyle          ()!=null&&!record.getLikeStyle          ().isEmpty()){rate = rate.add(per4);}
//    	if(record!=null&&record.getCustomerConcerns   ()!=null&&!record.getCustomerConcerns   ().isEmpty()){rate = rate.add(per4);}
//    	if(record!=null&&record.getSolution           ()!=null&&!record.getSolution           ().isEmpty()){rate = rate.add(per4);}
//    	if(record!=null&&record.getEyelashCondition   ()!=null&&!record.getEyelashCondition   ().isEmpty()){rate = rate.add(per4);}
//    	if(record!=null&&record.getEyeShape           ()!=null&&!record.getEyeShape           ().isEmpty()){rate = rate.add(per4);}
    	if(record!=null&&record.getLearnAboutUs       ()!=null&&!record.getLearnAboutUs       ().isEmpty()){rate = rate.add(per10);}
//    	
//    	if(record!=null&&record.getAge                ()!=null&&record.getAge                ()!=0){rate = rate.add(per4);}
//    	if(record!=null&&record.getNailHardness       ()!=null&&record.getNailHardness       ()!=0){rate = rate.add(per4);}
    	if(record!=null&&record.getResidence          ()!=null&&record.getResidence          ()!=0){rate = rate.add(per10);}
    	if(record!=null&&record.getWorkAddress        ()!=null&&record.getWorkAddress        ()!=0){rate = rate.add(per10);}
//    	if(record!=null&&record.getOccupation         ()!=null&&record.getOccupation         ()!=0){rate = rate.add(per4);}
    	if(record!=null&&record.getFrequencyOfNail    ()!=null&&record.getFrequencyOfNail    ()!=0){rate = rate.add(per10);}
    	
    	if(rate.compareTo(new BigDecimal("1"))>=0){
    		return 1.0;
    	}else{
    		return rate.doubleValue();
    	}
	}

}
