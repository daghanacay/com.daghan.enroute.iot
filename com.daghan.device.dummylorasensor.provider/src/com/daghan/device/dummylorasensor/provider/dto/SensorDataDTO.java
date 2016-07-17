package com.daghan.device.dummylorasensor.provider.dto;

import java.util.List;

public class SensorDataDTO {

	public String payload;
	public int port;
	public int counter;
	public String dev_eui;
	public List<MetaDataDTO> metadata;
}
