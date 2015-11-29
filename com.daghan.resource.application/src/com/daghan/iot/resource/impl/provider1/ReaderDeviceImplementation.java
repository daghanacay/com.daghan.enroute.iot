package com.daghan.iot.resource.impl.provider1;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.metatype.annotations.Designate;

import com.daghan.iot.core.api.Device;
import com.daghan.iot.core.api.MethodTypeEnum.GetMethod;

@Component
@Designate(ocd = ReaderDeviceConfig.class, factory = true)
public class ReaderDeviceImplementation implements Device {
	ReaderDeviceConfig config;

	@Activate
	public void activate(ReaderDeviceConfig config) {
		this.config = config;
	}

	@GetMethod
	private String getValue(String command) {
		return config.getPrependString() + command;
	}

	@Override
	public String getId() {
		return config.getName();
	}

}
