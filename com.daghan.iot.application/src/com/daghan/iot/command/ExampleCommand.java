package com.daghan.iot.command;

import java.io.IOException;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

import osgi.enroute.debug.api.Debug;
import osgi.enroute.iot.gpio.api.CircuitBoard;
import osgi.enroute.iot.gpio.api.IC;
import osgi.enroute.iot.gpio.util.Digital;
import osgi.enroute.iot.gpio.util.ICAdapter;



@Component(
		service = IC.class, 
		
		property = {
			Debug.COMMAND_SCOPE+"=domo", 
			Debug.COMMAND_FUNCTION+"=led"
		}
	)
public class ExampleCommand extends ICAdapter<Void, Digital> {

	public void led(boolean on) throws Exception {
		out().set(on);
		System.out.println("LED is set to " + on);
	}

	@Reference
	protected void setCircuitBoard(CircuitBoard cb) {
		super.setCircuitBoard(cb);
	}
	
	@Activate
	void activate() throws IOException, InterruptedException {
		System.out.println("Hello World");
	}

	@Deactivate
	void deactivate() {
		System.out.println("Goodbye World");
	}
}
