package com.groupon.mobile;

import java.util.List;
import java.util.Map;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.groupon.mobile.conn.GrouponCallback;
import com.groupon.mobile.model.Task;
import com.groupon.mobile.service.TaskService;

public class TaskActivity extends BaseActivity {
	private TextView taskNameField;
	private TextView taskDescriptionField;
	private TextView taskDeadlineField;
	private TextView taskOwner;
	private TextView taskDescriptionLabel;
	private TextView taskfollowercount;
	private TextView taskCommunityName;
	private Button followTaskButton;
	private long taskId;
	private Task task;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		taskId = getIntent().getLongExtra("taskId", -1);

		setContentView(R.layout.activity_task);
		setTask();

	}

	private void setupUI() {

		taskNameField = (TextView) findViewById(R.id.task_name);
		taskNameField.setText(task.getName() );
		taskCommunityName = (TextView) findViewById(R.id.task_community_name);
		taskCommunityName.setText("Community: " + task.getCommunityName());
		taskDescriptionField = (TextView) findViewById(R.id.task_description);
		taskDescriptionField.setText(task.getDescription());
		taskDescriptionLabel = (TextView) findViewById(R.id.task_description_info);
		taskDescriptionLabel.setVisibility(View.VISIBLE);
		taskDeadlineField = (TextView) findViewById(R.id.task_deadline);
		taskDeadlineField.setText(task.getDeadlineCount().toString() + " days left");
		taskOwner = (TextView) findViewById(R.id.task_owner);
		taskOwner.setText("by " + task.getOwnerUsername());

		String needType = task.getNeedType();
		TextView requirement = (TextView) findViewById(R.id.task_requirement);
		if (needType.equals("GOODS")) {

			requirement.setText(task.getRequirementQuantity() + " more " + task.getRequirementName() + " needed");
		} else if (needType.equals("SERVICE")) {
			requirement.setText(task.getRequirementName() + " needed");
		}
		taskfollowercount = (TextView) findViewById(R.id.task_follower_count);
		taskfollowercount.setText(task.getFollowerCount() + " followers");
		followTaskButton = (Button) findViewById(R.id.task_follow_button);
		followTaskButton.setVisibility(View.VISIBLE);
		if (!task.isFollower()) {
			followTaskButton.setOnClickListener(followTaskListener);
		} else {
			followTaskButton.setText("Unfollow");
			followTaskButton.setOnClickListener(unFollowTaskListener);
		}

		Map<String, List<String>> m = task.getAttributeMap();
		LinearLayout mainLayout = (LinearLayout) findViewById(R.id.mainLinear);
		for (Map.Entry<String, List<String>> entry : m.entrySet()) {
			String attributeName = entry.getKey();
			List<String> AttributeValues = entry.getValue();
			LinearLayout taskAttributeLayout = new LinearLayout(this.getApplicationContext());
			taskAttributeLayout.setOrientation(LinearLayout.VERTICAL);
			TextView attributeTextView = new TextView(this.getApplicationContext());
			attributeTextView.setText(attributeName);
			attributeTextView.setTextColor(Color.BLACK);
			taskAttributeLayout.addView(attributeTextView);
			for (String attributeValue : AttributeValues) {
				TextView attributeNameView = new TextView(this.getApplicationContext());
				attributeNameView.setText("\t" + attributeValue);
				
				attributeNameView.setTextColor(Color.BLACK);
				taskAttributeLayout.addView(attributeNameView);
			}
			mainLayout.addView(taskAttributeLayout);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.task, menu);
		return true;
	}

	private OnClickListener followTaskListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			TaskService taskService = new TaskService(getApp());
			taskService.followTask(taskId, new GrouponCallback<Task>() {

				@Override
				public void onSuccess(Task response) {

					followTaskButton.setText("unfollow");
					followTaskButton.setOnClickListener(unFollowTaskListener);
					taskfollowercount.setText(response.getFollowerCount() + " followers");
				}

				@Override
				public void onFail(String errorMessage) {

				}
			});
		}
	};
	private OnClickListener unFollowTaskListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			TaskService taskService = new TaskService(getApp());
			taskService.unFollowTask(taskId, new GrouponCallback<Task>() {

				@Override
				public void onSuccess(Task response) {

					followTaskButton.setText("follow");
					followTaskButton.setOnClickListener(followTaskListener);
					taskfollowercount.setText(response.getFollowerCount() + " followers");
				}

				@Override
				public void onFail(String errorMessage) {

				}
			});
		}
	};

	private void setTask() {
		TaskService taskService = new TaskService(getApp());
		taskService.getTask(taskId, new GrouponCallback<Task>() {

			@Override
			public void onSuccess(Task task) {
				TaskActivity.this.task = task;
				setupUI();

			}

			@Override
			public void onFail(String errorMessage) {

			}
		});

	}
}
