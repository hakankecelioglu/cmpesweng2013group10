package com.groupon.mobile;

import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class CommunityActivity extends Activity {
	private Button createButton;
	private TextView communityNameField;
	private TextView communityDescriptionField;
	private int communityId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_community);

		communityId = getIntent().getIntExtra("communityId", -1);
		Map<String, Object> m = DummyController.getCommunity(communityId);
		String name = (String) m.get("name");
		String description = (String) m.get("description");
		setupUI(name, description);
	}

	private void setupUI(String name, String description) {
		createButton = (Button) findViewById(R.id.button_create_task);
		createButton.setOnClickListener(createButtonClickListener);
		communityNameField = (TextView) findViewById(R.id.communityName);
		communityNameField.setText(name);
		communityDescriptionField = (TextView) findViewById(R.id.communityDescription);
		communityDescriptionField.setText(description);
	}

	private OnClickListener createButtonClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {

			Intent intent = new Intent(CommunityActivity.this, CreateTaskActivity.class);
			intent.putExtra("communityId", communityId);
			startActivity(intent);

		}
	};
}
