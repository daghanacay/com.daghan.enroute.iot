package com.daghan.protocol.gpio.provider.command;

import java.lang.reflect.InvocationTargetException;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.daghan.iot.core.api.MethodTypeEnum;
import com.daghan.iot.core.api.MethodTypeEnum.PostMethod;
import com.daghan.iot.resource.manager.ResourceRunner;

import osgi.enroute.debug.api.Debug;

/**
 * Porvides some commands related to gpio
 * 
 * @author daghan
 *
 */

@Component(service = GpioCommand.class, immediate = true, property = { Debug.COMMAND_SCOPE + "=gpio",
		Debug.COMMAND_FUNCTION + "=turn" })
public class GpioCommand {
	@Reference
	private ResourceRunner rm;

	
	/**
	 * turns on and of the output pin. the pin should be configured to be output
	 * before this method can be used
	 * 
	 * @param state
	 *            "ON" or "OFF"
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 */
	public String turn(String pinName, String state) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
		String returnVal = String.format("Changing the state of pin %s to %s", pinName, state);
		switch (state) {
		case "ON":
			rm.activateResource(pinName, "HIGH", String.class, MethodTypeEnum.POST );
			break;
		case "OFF":
			rm.activateResource(pinName, "LOW", String.class, MethodTypeEnum.POST );
			break;
		default:
			returnVal = "State should be ON or OFF";
			break;
		}
		return returnVal;
	}
}
