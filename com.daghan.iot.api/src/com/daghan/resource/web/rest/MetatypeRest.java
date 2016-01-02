package com.daghan.resource.web.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.daghan.iot.utils.internal.DeviceTracker;
import com.daghan.resource.web.rest.dto.DeviceConfiguration;

import osgi.enroute.rest.api.REST;

@Component
public class MetatypeRest implements REST {
	@Reference
	private DeviceTracker deviceTracker;
	
	public List<DeviceConfiguration> getConfigurations() {
		List<DeviceConfiguration> returnVal = new ArrayList<>();
		//returnVal.add(new DeviceConfiguration(12L, "daghan.deli"));
		for (Entry<Long, String[]> tempEntry : deviceTracker.getDeviceSummary().entrySet()) {
			for (String factoryPid : tempEntry.getValue()) {
				returnVal.add(new DeviceConfiguration(tempEntry.getKey(), factoryPid));
			}
		}
		return returnVal;
	}
}
