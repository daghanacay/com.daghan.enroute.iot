package com.company3.device.gpio.provider;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

import com.company3.device.gpio.provider.GpioDeviceImpl.InputOutputEnum;
import com.company3.device.gpio.provider.GpioDeviceImpl.PinLevelEnum;
import com.company3.device.gpio.provider.GpioDeviceImpl.PinNumberEnum;
import com.daghan.iot.core.api.Device;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPin;
import com.pi4j.io.gpio.GpioPinDigital;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinPullResistance;

/**
 * 
 */
@ObjectClassDefinition(name = "Digital Pin Configuration")
@interface DigitalPinConfiguration {
	@AttributeDefinition(name = "Device name", description = "Specifies this to be used by other devices.")
	String name() default "Pin8Input";

	@AttributeDefinition(name = "Pin number that can be used for digital input/output", description = "Select the pin to be configured.")
	PinNumberEnum pinNumber() default PinNumberEnum.pin8;

	@AttributeDefinition(name = "Pin type", description = "Defines pin as input or output pin.")
	InputOutputEnum type() default InputOutputEnum.output;

	@AttributeDefinition(name = "Pin Level", description = "Defines output default output for output pins and pull resistence for input pins.")
	PinLevelEnum pinLevel() default PinLevelEnum.high;
}

@Component(name = "com.company3.device.gpio")
@Designate(ocd = DigitalPinConfiguration.class, factory = true)
public class GpioDeviceImpl implements Device {
	private DigitalPinConfiguration config;

	public enum PinNumberEnum {
		pin8(8), pin9(9);
		int val;

		PinNumberEnum(int val) {
			this.val = val;
		}

		public int getVal() {
			return val;
		}
	}

	public enum InputOutputEnum {
		input, output;
	}

	public enum PinLevelEnum {
		low, high;
	}

	// Injection can only be done for @Component's
	private GpioController gpioContoller;

	@Reference(target = "(|(board.type=Model2B_Rev1)(board.type=ModelB_Plus_Rev1))")
	void setGpioController(GpioController controller) {
		this.gpioContoller = controller;
	}

	@Activate
	public void activate(DigitalPinConfiguration config) {
		this.config = config;
		configurePin();
	}

	private GpioPinDigital findPin(int pinNumber) {
		for (GpioPin tmp : gpioContoller.getProvisionedPins()) {
			if (tmp.getPin().getAddress() == pinNumber) {
				// This pin should be configured as digital pin but it is worth
				// making a check
				if (tmp instanceof GpioPinDigital) {
					return (GpioPinDigital) tmp;
				}
			}
		}
		return null;
	}

	private void unprovision(Pin pin) {
		for (GpioPin e : gpioContoller.getProvisionedPins()) {
			if (e.getPin().equals(pin)) {
				gpioContoller.unprovisionPin(e);
				break;
			}
		}
	}

	private void configurePin() {
		Pin pin = (Pin) findPin(config.pinNumber().getVal());

		unprovision(pin);
		switch (config.type()) { // Get request sets the input pin
		case input:
			GpioPinDigitalInput digitalIn = this.gpioContoller.provisionDigitalInputPin(pin);
			switch (config.pinLevel()) {
			case high:
				digitalIn.setPullResistance(PinPullResistance.PULL_UP);
				break;

			case low:
				digitalIn.setPullResistance(PinPullResistance.PULL_DOWN);
				break;
			}
			break;
		// Post request sets the output pin
		case output:
			GpioPinDigitalOutput digitalOut = this.gpioContoller.provisionDigitalOutputPin(pin);

			switch (config.pinLevel()) {
			case high:
				digitalOut.setState(true);
				break;
			case low:
				digitalOut.setState(false);
				break;
			}
			break;
		}
	}

}
