package com.daghan.resource.command;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

import com.daghan.iot.core.api.MethodTypeEnum;
import com.daghan.iot.resource.manager.ResourceRunner;

import osgi.enroute.debug.api.Debug;

@ObjectClassDefinition(name = "GPIO Command Config")
@interface GPIOCommandConfig {
	@AttributeDefinition(name = "Pins to read value from", description = "sets the get method pins")
	String[] getResourceNamesGet() default { "" };

	@AttributeDefinition(name = "Pins to write value to", description = "sets the post method pins")
	String[] getResourceNamesPost() default { "" };
}

@Component(service = URLuserCommand.class, immediate = true, property = { Debug.COMMAND_SCOPE + "=pro",
		Debug.COMMAND_FUNCTION + "=dagUrl" })
@Designate(ocd = GPIOCommandConfig.class)
public class URLuserCommand {
	private GPIOCommandConfig config;
	@Reference
	private ResourceRunner rm;

	@Activate
	public void activate(GPIOCommandConfig config) {
		this.config = config;
	}

	public void dagUrl(String str) throws Exception {

		// OUT GPIO 8 (or I2C SDA1) (.03) Low
		for (String postPin : config.getResourceNamesPost()) {
			String output = rm.activateResource(postPin, str, String.class, MethodTypeEnum.POST);
			System.out.println(output);
		}

		// IN GPIO 9 (or I2C SCL1) (.05)
		for (String getPin : config.getResourceNamesPost()) {
			String output = rm.activateResource(getPin, str, String.class, MethodTypeEnum.GET);
			System.out.println(output);
		}

		//
		// URL dagUrl = new URL("daghan:GET//deli:kiro@simple.txt?close=true");
		// URLConnection connection2 = dagUrl.openConnection();

	}

}
