package com.groupon.mobile.frag;

import java.util.List;
import java.util.Map;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.groupon.mobile.GrouponApplication;
import com.groupon.mobile.R;
import com.groupon.mobile.conn.GrouponCallback;
import com.groupon.mobile.model.Task;
import com.groupon.mobile.service.TaskService;

public class TaskFragment extends Fragment {
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
	private GrouponApplication app;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.activity_task, container, false);

		app = (GrouponApplication) getActivity().getApplication();
		taskId = getArguments().getLong("taskId", -1);
		setTask(rootView);

		return rootView;
	}

	private void setTask(final View rootView) {
		TaskService taskService = new TaskService(app);
		taskService.getTask(taskId, new GrouponCallback<Task>() {

			@Override
			public void onSuccess(Task task) {
				TaskFragment.this.task = task;
				setupUI(rootView);
			}

			@Override
			public void onFail(String errorMessage) {

			}
		});
	}

	private void setupUI(View rootView) {
		taskNameField = (TextView) rootView.findViewById(R.id.task_name);
		taskNameField.setText(task.getName());
		taskCommunityName = (TextView) rootView.findViewById(R.id.task_community_name);
		taskCommunityName.setText("Community: " + task.getCommunityName());
		taskDescriptionField = (TextView) rootView.findViewById(R.id.task_description);
		taskDescriptionField.setText(task.getDescription());
		taskDescriptionLabel = (TextView) rootView.findViewById(R.id.task_description_info);
		taskDescriptionLabel.setVisibility(View.VISIBLE);
		taskDeadlineField = (TextView) rootView.findViewById(R.id.task_deadline);
		taskDeadlineField.setText(task.getDeadlineCount().toString() + " days left");
		taskOwner = (TextView) rootView.findViewById(R.id.task_owner);
		taskOwner.setText("by " + task.getOwnerUsername());

		String needType = task.getNeedType();
		TextView requirement = (TextView) rootView.findViewById(R.id.task_requirement);
		if (needType.equals("GOODS")) {

			requirement.setText(task.getRequirementQuantity() + " more " + task.getRequirementName() + " needed");
		} else if (needType.equals("SERVICE")) {
			requirement.setText(task.getRequirementName() + " needed");
		}
		taskfollowercount = (TextView) rootView.findViewById(R.id.task_follower_count);
		taskfollowercount.setText(task.getFollowerCount() + " followers");
		followTaskButton = (Button) rootView.findViewById(R.id.task_follow_button);
		followTaskButton.setVisibility(View.VISIBLE);
		if (!task.isFollower()) {
			followTaskButton.setOnClickListener(followTaskListener);
		} else {
			followTaskButton.setText("Unfollow");
			followTaskButton.setOnClickListener(unFollowTaskListener);
		}

		Map<String, List<String>> m = task.getAttributeMap();
		LinearLayout mainLayout = (LinearLayout) rootView.findViewById(R.id.mainLinear);
		for (Map.Entry<String, List<String>> entry : m.entrySet()) {
			String attributeName = entry.getKey();
			List<String> AttributeValues = entry.getValue();
			LinearLayout taskAttributeLayout = new LinearLayout(app.getApplicationContext());
			taskAttributeLayout.setOrientation(LinearLayout.VERTICAL);
			TextView attributeTextView = new TextView(app.getApplicationContext());
			attributeTextView.setText(attributeName);
			attributeTextView.setTextColor(Color.BLACK);
			taskAttributeLayout.addView(attributeTextView);
			for (String attributeValue : AttributeValues) {
				TextView attributeNameView = new TextView(app.getApplicationContext());
				attributeNameView.setText("\t" + attributeValue);

				attributeNameView.setTextColor(Color.BLACK);
				taskAttributeLayout.addView(attributeNameView);
			}
			mainLayout.addView(taskAttributeLayout);
		}
	}

	private OnClickListener followTaskListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			TaskService taskService = new TaskService(app);
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
			TaskService taskService = new TaskService(app);
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
}
