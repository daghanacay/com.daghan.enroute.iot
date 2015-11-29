package com.company2.device.provider;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

//Should be added to identify this as configuration for OSGi
@ObjectClassDefinition(name = "Reader Device Config Company B")
public @interface ReaderConfigCompanyB {
	// Should be added to identify this as configuration attribute for OSGi
		@AttributeDefinition(name = "Device Name", description = "Provides the name for this device")
		public String getName() default "myCompany";
		@AttributeDefinition(name = "Company name", description = "Returns company name")
		public String getCompanyName() default "myCompany:";
}
