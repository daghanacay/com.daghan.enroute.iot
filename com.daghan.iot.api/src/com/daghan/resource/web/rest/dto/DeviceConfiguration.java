package com.daghan.resource.web.rest.dto;

import java.util.List;
import java.util.Map.Entry;

public class DeviceConfiguration {
	// to be used by Osgi Rest as DTO the fields of the class should be public
	public String pidString;
	public List<String> childPids;

	public DeviceConfiguration(String pidString, List<String> childPids) {
		this.pidString = pidString;
		this.childPids = childPids;
	}

}
