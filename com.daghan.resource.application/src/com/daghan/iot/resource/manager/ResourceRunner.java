package com.daghan.iot.resource.manager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.util.tracker.ServiceTracker;

import osgi.enroute.debug.api.Debug;

@Component(service = ResourceRunner.class, immediate = true, property = { Debug.COMMAND_SCOPE + "=res",
		Debug.COMMAND_FUNCTION + "=run" })
public class ResourceRunner {
	ResourceManagerImpl resourceTracker;

	@Activate
	void activate(BundleContext context) throws InvalidSyntaxException {
		resourceTracker = new ResourceManagerImpl(context);
		resourceTracker.open();
	}

	@Deactivate
	void deactivate() {
		resourceTracker.close();
	}

	public void run(String str) {
		System.out.println(str);
	}

	public <Output, Input> Output activateResource(String url, Input input) throws IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException{
		return resourceTracker.activateResource(url, input);
	}

	private class ResourceManagerImpl extends ServiceTracker {
		@SuppressWarnings("unchecked")
		public ResourceManagerImpl(BundleContext context) throws InvalidSyntaxException {
			// track all services
			super(context, FrameworkUtil.createFilter("(" + Constants.OBJECTCLASS + "=*)"), null);
		}

		public <Output, Input> Output activateResource(String url, Input input) throws IllegalAccessException,
				IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
			for (Object service : getServices()) {
				for (Method method : service.getClass().getMethods()) {
					ResourceProvider providerAnnotation = method.getAnnotation(ResourceProvider.class);
					if (providerAnnotation != null) {
						String urlVal = (String) service.getClass().getMethod(providerAnnotation.url()).invoke(service);
						if (urlVal.equalsIgnoreCase(url)) {
							System.out.println(url);
							// we found the method lets run it
							return (Output) method.invoke(service, input);
						}
					}
				}
			}
			// no annotated method is found
			return null;
		}
	}

}
