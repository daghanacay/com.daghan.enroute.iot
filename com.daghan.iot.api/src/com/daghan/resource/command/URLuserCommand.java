package com.daghan.resource.command;

import java.io.BufferedOutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;

import org.osgi.service.component.annotations.Component;

import osgi.enroute.debug.api.Debug;

@Component(service = URLuserCommand.class, immediate = true, property = { Debug.COMMAND_SCOPE + "=pro",
		Debug.COMMAND_FUNCTION + "=dagUrl" })
public class URLuserCommand {

	public void dagUrl(String str) throws Exception {
		
		//OUT GPIO 8 (or I2C SDA1) (.03) Low
		URL gpioUrl = new URL("gpio:POST//admin:admin@pin/8");
		URLConnection connection1 = gpioUrl.openConnection();
		connection1.getOutputStream().write("HIGH".getBytes());
		String content = (String) connection1.getContent();
		System.out.println(content);
		// IN GPIO 9 (or I2C SCL1) (.05)
		URL gpioUrl2 = new URL("gpio:GET//admin:admin@pin/9");
		URLConnection connection1a = gpioUrl2.openConnection();
		content = (String) connection1a.getContent();
		System.out.println(content);
//		
//		URL dagUrl = new URL("daghan:GET//deli:kiro@simple.txt?close=true");
//		URLConnection connection2 = dagUrl.openConnection();

	}

}
