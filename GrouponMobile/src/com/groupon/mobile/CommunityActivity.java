package com.groupon.mobile;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.groupon.mobile.conn.ConnectionUtils;
import com.groupon.mobile.utils.Constants;
import com.groupon.mobile.utils.ImageUtils;

public class CommunityActivity extends BaseActivity {
	private Button createButton;
	private Button createTypeButton;
	private TextView communityNameField;
	private TextView communityDescriptionField;
	private long communityId;
	private ImageView communityPicture;

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
					final String communityPicture = community.getString("picture");
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							setupUI(communityName, communityDescription, communityPicture);
						}
					});

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		t.start();

	}

	private void setupUI(String name, String description, String pictureUrl) {
		createTypeButton = (Button) findViewById(R.id.button_create_task_type);
		createTypeButton.setOnClickListener(createTypeButtonClickListener);
		createButton = (Button) findViewById(R.id.button_create_task);
		createButton.setOnClickListener(createButtonClickListener);
		communityNameField = (TextView) findViewById(R.id.communityName);
		communityNameField.setText(name);
		communityDescriptionField = (TextView) findViewById(R.id.communityDescription);
		communityDescriptionField.setText(description);
		communityPicture = (ImageView) findViewById(R.id.community_picture);
		ImageUtils.loadBitmap(communityPicture, pictureUrl);
	}

	private OnClickListener createButtonClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {

			Intent intent = new Intent(CommunityActivity.this, CreateTaskActivity.class);
			intent.putExtra("communityId", communityId);
			startActivity(intent);

		}
	};
	private OnClickListener createTypeButtonClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {

			Intent intent = new Intent(CommunityActivity.this, CreateTaskTypeActivity.class);
			intent.putExtra("communityId", communityId);
			startActivity(intent);
		}
	};

}
