package com.daghan.devices.rest.auth;

import java.io.IOException;
import java.net.URL;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ServiceScope;
import org.osgi.service.http.context.ServletContextHelper;
import org.osgi.service.http.whiteboard.HttpWhiteboardConstants;

/**
 * Handles the security for devices endpoint
 * 
 * @author daghan
 *
 */
@Component(service = ServletContextHelper.class, property = {
		HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_NAME + "=devicesContext",
		HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_PATH + "=/" })
public class DeviceContextHelper extends ServletContextHelper {
    @Override
    public boolean handleSecurity(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	return true;
    }
}
