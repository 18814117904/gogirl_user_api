package gogirl_user.controller;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gogirl.gogirl_user.GogirlUserApplication;
import com.gogirl.gogirl_user.controller.BalanceController;
import com.gogirl.gogirl_user.service.CustomerBalanceService;
import com.gogirl.gogirl_user.service.balance.ChargeBalance;
import com.gogirl.gogirl_user.service.balance.ConsumeBalance;

//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest(classes=GogirlUserApplication.class)
//public class TestBalance {
//	@Resource
//	BalanceController controller;
//	@Resource
//	ChargeBalance chargeBalance;
//	@Resource
//	ConsumeBalance consumeBalance;
//	@Test
//	public void test() {
//		System.out.println(controller.getBalance(request, customer_id)e(13));
//		service.balanceChange(12, 100000,chargeBalance, 1);
//		System.out.println(service.getBalance(13));
//		service.balanceChange(12, 100000,chargeBalance, 1);
//		System.out.println(service.getBalance(13));
//		System.out.println("end");
//	}
//}
