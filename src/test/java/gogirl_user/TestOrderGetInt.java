package gogirl_user;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.NumberFormat;

//
public class TestOrderGetInt {
	//字符串中所有字符相加得到一个int
//	public static int orderIdGetInt(String orderId) {
//		StringBuffer sb = new StringBuffer(orderId);
//		int sum = 0;
//		int length = sb.length();
//		for(int i=0;i<length;i++){
//			sum+=sb.charAt(i);
//		}
//		return sum;
//	}
	public static void main(String[] args) {
//		TestOrderGetInt.orderIdGetInt("as");
//        Double a = 1.0;
//Double b = 3.0;
//BigDecimal bi1 = new BigDecimal(a.toString());
//BigDecimal bi2 = new BigDecimal(b.toString());
//BigDecimal divide = bi1.divide(bi2, 4, RoundingMode.HALF_UP);
//
//System.out.println(divide.doubleValue());
//
//NumberFormat percent = NumberFormat.getPercentInstance();
//percent.setMaximumFractionDigits(2);


//System.out.println(percent.format(divide));
BigDecimal bb = new BigDecimal("123.1");
BigDecimal bb2 = new BigDecimal("100");

System.out.println(bb.multiply(bb2).setScale( 0, BigDecimal.ROUND_DOWN ));
System.out.println(bb2.toString());

//long l  = bd.setScale( 0, BigDecimal.ROUND_DOWN ).longValue(); // 向下取整
	}

}
