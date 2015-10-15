package com.daghan.iot.resource.test;

import org.osgi.service.component.annotations.Component;

import com.daghan.iot.resource.manager.ResourceProvider;

@Component(service = PrinterResource.class)
public class PrinterResource {
		
	@ResourceProvider(url = "/output/sysout/1", resourceInputInterface = String.class, resourceOutputInterface = Void.class)
	public void printData(String output) {
		System.out.println(output);
	}
}
