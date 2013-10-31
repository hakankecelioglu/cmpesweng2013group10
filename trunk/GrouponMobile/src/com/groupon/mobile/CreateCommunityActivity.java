package com.groupon.mobile;

import android.app.Activity;
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
			
			int id = DummyController.createCommunity(name, description);
			//Intent intent = new Intent();
		}
	};

}
