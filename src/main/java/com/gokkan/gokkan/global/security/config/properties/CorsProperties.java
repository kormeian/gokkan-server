package com.gokkan.gokkan.global.security.config.properties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//@ConfigurationProperties(prefix = "cors")
public class CorsProperties {

	private String allowedOrigins;
	private String allowedMethods;
	private String allowedHeaders;
	private Long maxAge;
}

