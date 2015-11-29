package com.daghan.iot.resource.impl.provider2;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.metatype.annotations.Designate;

import com.daghan.iot.core.api.Device;
import com.daghan.iot.core.api.MethodTypeEnum.GetMethod;

@Component
@Designate(ocd = ReaderConfigCompanyB.class, factory = true)
public class ReaderDeviceImplementation implements Device {
	ReaderConfigCompanyB config;

	@Activate
	public void activate(ReaderConfigCompanyB config) {
		this.config = config;
	}

	@GetMethod
	String readValue(String command) {
		return config.getCompanyName() + command;
	}

	// Override to make it more user friendly and make the configuration easy
	@Override
	public String getId() {
		return config.getName();
	}
}
