package com.daghan.iot.resource.impl.provider1;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

/**
 * Specific configuration of this device
 * 
 * @author daghan
 *
 */
// Should be added to identify this as configuration for OSGi
@ObjectClassDefinition(name = "Reader Device Config company A")
public @interface ReaderDeviceConfig {
	// Should be added to identify this as configuration attribute for OSGi
	@AttributeDefinition(name = "Device Name", description = "Provides the name for this device")
	public String getName() default "myReader";

	@AttributeDefinition(name = "String to prepend", description = "All the read values from this resource will be prepend with this value")
	public String getPrependString() default "myReader:";
}
