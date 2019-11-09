package com.gogirl.gogirl_user.constant;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;





import com.gogirl.gogirl_user.entity.GogirlConfig;
import com.gogirl.gogirl_user.service.GogirlConfigService;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * wechat mp properties
 *
 * @author Binary Wang(https://github.com/binarywang)
 */
@Component
@ConfigurationProperties(prefix = "gogirl")
public class GogirlProperties {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	@Bean
	public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        return restTemplate;
	}
	private String GOGIRLORDER;
	private String GOGIRLMP;
	
	@Resource
	GogirlConfigService gogirlConfigService;
	@PostConstruct
	public void init(){
		List<GogirlConfig> list = gogirlConfigService.selectByType("gogirl_user");
		Map<String, String> map  = new HashMap<String, String>();
		for(int i = 0 ;i<list.size();i++){
			GogirlConfig item = list.get(i);
			map.put(item.getName(), item.getValue());
		}
		GOGIRLMP=map.get("mp_url");
		GOGIRLORDER=map.get("order_url");
		logger.info("成功加载gogirl_config表配置:"+list.toString());
	}	
	
	
	
	public String getGOGIRLORDER() {
		return GOGIRLORDER;
	}
	public void setGOGIRLORDER(String gOGIRLORDER) {
		GOGIRLORDER = gOGIRLORDER;
	}
	public String getGOGIRLMP() {
		return GOGIRLMP;
	}
	public void setGOGIRLMP(String gOGIRLMP) {
		GOGIRLMP = gOGIRLMP;
	}


}
