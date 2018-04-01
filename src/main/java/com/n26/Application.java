package com.n26;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class Application extends SpringBootServletInitializer {

	 	private static final Class<Application> applicationClass = Application.class;
	    private static final Logger log = LogManager.getLogger(applicationClass);

		public static void main(String[] args) {
			log.info("Starting n26 application");
			SpringApplication.run(applicationClass, args);
		}

	    @Override
	    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
	        return application.sources(applicationClass);
	    }
	
	
}
