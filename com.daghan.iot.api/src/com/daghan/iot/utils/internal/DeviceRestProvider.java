package com.daghan.iot.utils.internal;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.http.whiteboard.HttpWhiteboardConstants;

import com.daghan.iot.core.api.MethodTypeEnum;
import com.daghan.iot.resource.manager.ResourceRunner;

import aQute.bnd.annotation.headers.ProvideCapability;
import osgi.enroute.dto.api.DTOs;

/**
 * Maps all the device methods to rest api
 * 
 * @author daghan
 *
 */
@Component(
		//
		service = Servlet.class, //
		name = "daghan.device.rest.simple", //
		property = HttpWhiteboardConstants.HTTP_WHITEBOARD_SERVLET_PATTERN + "=/devices/*")
public class DeviceRestProvider extends HttpServlet {
	@Reference
	private ResourceRunner rm;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Object returnVal = null;
		String[] pathInfo = req.getPathInfo().split("/");
		if (pathInfo.length > 2) {
			resp.getWriter().println("url does not support level after " + pathInfo[1]);
		}
		try {
			returnVal = rm.activateResource(pathInfo[1], null, Object.class, MethodTypeEnum.GET);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException e) {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			resp.getWriter().println("could not GET method on the device " + pathInfo[1]);
			return;
		}
		if (returnVal == null) {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			resp.getWriter().println("could not GET method on the device " + pathInfo[1]);
		} else {
			resp.setStatus(HttpServletResponse.SC_OK);
			resp.getWriter().println(returnVal.toString());
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String returnVal = null;
		String[] pathInfo = req.getPathInfo().split("/");
		if (pathInfo.length > 2) {
			resp.getWriter().println("url does not support level after " + pathInfo[1]);
		}
		try {
			returnVal = rm.activateResource(pathInfo[1], req.getReader().readLine(), String.class, MethodTypeEnum.POST);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException e) {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			resp.getWriter().println("could not POST method on the device " + pathInfo[1]);
			return;
		}
		if (returnVal == null) {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			resp.getWriter().println("could not POST method on the device " + pathInfo[1]);
		} else {
			resp.getWriter().println(returnVal);
		}
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		super.doDelete(req, resp);
	}
}
