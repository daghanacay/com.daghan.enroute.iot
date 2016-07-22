package com.daghan.device.dummylorasensor.provider;

import java.util.concurrent.atomic.AtomicReference;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;

import com.daghan.device.dummylorasensor.configuration.LoraSensorConfiguration;
import com.daghan.device.dummylorasensor.provider.dto.SensorDataDTO;
import com.daghan.iot.core.api.Device;
import com.daghan.iot.core.api.MethodTypeEnum.GetMethod;

import osgi.enroute.dto.api.DTOs;
import osgi.enroute.mqtt.api.IMqttClient;
import osgi.enroute.mqtt.api.MessageListener;

@Component(name = "com.daghan.device.lorasensor", configurationPolicy = ConfigurationPolicy.REQUIRE)
@Designate(ocd = LoraSensorConfiguration.class, factory = true)
public class DummylorasensorImpl implements Device {
	private AtomicReference<SensorDataDTO> lastKnownData;
	@Reference
	private DTOs dtoConverter;
	private LoraSensorConfiguration deviceConfiguration;

	private MessageListener subsMethod = (str) -> {
		try {
			lastKnownData.set(dtoConverter.decoder(SensorDataDTO.class).get(str));
		} catch (Exception e) {
			e.printStackTrace();
		}
	};
	
	@Reference
	IMqttClient mqttClient;

	@Activate
	public void activate(LoraSensorConfiguration conf) {
		this.deviceConfiguration = conf;
		mqttClient.subscribe(deviceConfiguration.subscriptionChannel(), subsMethod);
	}

	@Modified
	public void modified(LoraSensorConfiguration conf) {
		mqttClient.unsubscribe(deviceConfiguration.subscriptionChannel());
		this.deviceConfiguration = conf;
		mqttClient.subscribe(deviceConfiguration.subscriptionChannel(), subsMethod);
	}

	@GetMethod
	public SensorDataDTO getData() {
		return lastKnownData.get();
	}

	@Override
	public String getId() {
		return deviceConfiguration.name();
	}

}
