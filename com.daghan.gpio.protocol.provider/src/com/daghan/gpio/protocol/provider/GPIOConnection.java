package com.daghan.gpio.protocol.provider;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;

import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.daghan.iot.core.api.MethodTypeEnum;
import com.daghan.iot.utils.StringUtils;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPin;
import com.pi4j.io.gpio.GpioPinDigital;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;

/**
 * Creates a connection to GPIO.
 * 
 * @author daghan
 *
 */
public class GPIOConnection extends URLConnection {
	// Logger
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	// method type of this connection
	protected MethodTypeEnum methodType;
	// Input stream for the connection
	protected InputStream inputStream;
	// Output stream for the connection
	protected OutputStream outputStream = new ByteArrayOutputStream();
	private GpioController gpioContoller;

	protected GPIOConnection(URL url, MethodTypeEnum methodType, GpioController gpioContoller) {
		super(url);
		this.methodType = methodType;
		this.gpioContoller = gpioContoller;
	}

	@Override
	synchronized public void connect() throws IOException {
		if (!connected) {
			logger.debug("connection opened using protocol {}, method type {}, user info {}, query {}.",
					super.url.getProtocol(), methodType.name(), super.url.getUserInfo(), super.url.getQuery());

			switch (methodType) {
			case GET:
				super.doInput = true;
				super.doOutput = false;
				doGet();
				break;
			case POST:
				super.doInput = true;
				super.doOutput = true;
				doPost();
				break;
			case DELETE:
				super.doInput = false;
				super.doOutput = true;
				break;
			default:
				logger.error("Should never end up here. This is a request type " + methodType);
				break;
			}
			connected = true;
		}
	}

	private void doPost() throws UnsupportedEncodingException {
		int pinNumber = getPinNumber();
		if (pinNumber == -1) {
			return;
		}
		// Read the command from the payload
		String command = outputStream.toString();
		GpioPinDigital pin = findPin(pinNumber);
		if ((pin == null) || !(pin instanceof GpioPinDigitalOutput)) {
			inputStream = StringUtils.convertStringToInputStream(
					"Pin is not configured properly. Please check with your device configuration.");
			return;
		}

		if (command.equalsIgnoreCase("HIGH")) {
			((GpioPinDigitalOutput) pin).setState(PinState.HIGH);
		} else {
			((GpioPinDigitalOutput) pin).setState(PinState.LOW);
		}
		inputStream = StringUtils.convertStringToInputStream(
				"Success");

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

	private int getPinNumber() {
		int pinNumber = -1;
		try {
			// We should get /1 for the file in URL
			pinNumber = Integer.parseInt(getURL().getFile().substring(1));
		} catch (NumberFormatException e) {
			inputStream = StringUtils.convertStringToInputStream(
					getURL().getFile() + " is not a number. Please chcek with your device provider implementation.");
		}
		return pinNumber;
	}

	/**
	 * does get request on a pin. It assumes the pin is configured as
	 * {@link GpioPinDigital}, if not then the error is written to output stream
	 */
	private void doGet() {
		int pinNumber = getPinNumber();
		if (pinNumber == -1) {
			return;
		}
		PinState pinValue;
		GpioPinDigital pin = findPin(pinNumber);
		if (pin == null) {
			inputStream = StringUtils.convertStringToInputStream(
					"Pin is not configured properly. Please check with your device configuration.");
			return;
		}
		pinValue = gpioContoller.getState(pin);
		// Write the result back to the requester
		if (pinValue.isHigh()) {
			inputStream = new ByteArrayInputStream(new byte[] { '1' });
		} else {
			inputStream = new ByteArrayInputStream(new byte[] { '0' });
		}

	}

	/**
	 * Checks if this connection supports the input or output based on the
	 * {@code methodType} Extending classes do not need to override this method
	 * but are responsible for initializing the {@code inputStream}.
	 */
	@Override
	public InputStream getInputStream() throws IOException {
		InputStream returnVal = null;
		connect();
		if (super.doInput) {
			returnVal = inputStream;
		}
		return returnVal;
	}

	/**
	 * Checks if this connection supports the input or output based on the
	 * {@code methodType} Extending classes do not need to override this method
	 * but are responsible for initializing the {@code outputStream}.
	 */
	@Override
	public OutputStream getOutputStream() throws IOException {
		return outputStream;
	}

	/**
	 * To escape the content type. current system only supports text/plain
	 */
	@Override
	public Object getContent() throws IOException {
		return StringUtils.convertStreamToString(getInputStream());
	}

	@Override
	public final void setDoInput(boolean doinput) {
		throw new UnsupportedOperationException(
				"Only implementations can change the input and output characteristics.");
	}

	@Override
	public final void setDoOutput(boolean doinput) {
		throw new UnsupportedOperationException(
				"Only implementations can change the input and output characteristics.");
	}

}
