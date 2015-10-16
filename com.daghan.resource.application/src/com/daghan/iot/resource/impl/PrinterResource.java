package com.daghan.iot.resource.impl;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.metatype.annotations.Designate;

import com.daghan.iot.resource.manager.ResourceConfig;
import com.daghan.iot.resource.manager.ResourceProvider;

@Component(service = PrinterResource.class)
@Designate(ocd = ResourceConfig.class, factory = true)
public class PrinterResource {
	private String url;

	public String getUrl() {
		return url;
	}
	@Activate
	public void activate(ResourceConfig config) {
		url = config.url();
	}

	@ResourceProvider(url = "getUrl")
	public void printData(String output) {
		System.out.println(output);
	}
}
