package com.daghan.heatmap.application;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.daghan.device.dummylorasensor.provider.dto.SensorDataDTO;
import com.daghan.heatmap.application.dto.AppSensorDataDTO;
import com.daghan.heatmap.application.dto.DataConverter;
import com.daghan.iot.core.api.Device;
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
	// List of lora sensors see configuration/configuration.json
	@Reference(target="(service.factoryPid=com.daghan.device.lorasensor)")
	List<Device> loraSensors;
	
	private DataConverter converter = new DataConverter();
	
	public List<AppSensorDataDTO> getSensorData() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException{
		List<AppSensorDataDTO> returnVal = new ArrayList<>();
		SensorDataDTO sensorData;
		for (Device loraSensor:loraSensors){
			sensorData = rm.activateResource(loraSensor.getId(), null, SensorDataDTO.class, MethodTypeEnum.GET);
			returnVal.add(converter.convert(loraSensor.getId(), sensorData));
		}
		return returnVal;
	}
	

}
