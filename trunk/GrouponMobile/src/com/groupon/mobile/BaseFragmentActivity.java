package com.groupon.mobile;

import android.support.v4.app.FragmentActivity;

import com.groupon.mobile.model.User;

public class BaseFragmentActivity extends FragmentActivity {
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
