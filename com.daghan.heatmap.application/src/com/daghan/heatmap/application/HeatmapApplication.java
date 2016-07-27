package com.daghan.heatmap.application;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.daghan.device.dummylorasensor.provider.dto.SensorDataDTO;
import com.daghan.heatmap.application.dto.AppSensorDataDTO;
import com.daghan.heatmap.application.dto.DataConverter;
import com.daghan.iot.core.api.MethodTypeEnum;
import com.daghan.iot.resource.manager.ResourceRunner;

import osgi.enroute.configurer.api.RequireConfigurerExtender;
import osgi.enroute.google.angular.capabilities.RequireAngularWebResource;
import osgi.enroute.rest.api.REST;
import osgi.enroute.twitter.bootstrap.capabilities.RequireBootstrapWebResource;
import osgi.enroute.webserver.capabilities.RequireWebServerExtender;

@RequireAngularWebResource(resource = { "angular.js", "angular-resource.js", "angular-route.js" }, priority = 1000)
@RequireBootstrapWebResource(resource = "css/bootstrap.css")
@RequireWebServerExtender
@RequireConfigurerExtender
@Component(name = "com.daghan.heatmap")
public class HeatmapApplication implements REST {
	@Reference
	private ResourceRunner rm; 
	// List of sensor names see configuration/configuration.json
	private String[] sensorNames = {"sensor1","sensor2"};
	private DataConverter converter = new DataConverter();
	
	public List<AppSensorDataDTO> getSensorData() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException{
		List<AppSensorDataDTO> returnVal = new ArrayList<>();
		for (String sensorName:sensorNames){
			SensorDataDTO sensorData = rm.activateResource(sensorName, null, SensorDataDTO.class, MethodTypeEnum.GET);
			returnVal.add(converter.convert(sensorData));
		}
		return returnVal;
	}
	

}
