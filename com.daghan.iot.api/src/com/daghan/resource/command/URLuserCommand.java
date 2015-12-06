package com.daghan.resource.command;

import java.net.URL;
import java.net.URLConnection;
import java.util.Random;

import org.osgi.service.component.annotations.Component;

import osgi.enroute.debug.api.Debug;

@Component(service = URLuserCommand.class, immediate = true, property = { Debug.COMMAND_SCOPE + "=pro",
		Debug.COMMAND_FUNCTION + "=dagUrl" })
public class URLuserCommand {

	public void dagUrl(String str) throws Exception {
		URL gpioUrl = new URL("gpio:POST//admin:admin@pin/1");
		URLConnection connection1 = gpioUrl.openConnection();
		String content = (String) connection1.getContent();
		System.out.println(content);
//		URL gpioUrl2 = new URL("gpio:GET//admin:admin@pin/1?period=1000");
//		URLConnection connection1a = gpioUrl2.openConnection();
//		
//		URL dagUrl = new URL("daghan:GET//deli:kiro@simple.txt?close=true");
//		URLConnection connection2 = dagUrl.openConnection();

	}

}
