package com.ljk.property;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

import lombok.Getter;

@Getter
@Configuration
@PropertySources({
	@PropertySource(value = "classpath:/static/property/config.properties", ignoreResourceNotFound = true)
})
public class GlobalProperty {
	@Value("${spring.datasource.driverClassName}")
	private String driverClassName;
	
	@Value("${spring.datasource.url}")
	private String url;
	
	@Value("${spring.datasource.username}")
	private String username;
	
	@Value("${spring.datasource.password}")
	private String password;
	
	private Logger LOGGER = LoggerFactory.getLogger(GlobalProperty.class);
	
	@PostConstruct
	public void postGlobalPropertySource() {
		LOGGER.debug("driverClassName : " + driverClassName);
		LOGGER.debug("url : " + url);
		LOGGER.debug("username : " + username);
	}
}
