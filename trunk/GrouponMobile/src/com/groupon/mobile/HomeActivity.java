package com.groupon.mobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.groupon.mobile.model.User;

public class HomeActivity extends BaseActivity {
	private User user;
	private Button createCommunityTaskButton;
	private Button myCommunitiesButton;
	private Button logoutButton;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		user = getLoggedUser();

		TextView view = (TextView) findViewById(R.id.home_hello_username);
		view.setText("Hello, " + user.getUsername() + "!");
		final TextView view2 = (TextView) findViewById(R.id.conn);
		view2.setText(" ");

		setupUI();
	}

	private void setupUI() {
		createCommunityTaskButton = (Button) findViewById(R.id.button_home_create_new_community);
		createCommunityTaskButton.setOnClickListener(createNewCommunityListener);
		myCommunitiesButton = (Button) findViewById(R.id.button_my_communities);
		myCommunitiesButton.setOnClickListener(myCommunitiesListener);
		logoutButton = (Button) findViewById(R.id.button_logout);
		logoutButton.setOnClickListener(logoutClickListener);
	}

	private OnClickListener createNewCommunityListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(HomeActivity.this, CreateCommunityActivity.class);
			startActivity(intent);
		}
	};

	private OnClickListener myCommunitiesListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(HomeActivity.this, MyCommunitiesActivity.class);
			startActivity(intent);
		}
	};

	private OnClickListener logoutClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			getApp().setAuthToken(null);
			getApp().setLoggedUser(null);

			Intent intent = new Intent(HomeActivity.this, MainActivity.class);
			startActivity(intent);
			finish();
		}
	};
}
