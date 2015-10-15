package com.daghan.iot.resource.manager;

import java.io.Serializable;
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
		System.out.println("Starting resource tracker." + context);
		resourceTracker = new ResourceManagerImpl(context);
		resourceTracker.open();
	}

	@Deactivate
	void deactivate() {
		System.out.println("Stopping resource tracker.");
		resourceTracker.close();
	}

	public void run(String str) {
		System.out.println(str);
	}

	public <Output, Input> Output activateResource(String url, Input input, Class<?> inputClass)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		return resourceTracker.activateResource(url, input, inputClass);
	}

	private class ResourceManagerImpl extends ServiceTracker {
		@SuppressWarnings("unchecked")
		public ResourceManagerImpl(BundleContext context) throws InvalidSyntaxException {
			// track all services
			super(context, FrameworkUtil.createFilter("(" + Constants.OBJECTCLASS + "=*)"), null);
		}

		public <Output, Input> Output activateResource(String url, Input input, Class<?> inputClass)
				throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
			for (Object service : getServices()) {
				for (Method method : service.getClass().getMethods()) {
					ResourceProvider providerAnnotation = method.getAnnotation(ResourceProvider.class);
					if (providerAnnotation != null && providerAnnotation.url().equalsIgnoreCase(url)) {
						System.out.println(providerAnnotation.url());
						// check if input and output interfaces passed to us can
						// work with the method we found
						if (method.getParameterTypes()[0].isInstance(input)) {
							// we found the method lets run it
							return (Output) method.invoke(service, input);
						} else {
							System.out.println("Input and output interface doea not fit the annotated method");
						}
					}
				}

			}
			// no annotated method is found
			return null;
		}
	}

}
