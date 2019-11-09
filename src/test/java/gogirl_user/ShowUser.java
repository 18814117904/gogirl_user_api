package gogirl_user;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import weibo4j.Users;
import weibo4j.model.User;
import weibo4j.model.WeiboException;
import weibo4j.util.WeiboConfig;

public class ShowUser {

	public static void main(String[] args) throws ParseException {
//		String access_token = "2.00UO6D_H0KidH51ca8f50e2bcaN2IB";
//		String uid = "6694492190";
//		Users um = new Users(access_token);
//		try {
//			User user = um.showUserById(uid);
//			System.out.println(user.toString());
//		} catch (WeiboException e) {
//			e.printStackTrace();
//		}
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		Date nowdate=new Date();
		String strdate = format.format(nowdate);
Date d2=format.parse(strdate);

		System.out.println(d2);
	}
}