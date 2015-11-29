package com.daghan.resource.command;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

import com.daghan.iot.core.api.MethodTypeEnum;
import com.daghan.iot.resource.manager.ResourceRunner;

import osgi.enroute.debug.api.Debug;

@ObjectClassDefinition(name = "Resource Command Config")
@interface ResourceCommandConfig {
	@AttributeDefinition(name = "Command resource", description = "Assigns a resource with a GET method.")
	String[] getResourceNamesGet() default { "" };
}

@Component(service = ResourceUserCommand.class, immediate = true, property = { Debug.COMMAND_SCOPE + "=res",
		Debug.COMMAND_FUNCTION + "=get" })
@Designate(ocd = ResourceCommandConfig.class)
public class ResourceUserCommand {
	ResourceCommandConfig config;
	private ResourceRunner rm;

	@Activate
	public void activate(ResourceCommandConfig config) {
		this.config = config;
	}

	public void get(String str) throws Exception {
		for (String tmp : config.getResourceNamesGet()) {
			String output = rm.activateResource(tmp, str, String.class, MethodTypeEnum.GET);
			System.out.println(output);
		}
	}

	@Reference
	void setResourceManager(ResourceRunner rm) {
		this.rm = rm;
	}
}
