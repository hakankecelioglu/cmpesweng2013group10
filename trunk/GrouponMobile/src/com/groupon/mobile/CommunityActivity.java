package com.groupon.mobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CommunityActivity extends Activity {
	private Button createButton;
	private TextView communityNameField;
	private TextView communityDescriptionField;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_community);
		
		getIntent().getIntExtra("communityId", -1);
		
		setupUI();
	}
	private void setupUI() {
		createButton = (Button) findViewById(R.id.button_create_community);		
		createButton.setOnClickListener(createButtonClickListener);
		communityNameField = (EditText) findViewById(R.id.communityName);
		communityDescriptionField = (EditText) findViewById(R.id.communityDescription);
	}
	private OnClickListener createButtonClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {

			
			Intent intent = new Intent(CommunityActivity.this, CreateTaskActivity.class);
			startActivity(intent);

		}
	};
}
