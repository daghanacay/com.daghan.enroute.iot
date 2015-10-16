package com.daghan.iot.resource.manager;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "Resource Config")
public @interface ResourceConfig {
	@AttributeDefinition(name = "Resource Url", description = "Provides the Url configuration for this resource")
	String url() default "";
}
