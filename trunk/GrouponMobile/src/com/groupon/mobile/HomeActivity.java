package com.groupon.mobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.groupon.mobile.model.User;

public class HomeActivity extends Activity {
	private User user;
	private Button createCommunityTaskButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		GrouponApplication app = (GrouponApplication) getApplication();
		user = app.getLoggedUser();

		TextView view = (TextView) findViewById(R.id.home_hello_username);
		view.setText("Hello, " + user.getUsername() + "!");

		setupUI();
	}

	private void setupUI() {
		createCommunityTaskButton = (Button) findViewById(R.id.button_home_create_new_community);
		createCommunityTaskButton.setOnClickListener(createNewCommunityListener);
	}

	private OnClickListener createNewCommunityListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(HomeActivity.this, CreateCommunityActivity.class);
			startActivity(intent);
		}
	};
}
