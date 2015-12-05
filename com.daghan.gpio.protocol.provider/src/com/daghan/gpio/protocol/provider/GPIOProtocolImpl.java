package com.daghan.gpio.protocol.provider;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.WeakHashMap;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.url.AbstractURLStreamHandlerService;
import org.osgi.service.url.URLConstants;
import org.osgi.service.url.URLStreamHandlerService;
import org.osgi.service.url.URLStreamHandlerSetter;

import com.daghan.iot.core.api.MethodTypeEnum;

/**
 * 
 */
@Component(name = "com.daghan.gpio.protocol", property = { URLConstants.URL_HANDLER_PROTOCOL + "=daghan" })
public class GPIOProtocolImpl extends AbstractURLStreamHandlerService implements URLStreamHandlerService {
	// Stores the reference to all url to methods. it will remove keys as soon
	// as URL are ready for garbage collection
	private Map<URL, MethodTypeEnum> urlMethodMap = Collections.synchronizedMap(new WeakHashMap<URL, MethodTypeEnum>());
	
	@Override
	public URLConnection openConnection(URL u) throws IOException {
		// Uncomment if you wish to force cleaning the memory used by unused URLs
		// System.gc();
		System.out.println(String.format("connection opened using protocol %s, method type %s, user info %s, query %s.",
				u.getProtocol(), urlMethodMap.get(u).name(), u.getUserInfo(), u.getQuery()));

		return null;
	}

	@Override
	public void parseURL(URLStreamHandlerSetter realHandler, URL u, String spec, int start, int limit) {
		System.out.println(u.hashCode());
		int slash = spec.indexOf('/');
		MethodTypeEnum type = Enum.valueOf(MethodTypeEnum.class, spec.substring(start, slash));
		start = slash;
		super.parseURL(realHandler, u, spec, start, limit);
		// IMPORTANT URL SHOULD BE ADDED TO MAP HERE OTHERWISE MAP.get() is
		// broken due to changing hash value of the URL instance
		urlMethodMap.put(u, type);
	}

}
