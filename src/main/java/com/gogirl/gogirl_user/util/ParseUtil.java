package com.gogirl.gogirl_user.util;

import java.util.regex.Pattern;

public class ParseUtil {
	  public static boolean isInteger(String str) {  
	        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");  
	        return pattern.matcher(str).matches();  
	  }

}
