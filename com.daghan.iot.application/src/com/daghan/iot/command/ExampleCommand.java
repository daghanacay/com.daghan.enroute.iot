package com.daghan.iot.command;

import java.io.IOException;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

import osgi.enroute.debug.api.Debug;
import osgi.enroute.iot.gpio.api.CircuitBoard;
import osgi.enroute.iot.gpio.api.IC;
import osgi.enroute.iot.gpio.util.Digital;
import osgi.enroute.iot.gpio.util.ICAdapter;

@ObjectClassDefinition(name = "Server Configuration")
@interface ExampleConfig {
	String name() default "ExampleCommand";

	@AttributeDefinition(name = "Interval Lenght", description = "Use it to change interval, not at all used anywhere.")
	int interval() default 1000;

	ExampleEnum enumChooser() default ExampleEnum.up;

}

enum ExampleEnum {
	up, down;
}

@Component(service = IC.class, property = {Debug.COMMAND_SCOPE + "=domo", Debug.COMMAND_FUNCTION + "=led" })
@Designate(ocd = ExampleConfig.class, factory = true)
public class ExampleCommand extends ICAdapter<Void, Digital> {
	ExampleConfig config;

	public void led(boolean on) throws Exception {
		out().set(on);
		System.out.println("LED is set to " + on + "on components " + config.name());
	}

	@Reference
	protected void setCircuitBoard(CircuitBoard cb) {
		super.setCircuitBoard(cb);
	}

	@Activate
	void activate(ExampleConfig config) throws IOException, InterruptedException {
		System.out.println("Hello World " + config.interval());
		this.config = config;
	}

	@Deactivate
	void deactivate() {
		System.out.println("Goodbye World");
	}
}
