package com.groupon.mobile;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.groupon.mobile.conn.ConnectionUtils;
import com.groupon.mobile.utils.Constants;

public class CommunityActivity extends BaseActivity {
	private Button createButton;
	private TextView communityNameField;
	private TextView communityDescriptionField;
	private long communityId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_community);

		communityId = getIntent().getLongExtra("communityId", -1);

		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					String path = Constants.SERVER + "communityMobile/" + communityId;
					JSONObject obj = ConnectionUtils.makePostRequest(path, null, getAuthToken());

					JSONObject community = obj.getJSONObject("community");
					final String communityName = community.getString("name");
					final String communityDescription = community.getString("description");
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							setupUI(communityName, communityDescription);
						}
					});

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		t.start();

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
