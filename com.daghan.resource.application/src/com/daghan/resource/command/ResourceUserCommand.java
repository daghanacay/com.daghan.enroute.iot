package com.daghan.resource.command;

import java.io.Serializable;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.daghan.iot.resource.manager.ResourceRunner;

import osgi.enroute.debug.api.Debug;

@Component(immediate = true, property = { Debug.COMMAND_SCOPE + "=res", Debug.COMMAND_FUNCTION + "=write" })
public class ResourceUserCommand implements Serializable {
	ResourceRunner rm;

	public void write(String str) throws Exception {
		System.out.println(str);
		rm.activateResource("/output/sysout/1", str, String.class); 
	}

	@Reference
	void setResourceManager(ResourceRunner rm) {
		this.rm = rm;
	}
}
