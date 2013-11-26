package com.groupon.mobile;

import java.util.HashMap;
import java.util.Map;

import com.groupon.mobile.conn.ConnectionUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateCommunityActivity extends Activity {

	private Button createButton;
	private EditText communityNameField;
	private EditText communityDescriptionField;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_community);

		setupUI();
	}

	private void setupUI() {
		createButton = (Button) findViewById(R.id.button_create_community);
		createButton.setOnClickListener(createButtonClickListener);

		communityNameField = (EditText) findViewById(R.id.community_name);
		communityDescriptionField = (EditText) findViewById(R.id.community_description);
	}

	private OnClickListener createButtonClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			String name = communityNameField.getText().toString();
			String description = communityDescriptionField.getText().toString();

			if (name.trim().equals("")) {
				Toast.makeText(CreateCommunityActivity.this, "Community name cannot be empty!", Toast.LENGTH_SHORT).show();
				return;
			}

			if (description.trim().equals("")) {
				Toast.makeText(CreateCommunityActivity.this, "Community description cannot be empty!", Toast.LENGTH_SHORT).show();
				return;
			}
			final Map<String, String> community = new HashMap<String, String>();
			community.put("name", name);
			community.put("description", description);
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
			        	ConnectionUtils.makePostRequest("http://192.168.1.42:1616/createCommunityAndroid", community, null);
			        } catch (Exception e) {
			            e.printStackTrace();
			        }
				}
			});
			t.start();		
			
			int communityId = DummyController.createCommunity(name, description);
			Intent intent = new Intent(CreateCommunityActivity.this, CommunityActivity.class);
			intent.putExtra("communityId", communityId);
			startActivity(intent);
			finish();
		}
	};

}
