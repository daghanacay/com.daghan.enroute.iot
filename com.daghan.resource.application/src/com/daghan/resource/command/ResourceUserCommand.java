package com.daghan.resource.command;

import java.io.Serializable;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

import com.daghan.iot.resource.manager.ResourceRunner;

import osgi.enroute.debug.api.Debug;

@ObjectClassDefinition(name = "Resource Command Config")
@interface ResourceCommandConfig {
	@AttributeDefinition(name = "Printer resource", description = "Assigns output pipe for writing strings.")
	String resourceName() default "";
}

@Component(immediate = true, property = { Debug.COMMAND_SCOPE + "=res", Debug.COMMAND_FUNCTION + "=write" })
@Designate(ocd = ResourceCommandConfig.class)
public class ResourceUserCommand implements Serializable {
	private String resourceUrl;
	private ResourceRunner rm;

	@Activate
	public void activate(ResourceCommandConfig config){
		resourceUrl = config.resourceName();
	}
	
	public void write(String str) throws Exception {
		rm.activateResource(resourceUrl, str); 
	}

	@Reference
	void setResourceManager(ResourceRunner rm) {
		this.rm = rm;
	}
}
