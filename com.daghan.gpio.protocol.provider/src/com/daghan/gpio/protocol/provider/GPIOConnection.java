package com.daghan.gpio.protocol.provider;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.invoke.MethodType;
import java.net.URL;
import java.net.URLConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.daghan.iot.core.api.MethodTypeEnum;
import com.daghan.iot.utils.StringUtils;

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
	protected OutputStream outputStream;

	protected GPIOConnection(URL url, MethodTypeEnum methodType) {
		super(url);
		this.methodType = methodType;
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
				break;
			case POST:
				super.doInput = true;
				super.doOutput = true;
				break;
			case DELETE:
				super.doInput = false;
				super.doOutput = true;
				break;
			default:
				logger.error("Should never end up here. This is a request type " + methodType);
				break;
			}
			inputStream = StringUtils
					.convertStringToInputStream("Welcome to GPIO protocol u are using " + methodType.name());
			connected = true;
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
		OutputStream returnVal = null;
		connect();
		if (super.doOutput) {
			returnVal = outputStream;
		}
		return returnVal;
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
