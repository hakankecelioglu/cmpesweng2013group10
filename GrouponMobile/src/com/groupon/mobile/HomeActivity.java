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
	private Button profileButton;
	private Button createCommunityTaskButton;
	private Button myCommunitiesButton;
	private Button followedTaskButton;
	private Button logoutButton;
	private Button allCommunitiesButton;

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
		allCommunitiesButton= (Button)findViewById(R.id.button_all_communities);
		allCommunitiesButton.setOnClickListener(allCommunitiesListener);
		myCommunitiesButton.setOnClickListener(myCommunitiesListener);
		logoutButton = (Button) findViewById(R.id.button_logout);
		logoutButton.setOnClickListener(logoutClickListener);

		profileButton = (Button) findViewById(R.id.button_home_my_profile);

		profileButton.setOnClickListener(profileClickListener);
		followedTaskButton = (Button) findViewById(R.id.button_followed_tasks);
		followedTaskButton.setOnClickListener(followedTasksListener);
	}

	private OnClickListener followedTasksListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(HomeActivity.this, FollowedTasksActivity.class);
			startActivity(intent);
		}
	};
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
	private OnClickListener allCommunitiesListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(HomeActivity.this, MyCommunitiesActivity.class);
			intent.putExtra("all", true);
			startActivity(intent);
		}
	};
	private OnClickListener profileClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
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
