package com.groupon.mobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.groupon.mobile.conn.GrouponCallback;
import com.groupon.mobile.model.Community;
import com.groupon.mobile.service.CommunityService;

public class CreateCommunityActivity extends BaseActivity {

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

			Community community = new Community();
			community.setName(name);
			community.setDescription(description);

			CommunityService service = new CommunityService(getApp());
			service.createCommunity(community, new GrouponCallback<Community>() {
				public void onSuccess(Community response) {
					Intent intent = new Intent(CreateCommunityActivity.this, CommunityActivity.class);
					intent.putExtra("communityId", response.getId());
					startActivity(intent);
					finish();
				}

				public void onFail(String errorMessage) {
					Toast.makeText(CreateCommunityActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
				}
			});
		}
	};

}
