package com.company2.device.provider;

// Todo Create the pin provider

public class GPIODigitalPinDevice {
	// Output pin level
		public enum Level {
			off, low, high
		};	
//	private void unprovision(Pin pin) {
//		for (GpioPin e : gpio.getProvisionedPins()) {
//			if (e.getPin().equals(pin)) {
//				gpio.unprovisionPin(e);
//				break;
//			}
//		}
//	}
	
//	private void gpio(Pin pin, Level level) {
//		unprovision(pin);
//		
//		// Get request sets the input pin
//		case INPUT:
//			GpioPinDigitalInput digitalIn = this.gpio.provisionDigitalInputPin(pin);
//			switch (level) {
//			case high:
//				digitalIn.setPullResistance(PinPullResistance.PULL_UP);
//				break;
//
//			case low:
//				digitalIn.setPullResistance(PinPullResistance.PULL_DOWN);
//				break;
//
//			default:
//			case off:
//				digitalIn.setPullResistance(PinPullResistance.OFF);
//				break;
//			}
//			break;
//		// Post request sets the output pin
//		case OUTPUT:
//			unprovision(pin);
//			GpioPinDigitalOutput digitalOut = this.gpio.provisionDigitalOutputPin(pin);
//
//			switch (level) {
//			case high:
//				digitalOut.setState(true);
//				break;
//
//			default:
//			case off:
//			case low:
//				digitalOut.setState(false);
//				break;
//			}
//			break;
//		}
//
//	}
}
