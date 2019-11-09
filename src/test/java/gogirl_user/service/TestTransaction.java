package gogirl_user.service;

import java.util.Date;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.gogirl.gogirl_user.GogirlUserApplication;
import com.gogirl.gogirl_user.entity.CustomerBalance;
import com.gogirl.gogirl_user.entity.CustomerBalanceRecord;
import com.gogirl.gogirl_user.service.CustomerBalanceService;

//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest(classes=GogirlUserApplication.class)
////@RunWith(SpringRunner.class)
////@SpringBootTest
//public class TestTransaction {
//	@Resource
//	CustomerBalanceService service;
////	@Test
////	public void test() {
////		service.testTransaction();
////		System.out.println("end");
////	}
////	@Test
////	public void testLock() {
////		System.out.println("start");
////		service.testLock();
////		System.out.println("end");
////	}
//}
////以下代码为service示例，放在service文件中：
////
////public void testLock() {
//////	//插入数据
//////	for(int i=2;i<10;i++){
//////		balanceDao.insert(new CustomerBalance(i,1000, new Date(), new Date(), 1000, 00, 0));
//////	}
////	int id = (int)(Math.random()*10);
////	//不同id获取id锁更新数据
//////	for(int i=0;i<10;i++){
////	balanceLock.lock(id);
////	System.out.println("为用户"+id+"加锁");
////		CustomerBalance balance= balanceDao.selectByPrimaryKey(id);
////		//睡觉
////		try {
////			Thread.currentThread().sleep(3000);
////		} catch (InterruptedException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
////		balance.setBalance(balance.getBalance()+100);
////		balance.setTotalCharge(balance.getTotalCharge()+100);
////		balance.setUpdateTime(new Date());
////		balance.setVersion(balance.getVersion()+1);
////		balanceDao.updateByPrimaryKeySelective(balance);
////		System.out.println("客户"+id+"充值100元");
////		System.out.println("为用户"+id+"解锁。");
////		balanceLock.unlock(id);
//////	}
////}
////@Transactional
////public void testTransaction() {
////	System.out.println("in test");
//////	for(int i=0;i<2;i++){
//////		CustomerBalanceRecord balanceRecord = new CustomerBalanceRecord(i, 2, 1, new Date(), 1000, "0.0.0.0", "事务测试");
//////		balanceRecordDao.insertSelective(balanceRecord);
//////	}
////	try {
////		balanceRecordDao.insertSelective(	new CustomerBalanceRecord(1, 2, 1, new Date(), 1000, "0.0.0.0", "事务测试")	);
////		balanceRecordDao.insertSelective(	new CustomerBalanceRecord(2, 2, 1, new Date(), 1000, "0.0.0.0", "事务测试")	);
////		balanceRecordDao.insertSelective(	new CustomerBalanceRecord(3, 2, 1, new Date(), 1000, "0.0.0.0", "事务测试")	);
////		balanceRecordDao.insertSelective(	new CustomerBalanceRecord(4, 2, 1, new Date(), 1000, "0.0.0.0", "事务测试")	);
////		balanceRecordDao.insertSelective(	new CustomerBalanceRecord(5, 2, 1, new Date(), 1000, "0.0.0.0", "事务测试")	);
////		balanceRecordDao.insertSelective(	new CustomerBalanceRecord(6, 2, 1, new Date(), 1000, "0.0.0.0", "事务测试")	);
////		balanceRecordDao.insertSelective(	new CustomerBalanceRecord(7, 2, 1, new Date(), 1000, "111.111.111.111.111", "事务测试")	);
////		balanceRecordDao.insertSelective(	new CustomerBalanceRecord(8, 2, 1, new Date(), 1000, "0.0.0.0", "事务测试")	);
////		balanceRecordDao.insertSelective(	new CustomerBalanceRecord(9, 2, 1, new Date(), 1000, "0.0.0.0", "事务测试")	);
////	} catch (Exception e) {
////		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//手动回滚
////		System.out.println("插入数据异常");
////	}
////}