package com.daghan.iot.core.api;

/**
 * Resource that can be accessed by the framework. The classes that implement
 * this class should have a accompanying {@link DELETEDeviceConfig}
 * configuration.
 * 
 * @author daghan
 *
 */
public interface Device {
	/**
	 * Returns the ID of this device. Default implementation returns the value
	 * of the toStringMethod.
	 * 
	 * @return
	 */
	public String getId();
}
