package com.daghan.device.dummylorasensor.provider;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;

import com.daghan.device.dummylorasensor.configuration.DummySensorCongifuration;
import com.daghan.device.dummylorasensor.provider.dto.SensorDataDTO;
import com.daghan.iot.core.api.Device;

import osgi.enroute.dto.api.DTOs;
import osgi.enroute.mqtt.api.IMqttClient;

@Component(name = "com.daghan.device.dummylorasensor")
@Designate(ocd = DummySensorCongifuration.class, factory = true)
public class DummylorasensorImpl implements Device {
	public final String SUBSCRIPTION_CHANNEL_NAME = "DummySensor";

	DummySensorCongifuration deviceConfiguration;

	@Reference
	IMqttClient mqttClient;

	@Reference
	DTOs dtoConverter;

	@Activate
	public void activate(DummySensorCongifuration conf) {
		this.deviceConfiguration = conf;
		mqttClient.subscribe(deviceConfiguration.subscriptionChannel(), (str) -> {
			SensorDataDTO data = new SensorDataDTO();
			try {
				data = dtoConverter.decoder(SensorDataDTO.class).get(str);
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println(deviceConfiguration.name() + data.dev_eui);
		});
	}

	// @Modified
	// public void modified(DummySensorCongifuration conf) {
	// System.out.println("sensor modified");
	// }

	@Override
	public String getId() {
		return deviceConfiguration.name();
	}

}
