package com.groupon.mobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.groupon.mobile.conn.ConnectionUtils;
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
		final TextView view2 = (TextView) findViewById(R.id.conn);
		view2.setText("dummy");
		
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
		        	final String response=ConnectionUtils.makeGetRequest("http://192.168.1.42:1616/dummy", null, null);
		        	
		        	runOnUiThread(new Runnable() {
		        		@Override
		        		public void run() {
		        			view2.setText(response);
		        		}
		        	});
		        } catch (Exception e) {
		            e.printStackTrace();
		        }
			}
		});
		t.start();
		
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
