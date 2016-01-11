package com.daghan.iot.utils.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.metatype.MetaTypeInformation;
import org.osgi.service.metatype.MetaTypeService;
import org.osgi.util.tracker.BundleTracker;

import com.daghan.iot.core.api.Device;
import com.daghan.iot.core.api.IoTConstants;

/**
 * Provides a service that tracks the bundles which has Bundle-Category heading
 * includes "IoT"
 * 
 * @author daghan
 *
 */
@Component(service = DeviceTracker.class)
public class DeviceTracker {

	@Reference
	private MetaTypeService metatypeService;
	//
	private BundleTrackerImpl deviceTracker;
	final private String CATEGORY_HEADER = "Bundle-Category";
	final private String IOT_CATEGORY_NAME = "IoT";
	private volatile Map<Long, List<PidObject>> bundleMap = new ConcurrentHashMap<>();

	@Activate
	public void start(BundleContext bundleContext) {
		deviceTracker = new BundleTrackerImpl(bundleContext);
		deviceTracker.open();

	};

	@Deactivate
	public void stop() {
		deviceTracker.close();
	}

	public List<PidObject> getDeviceSummary() {
		List<PidObject> returnVal = new ArrayList<>();
		for (List<PidObject> temp : bundleMap.values()) {
			returnVal.addAll(temp);
		}
		return returnVal;
	}

	/**
	 * NOTE:
	 * 
	 * cardinality = ReferenceCardinality.MULTIPLE :Because we want multiple
	 * reference but if there is none than the tracker buyndle should not stop,
	 * e.g. not mandatory
	 * 
	 * policy = ReferencePolicy.DYNAMIC : Because we want to be notified for
	 * each service entered to the system
	 * 
	 * @param properties
	 */
	@Reference(service = Device.class, unbind = "unbindDeviceService", cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
	public void bindDeviceService(Map properties) {
		String servicePID = (String) properties.get(Constants.SERVICE_PID);
		getFactoryPid(properties).addChildPid(servicePID);
	}

	public void unbindDeviceService(Map properties) {
		String servicePID = (String) properties.get(Constants.SERVICE_PID);
		getFactoryPid(properties).removeChildPid(servicePID);

	}

	/**
	 * Gets the factory pid corresponding to the service based on the service
	 * properties
	 * 
	 * @param properties
	 * @return
	 */
	private PidObject getFactoryPid(Map properties) {
		
		String factoryPID = (String) properties.get(IoTConstants.SERVICE_FACTORY_PID);
		Long bundleId = (Long) properties.get(Constants.SERVICE_BUNDLEID);
		return bundleMap.get(bundleId).stream().filter(a -> a.getPidStr().equals(factoryPID)).findFirst().get();
	}

	// Internal tracker implementation
	private class BundleTrackerImpl extends BundleTracker<Long> {
		public BundleTrackerImpl(BundleContext context) {
			super(context, Bundle.ACTIVE, null);
		}

		@Override
		public Long addingBundle(Bundle bundle, BundleEvent event) {
			Long returnVal = null;
			String categoryNames = bundle.getHeaders().get(CATEGORY_HEADER);
			if ((categoryNames != null) && categoryNames.length() > 0
					&& Arrays.binarySearch(categoryNames.split(","), IOT_CATEGORY_NAME) >= 0) {
				MetaTypeInformation info = metatypeService.getMetaTypeInformation(bundle);
				returnVal = bundle.getBundleId();
				List<PidObject> objList = Arrays.asList(info.getFactoryPids()).stream()
						.map(fatPid -> new PidObject(fatPid)).collect(Collectors.toList());

				bundleMap.put(returnVal, objList);
			}
			// retunVal == null notifies to the framework that we are not
			// tracking
			// this bundle
			return returnVal;
		}

		@Override
		public void removedBundle(Bundle bundle, BundleEvent event, Long object) {
			bundleMap.remove(object);
			super.removedBundle(bundle, event, object);
		}

	}
}
