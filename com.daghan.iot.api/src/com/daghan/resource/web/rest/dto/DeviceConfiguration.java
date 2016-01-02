package com.daghan.resource.web.rest.dto;

import java.util.Map.Entry;

public class DeviceConfiguration {
	public Long key;
	public String factoryPid;
	public DeviceConfiguration(Long key, String factoryPid) {
		this.key = key;
		this.factoryPid =factoryPid;
	}

}
