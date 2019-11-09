package com.gogirl.gogirl_user.entity;

import java.util.Map;

public class JsonResult {

	public static final boolean APP_REQ_CODE_ERROR = false;// 请求返回码-失败
	public static final boolean APP_REQ_CODE_SUCCESS = true;// 请求返回码-成功

	public static final String APP_DEFINE_ERR = "参数错误或者操作未成功";// 返回信息-失败
	public static final String APP_DEFINE_SUC = "操作成功";// 返回信息-成功
	public static final String APP_DEFINE_URL_ERR = "请求地址错误";// 返回信息-地址错误
	public static final String APP_DEFINE_SIGN_ERR = "参数签名验证未通过";// 返回信息-参数签名验证未通过
	public static final String APP_DEFINE_EXCEPTION = "操作未成功";// 返回信息-服务器发生异常
	public static final String APP_DEFINE_PARAM_ERR = "参数%s为空,请检查入参";// 失败
//	public static final String APP_DEFINE_PARAM_ERR = "订单标识为空,请传入orderId";// 失败
	public static final String APP_DEFINE_NICKNAME_ERR = "用户昵称转换出错";// 失败
	public static final String APP_OPENID_ERR = "无该openid的用户";// 失败
	public static final String APP_PHONE_BE_BIND_ERR = "该手机号码已被其他账号绑定";// 失败
	public static final String APP_CUSTOMER_HAVE_CARD = "该手机号码已有会员卡";// 失败
	
	
	private Boolean success;
	private String message;
	private Object data;
	
	public JsonResult() {
		this.message = APP_DEFINE_ERR;
		this.success = APP_REQ_CODE_ERROR;
		
	}
	public JsonResult(boolean success){
		this.success = success;
	}
	public JsonResult(Boolean success, String message, Object data) {
		super();
		this.success = success;
		this.message = message;
		this.data = data;
	}

	public Boolean getSuccess() {
		return success;
	}
	public void setSuccess(Boolean success) {
		this.success = success;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	@Override
	public String toString() {
		return "JsonResult [success=" + success + ", message=" + message
				+ ", data=" + data + "]";
	}
	
	
}
