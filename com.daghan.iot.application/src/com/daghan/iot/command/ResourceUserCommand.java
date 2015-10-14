package com.daghan.iot.command;

import java.io.Serializable;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;

import com.daghan.iot.resource.manager.ResourceManagerImpl;

import osgi.enroute.debug.api.Debug;

@Component(immediate = true, property = { Debug.COMMAND_SCOPE + "=res", Debug.COMMAND_FUNCTION + "=write" })
public class ResourceUserCommand implements Serializable{
	ResourceManagerImpl rm;

	public void write(String str) throws Exception {
		rm.activateResource("/output/sysout/1", str, String.class);
	}

	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	void setResourceManager(ResourceManagerImpl rm) {
		this.rm = rm;
	}
}
