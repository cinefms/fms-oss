package com.openfms.core.api.v2.controllers.admin;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.cinefms.apitester.annotations.ApiDescription;
import com.openfms.core.api.v2.AdminController;
import com.openfms.core.api.v2.utils.CorsHeaderUtil;
import com.openfms.core.service.AuthnService;
import com.openfms.model.core.auth.FmsAuthentication;
import com.openfms.model.core.auth.FmsSession;
import com.openfms.model.core.auth.FmsSessionHolder;
import com.openfms.model.core.auth.FmsUser;
import com.openfms.model.exceptions.AccessDeniedException;
import com.openfms.model.exceptions.DatabaseException;
import com.openfms.model.exceptions.NotAuthenticatedException;

@Controller
@RequestMapping(value="/admin/authentication")
public class AuthenticationController implements AdminController {
	
	private static Log log = LogFactory.getLog(AuthenticationController.class);
	
	@Autowired(required=true)
	private AuthnService authenticationService;
	
	@RequestMapping(value="/login",method=RequestMethod.OPTIONS)
	@ResponseBody
	public void loginOptions(HttpServletResponse response) throws AccessDeniedException, DatabaseException {
		List<HttpMethod> methods = new ArrayList<HttpMethod>();
		methods.add(HttpMethod.OPTIONS);
		methods.add(HttpMethod.PUT);
		methods.add(HttpMethod.GET);
		methods.add(HttpMethod.DELETE);
		CorsHeaderUtil.setCorsAllowedMethodHeaders(response, methods);
	}
	
	@RequestMapping(value="/reset",method=RequestMethod.OPTIONS)
	@ResponseBody
	public void resetOptions(HttpServletResponse response) throws AccessDeniedException, DatabaseException {
		List<HttpMethod> methods = new ArrayList<HttpMethod>();
		methods.add(HttpMethod.OPTIONS);
		methods.add(HttpMethod.POST);
		CorsHeaderUtil.setCorsAllowedMethodHeaders(response, methods);
	}
	
	@RequestMapping(value="/login", method=RequestMethod.GET)
	@ResponseBody
	@ApiDescription("Use this method to get the current user. Throws a 401 if not logged in.")
	public FmsUser getCurrentUserProfile() throws NotAuthenticatedException {
		FmsUser out = FmsSessionHolder.getCurrentUser();
		if(out == null) {
			throw new NotAuthenticatedException();
		}
		return out;
	}
	
	private void addCookie(HttpServletResponse response, String key, long expires) {
		String sessionCookieName = FmsSessionHolder.getSessionCookieName();
		Cookie c = new Cookie(sessionCookieName, key);
		if(log.isDebugEnabled()){ log.debug(" CONTROLLER ADDING cookie: "+key+" / "+expires); }
		if(expires>-1) {
			c.setMaxAge(0);
		}
		c.setPath("/");
		response.addCookie(c);
	}
	
	@RequestMapping(value="/login", method=RequestMethod.DELETE)
	@ResponseBody
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ApiDescription("Use this method to end your session (i.e. log out).")
	public void logout(HttpServletResponse response) throws NotAuthenticatedException {
		authenticationService.logout();
		FmsSession session = FmsSessionHolder.get();
		if(session!=null) {
			addCookie(response, session.getKey(),0);
			authenticationService.destroySession(FmsSessionHolder.get());
		}
	}
	
	
	@RequestMapping(value="/login", method=RequestMethod.PUT)
	@ResponseBody
	@ApiDescription("Use this method to log in with an email/password combination. You should PUT an object that has an 'email' and a 'password' field")
	public FmsUser login(@Valid @RequestBody FmsAuthentication auth, HttpServletRequest request, HttpServletResponse response) throws AccessDeniedException {
		FmsSession session = FmsSessionHolder.get();
		if(session!=null) {
			addCookie(response, session.getKey(),0);
			authenticationService.destroySession(FmsSessionHolder.get());
		}
		FmsUser user = authenticationService.authenticate(auth);
		if(user!=null) {
			session = authenticationService.createSession();
			session.setUserId(user.getId());
			session.setUser(user);
			authenticationService.updateSession(session);
			FmsSessionHolder.set(session);
			String newKey = FmsSessionHolder.get().getKey();
			addCookie(response, newKey,-1);
		}
		return user; 
	}
	
	
	@RequestMapping(value="/reset", method=RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.NO_CONTENT)
	@ResponseBody
	@ApiDescription("This can be used to reset the password for a email/password combination. If only the email is set, a mail-based email/password combination is triggered. If email, old password or token and new password is set, and the old password/token is correct, the password is simply reset.")
	public void resetEmailPassword(@Valid @RequestBody FmsAuthentication auth) throws AccessDeniedException, DatabaseException {
		authenticationService.resetAuthentication(auth);
	}
	
	public static void main(String[] args) {
		BCrypt bc = new BCrypt();
		System.err.println(bc.hashpw("abc123", bc.gensalt()));
	}
	

}
