package com.groupon.mobile.frag;

import java.util.List;
import java.util.Map;

import android.app.Fragment;
import android.app.FragmentManager;
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
	private TextView taskfollowercount;
	private TextView taskCommunityName;
	private Button followTaskButton;
	private Button unfollowTaskButton;
	private Button replyTaskButton;
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
		taskCommunityName.setText(task.getCommunityName());
		taskDescriptionField = (TextView) rootView.findViewById(R.id.task_description);
		taskDescriptionField.setText(task.getDescription());
		taskDeadlineField = (TextView) rootView.findViewById(R.id.task_deadline);
		taskDeadlineField.setText(task.getDeadlineCount().toString() + " days left");
		taskOwner = (TextView) rootView.findViewById(R.id.task_owner);
		taskOwner.setText(task.getOwnerUsername());

		String needType = task.getNeedType();
		TextView requirement = (TextView) rootView.findViewById(R.id.task_requirement);
		if (needType.equals("GOODS")) {

			requirement.setText(task.getRequirementQuantity() + " more " + task.getRequirementName() + " needed");
		} else if (needType.equals("SERVICE")) {
			requirement.setText(task.getRequirementName() + " needed");
		}
		taskfollowercount = (TextView) rootView.findViewById(R.id.task_follower_count);
		taskfollowercount.setText(task.getFollowerCount() + " followers");
		setFollowerUI(rootView);
		setNeedTypeUI(rootView);
		setTaskAttributesUI(rootView);
		replyTaskButton = (Button) rootView.findViewById(R.id.task_reply_button);
		replyTaskButton.setOnClickListener(replyListener);

	}

	private void setFollowerUI(View rootView) {
		followTaskButton = (Button) rootView.findViewById(R.id.task_follow_button);
		unfollowTaskButton = (Button) rootView.findViewById(R.id.task_unfollow_button);
		followTaskButton.setOnClickListener(followTaskListener);
		unfollowTaskButton.setOnClickListener(unFollowTaskListener);

		changeFollowUnfollowUI(task.isFollower());
	}

	private void changeFollowUnfollowUI(boolean isFollower) {
		if (!isFollower) {
			followTaskButton.setVisibility(View.VISIBLE);
			unfollowTaskButton.setVisibility(View.GONE);
		} else {
			followTaskButton.setVisibility(View.GONE);
			unfollowTaskButton.setVisibility(View.VISIBLE);
		}
	}

	private void setNeedTypeUI(View rootView) {
		String needType = task.getNeedType();
		TextView requirement = (TextView) rootView.findViewById(R.id.task_requirement);
		if (needType.equals("GOODS")) {
			requirement.setText(task.getRequirementQuantity() + " more " + task.getRequirementName() + " needed");
		} else if (needType.equals("SERVICE")) {
			requirement.setText(task.getRequirementName() + " needed");
		}
	}

	private void setTaskAttributesUI(View rootView) {
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
				taskAttributeLayout.addView(attributeNameView);
				attributeNameView.setTextColor(Color.BLACK);
			}
			mainLayout.addView(taskAttributeLayout);
		}

	}

	private OnClickListener replyListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			ReplyFragment replyFragment = new ReplyFragment();
			Bundle args = new Bundle();
			args.putLong("taskId", taskId);
			String needType = task.getNeedType();
			args.putString("needType", needType);
			if (needType.equals("GOODS") || needType.equals("SERVICE"))
				args.putString("requirementName", task.getRequirementName());
			if (needType.equals("GOODS"))
				args.putLong("requirementQuantity", task.getRequirementQuantity());
			replyFragment.setArguments(args);
			FragmentManager fm = getFragmentManager();
			replyFragment.show(fm, "reply");

		}

	};
	private OnClickListener followTaskListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			TaskService taskService = new TaskService(app);
			taskService.followTask(taskId, new GrouponCallback<Task>() {

				@Override
				public void onSuccess(Task response) {
					changeFollowUnfollowUI(true);
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
					changeFollowUnfollowUI(false);
					taskfollowercount.setText(response.getFollowerCount() + " followers");
				}

				@Override
				public void onFail(String errorMessage) {

				}
			});
		}
	};

}
