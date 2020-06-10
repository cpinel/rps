package com.dsv.rps.resources;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Component;

	@Component
	@PropertySource("classpath:application.properties")
	public class Config {

		
	    public String getConfigValue(String configKey)
	    {
	    	AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
	    	
	    	ConfigurableEnvironment env = context.getEnvironment();
	        return env.getProperty(configKey);
	    }
	}
	
	