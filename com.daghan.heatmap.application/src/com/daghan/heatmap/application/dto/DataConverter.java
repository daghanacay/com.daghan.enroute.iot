package com.daghan.heatmap.application.dto;

import com.daghan.device.dummylorasensor.provider.dto.SensorDataDTO;

public class DataConverter {
	// Converts the data coming from sensor to application DTO that will be sent
	// to front end. Does not clone the values so input value should be
	// immutable TODO make this a service and inject DTO for deep cloning
	public AppSensorDataDTO convert(SensorDataDTO sensorDto) {
		AppSensorDataDTO returnval = new AppSensorDataDTO();
		returnval.sensorValue = Double.valueOf(sensorDto.payload);
		returnval.longitude = sensorDto.metadata.get(0).longitude;
		returnval.latitude = sensorDto.metadata.get(0).latitude;
		return returnval;
	}
}
