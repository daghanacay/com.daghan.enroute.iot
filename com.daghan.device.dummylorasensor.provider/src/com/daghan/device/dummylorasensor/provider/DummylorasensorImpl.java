package com.daghan.device.dummylorasensor.provider;

import java.util.Base64;
import java.util.Random;
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
	private static final String NOT_AVAILABLE = "N/A";
	private AtomicReference<SensorDataDTO> lastKnownData = new AtomicReference<SensorDataDTO>(new SensorDataDTO());
	@Reference
	private DTOs dtoConverter;
	private LoraSensorConfiguration deviceConfiguration;
	private SensorDataDTO newData = new SensorDataDTO();
	// lambda that is subscribed to MQTT
	private MessageListener subsMethod = (str) -> {
		try {
			SensorDataDTO newData = dtoConverter.decoder(SensorDataDTO.class).get(str);
			// Decode payload
			newData.payload = new String(Base64.getDecoder().decode(newData.payload));
			lastKnownData.set(newData);
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
		createRandomData();
		return lastKnownData.get();
	}

	// Creates a random data if device configuration defines either of
	// subscription or publish channel as "N/A"
	private void createRandomData() {
		if (NOT_AVAILABLE.equals(deviceConfiguration.subscriptionChannel())
				|| NOT_AVAILABLE.equals(deviceConfiguration.publishChannel())) {
			newData.payload = String.valueOf(120 * Math.random() - 20);
			newData.metadata.get(0).latitude += (.01 * (Math.random() - 0.5));
			newData.metadata.get(0).longitude += (.01 * (Math.random() - 0.5));
			lastKnownData.set(newData);
		}
	}

	@Override
	public String getId() {
		return deviceConfiguration.name();
	}

}
