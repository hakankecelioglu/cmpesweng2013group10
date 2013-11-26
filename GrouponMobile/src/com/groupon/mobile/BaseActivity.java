package com.groupon.mobile;

import android.app.Activity;

import com.groupon.mobile.model.User;

/**
 * All activities will be a subclass of this type.
 * 
 * @author sedrik
 */
public class BaseActivity extends Activity {
	protected String getAuthToken() {
		GrouponApplication app = (GrouponApplication) this.getApplication();
		return app.getAuthToken();
	}

	protected void setAuthToken(String token) {
		GrouponApplication app = (GrouponApplication) this.getApplication();
		app.setAuthToken(token);
	}

	protected User getLoggedUser() {
		GrouponApplication app = (GrouponApplication) this.getApplication();
		return app.getLoggedUser();
	}

	protected void setLoggedUser(User user) {
		GrouponApplication app = (GrouponApplication) this.getApplication();
		app.setLoggedUser(user);
	}

	protected GrouponApplication getApp() {
		return (GrouponApplication) this.getApplication();
	}
}
