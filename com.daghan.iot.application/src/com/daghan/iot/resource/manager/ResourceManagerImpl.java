package com.daghan.iot.resource.manager;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.swing.plaf.synth.SynthSeparatorUI;

import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.util.tracker.ServiceTracker;

@Component(service = ResourceManagerImpl.class, immediate = true)
public class ResourceManagerImpl extends ServiceTracker {
	ResourceManagerImpl resourceTracker;
	
	@Activate
	void activate(BundleContext context){
		System.out.println("Starting resource tracker.");
		resourceTracker = new ResourceManagerImpl(context);
		resourceTracker.open();
	}
	
	@Deactivate
	void deactivate(){
		System.out.println("Stopping resource tracker.");
		resourceTracker.close();
	}
	
	
	
	@SuppressWarnings("unchecked")
	public ResourceManagerImpl(BundleContext context) {
		// track all services
		super(context, "(*)", null);
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
