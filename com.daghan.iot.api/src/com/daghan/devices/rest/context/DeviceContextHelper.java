package com.daghan.devices.rest.context;

import java.io.IOException;
import java.util.Base64;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.http.context.ServletContextHelper;
import org.osgi.service.http.whiteboard.HttpWhiteboardConstants;
import org.osgi.service.useradmin.User;
import org.osgi.service.useradmin.UserAdmin;

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
	@Reference
	private UserAdmin userAdmin;

	@Override
	public boolean handleSecurity(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// Check if we use https
		if (!request.getScheme().equals("https")) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
			return false;
		}
		// check if we have authentication header. if not ask for authentication
		// i.e. Basic authentication
		if (request.getHeader("Authorization") == null) {
			response.addHeader("WWW-Authenticate", "Basic");
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			return false;
		}
		// Check if authenticated
		if (authenticated(request, response)) {
			return true;
		} else {
			response.addHeader("WWW-Authenticate", "Basic");
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			return false;
		}

	}

	protected boolean authenticated(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String authzHeader = request.getHeader("Authorization");
		String usernameAndPassword = new String(Base64.getDecoder().decode(authzHeader.substring(6).getBytes()));

		int userNameIndex = usernameAndPassword.indexOf(":");
		String username = usernameAndPassword.substring(0, userNameIndex);
		String password = usernameAndPassword.substring(userNameIndex + 1);

		// Check if the password matches
		User user = (User) userAdmin.getRole(username);
		if (user == null) {
			response.addHeader("WWW-Authenticate", "Basic");
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			return false;
		}
		return ((String) user.getCredentials().get("password")).equals(password);

	}
}
