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

import com.daghan.iot.core.api.Device;
import com.daghan.iot.core.api.MethodTypeEnum;

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

	public <O, I> O activateResource(String requestServiceId, I input, Class<O> outputType, MethodTypeEnum methodType)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
		return resourceTracker.activateResource(requestServiceId, input, outputType, methodType);
	}

	private class ResourceManagerImpl extends ServiceTracker<Device, Device> {
		public ResourceManagerImpl(BundleContext context) throws InvalidSyntaxException {
			// track all services
			super(context, FrameworkUtil.createFilter("(" + Constants.OBJECTCLASS + "=com.daghan.iot.core.api.Device)"),
					null);
		}

		public <Output, Input> Output activateResource(String requestServiceId, Input input, Class<?> outputType,
				MethodTypeEnum methodType) throws IllegalAccessException, IllegalArgumentException,
						InvocationTargetException, NoSuchMethodException {
			for (Object service : getServices()) {
				Device deviceService = (Device) service;
				// Check if this is the service we are interested
				if (requestServiceId.equalsIgnoreCase(deviceService.getId())) {
					for (Method method : deviceService.getClass().getDeclaredMethods()) {
						Object providerAnnotation;
						providerAnnotation = method.getAnnotation(methodType.getAnnotation());
						// We only allow zero or one parameter methods
						if (providerAnnotation != null && method.getParameters().length <= 1
								&& method.getReturnType().isAssignableFrom(outputType)) {
							// Allow calling even private, protected, or no
							// modifier
							method.setAccessible(true);

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
