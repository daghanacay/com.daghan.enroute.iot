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
		URL dagUrl = new URL("daghan:POST//deli:kiro@" + Math.random() + ".txt?close=hello");
		URL dagUrl2 = new URL("daghan:GET//deli:kiro@" + Math.random() + ".txt?close=hello");
		URLConnection connection1 = dagUrl.openConnection();
		URLConnection connection1a = dagUrl.openConnection();
		URLConnection connection2 = dagUrl2.openConnection();

	}

}
