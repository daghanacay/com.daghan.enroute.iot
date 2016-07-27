package com.daghan.device.dummylorasensor.provider.dto;

import java.util.Arrays;
import java.util.List;

import org.osgi.dto.DTO;

public class SensorDataDTO extends DTO{
// With default values
	public String payload = "0.3";
	public int port;
	public int counter;
	public String dev_eui;
	public List<MetaDataDTO> metadata = Arrays.asList(new MetaDataDTO());
}
