package com.groupon.mobile;

import android.app.Activity;

import com.groupon.mobile.model.User;

/**
 * All activities will be a subclass of this type.
 * It provides necessary functions for authorization and setting and getting authorized user.
 * @author sedrik
 */
public class BaseActivity extends Activity {
	/**
	 * 
	 * @return	authorization token of logged user.
	 */
	protected String getAuthToken() {
		GrouponApplication app = (GrouponApplication) this.getApplication();
		return app.getAuthToken();
	}
	/**
	 * Set authorization token of logged user
	 * @param token The token which is sent to server for authorization purposes
	 */
	protected void setAuthToken(String token) {
		GrouponApplication app = (GrouponApplication) this.getApplication();
		app.setAuthToken(token);
	}
	/**
	 * 
	 * @return	user logged to the application
	 */
	protected User getLoggedUser() {
		GrouponApplication app = (GrouponApplication) this.getApplication();
		return app.getLoggedUser();
	}
	/**
	 * Saves user logged to the application in Application instance.
	 * @param user	returns logged user.
	 */
	protected void setLoggedUser(User user) {
		GrouponApplication app = (GrouponApplication) this.getApplication();
		app.setLoggedUser(user);
	}
	/**
	 * 
	 * @return used Application instance
	 */
	protected GrouponApplication getApp() {
		return (GrouponApplication) this.getApplication();
	}
}
