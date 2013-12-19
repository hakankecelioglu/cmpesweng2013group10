package com.groupon.mobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.groupon.mobile.conn.GrouponCallback;
import com.groupon.mobile.model.Community;
import com.groupon.mobile.model.Task;
import com.groupon.mobile.service.CommunityService;
import com.groupon.mobile.service.TaskService;
import com.groupon.mobile.utils.ImageUtils;

public class CommunityActivity extends BaseActivity {
	private Button createButton;
	private Button createTypeButton;
	private Button joinCommunityButton;
	private TextView communityNameField;
	private TextView communityDescriptionField;
	private Button taskButton;
	private long communityId;
	private ImageView communityPicture;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_community);

		communityId = getIntent().getLongExtra("communityId", -1);

		CommunityService service = new CommunityService(getApp());
		service.getCommunity(communityId, new GrouponCallback<Community>() {
			public void onSuccess(Community community) {
				setupUI(community.getName(), community.getDescription(), community.getPicture());
			}

			public void onFail(String errorMessage) {
				Toast.makeText(CommunityActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void setupUI(String name, String description, String pictureUrl) {
		createTypeButton = (Button) findViewById(R.id.button_create_task_type);
		createTypeButton.setOnClickListener(createTypeButtonClickListener);
		taskButton = (Button)findViewById(R.id.button_tasks_community);
		
		taskButton.setOnClickListener(tasksListener);
		joinCommunityButton = (Button)findViewById(R.id.button_join_community);
		joinCommunityButton.setOnClickListener(joinCommunityListener);
		createButton = (Button) findViewById(R.id.button_create_task);
		createButton.setOnClickListener(createButtonClickListener);
		communityNameField = (TextView) findViewById(R.id.communityName);
		communityNameField.setText(name);
		communityDescriptionField = (TextView) findViewById(R.id.communityDescription);
		communityDescriptionField.setText(description);
		communityPicture = (ImageView) findViewById(R.id.community_picture);
		ImageUtils.loadBitmap(communityPicture, pictureUrl);
	}
	private OnClickListener tasksListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(CommunityActivity.this, CommunityTasksActivity.class);
			intent.putExtra("communityId", communityId);
			startActivity(intent);
			
		}
	};
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
private OnClickListener joinCommunityListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			CommunityService communityService = new CommunityService(getApp());
			communityService.joinCommunity(communityId, new GrouponCallback<Community>() {

				@Override
				public void onSuccess(Community response) {
					
					joinCommunityButton.setText("Leave");
					joinCommunityButton.setOnClickListener(leaveCommunityListener);
					
				}

				@Override
				public void onFail(String errorMessage) {
					
					
				}
			});
		}
	};
	private OnClickListener leaveCommunityListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			CommunityService communityService = new CommunityService(getApp());
			communityService.leaveCommunity(communityId, new GrouponCallback<Community>() {

				@Override
				public void onSuccess(Community response) {
					
					joinCommunityButton.setText("Join");
					joinCommunityButton.setOnClickListener(joinCommunityListener);
					
				}

				@Override
				public void onFail(String errorMessage) {
					
					
				}
			});
		}
	};	

}
