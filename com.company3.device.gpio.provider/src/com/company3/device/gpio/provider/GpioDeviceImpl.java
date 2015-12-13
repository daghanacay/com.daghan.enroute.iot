package com.company3.device.gpio.provider;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

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
import com.daghan.iot.core.api.MethodTypeEnum.GetMethod;
import com.daghan.iot.core.api.MethodTypeEnum.PostMethod;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPin;
import com.pi4j.io.gpio.GpioPinDigital;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.RaspiPin;

/**
 * 
 */
@ObjectClassDefinition(name = "Digital Pin Configuration")
@interface DigitalPinConfiguration {
	@AttributeDefinition(name = "Device name", description = "Specifies this to be used by other devices.")
	String name() default "Pin8Output";

	@AttributeDefinition(name = "Pin number that can be used for digital input/output", description = "Select the pin to be configured.")
	PinNumberEnum pinNumber() default PinNumberEnum.pin8;

	@AttributeDefinition(name = "Pin type", description = "Defines pin as input or output pin.")
	InputOutputEnum type() default InputOutputEnum.output;

	@AttributeDefinition(name = "Pin Level", description = "Defines output default output for output pins and pull resistence for input pins.")
	PinLevelEnum pinLevel() default PinLevelEnum.high;
}

@Component
@Designate(ocd = DigitalPinConfiguration.class, factory = true)
public class GpioDeviceImpl implements Device {
	private DigitalPinConfiguration config;

	public enum PinNumberEnum {
		pin1(1), pin2(2), pin3(3), pin4(4), pin5(5), pin6(6), pin7(7), pin8(8), pin9(9), pin10(10), pin11(11), pin12(
				12), pin13(13), pin14(14), pin15(15), pin16(16), pin17(17), pin18(18), pin19(19), pin20(20), pin21(
						21), pin22(22), pin23(23), pin24(24), pin25(25), pin26(26), pin27(27), pin28(28), pin29(29);
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

	// Override to make it more user friendly and make the configuration easy
	@Override
	public String getId() {
		return config.name();
	}

	@GetMethod
	public String getPinValue() throws IOException {
		URL gpioUrl2 = new URL("gpio:GET//admin:admin@pin/" + config.pinNumber().getVal());
		URLConnection connection1a = gpioUrl2.openConnection();
		return (String) connection1a.getContent();
	}

	@PostMethod
	public String setPinValue(String value) throws IOException {
		URL gpioUrl = new URL("gpio:POST//admin:admin@pin/" + config.pinNumber().getVal());
		URLConnection connection1 = gpioUrl.openConnection();
		connection1.getOutputStream().write(value.getBytes());
		return (String) connection1.getContent();
	}

	private Pin findPin(int pinNumber) {
		return RaspiPin.getPinByName("GPIO " + pinNumber);
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
