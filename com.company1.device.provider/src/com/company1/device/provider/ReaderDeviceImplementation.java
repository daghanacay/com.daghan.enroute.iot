package com.company1.device.provider;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.metatype.annotations.Designate;

import com.daghan.iot.core.api.Device;
import com.daghan.iot.core.api.MethodTypeEnum.GetMethod;
import com.daghan.iot.core.api.MethodTypeEnum.PostMethod;

@Component
@Designate(ocd = ReaderDeviceConfig.class, factory = true)
public class ReaderDeviceImplementation implements Device {
	ReaderDeviceConfig config;

	@Activate
	public void activate(ReaderDeviceConfig config) {
		this.config = config;
	}

	@GetMethod
	private String getValue() {
		return config.getPrependString();
	}
	
	@PostMethod
	private String writeValue(String str) {
		return config.getPrependString() + str;
	}

	@Override
	public String getId() {
		return config.getName();
	}

}
