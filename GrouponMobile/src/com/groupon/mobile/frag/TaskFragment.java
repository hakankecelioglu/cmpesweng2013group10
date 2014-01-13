package com.groupon.mobile.frag;

import java.util.ArrayList;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.groupon.mobile.GrouponApplication;
import com.groupon.mobile.R;
import com.groupon.mobile.conn.GrouponCallback;
import com.groupon.mobile.frag.ReplyFragment.refreshListener;
import com.groupon.mobile.layout.TaskReplyAdapter;
import com.groupon.mobile.model.Task;
import com.groupon.mobile.model.TaskReply;
import com.groupon.mobile.service.TaskService;

/**
 * Fragment to display a view of task.
 * 
 * @author serkan
 * 
 */
public class TaskFragment extends Fragment implements refreshListener {
	private GrouponApplication app;

	private ListView listview;
	private TaskReplyAdapter adapter;
	private ArrayList<TaskReply> taskReplies = new ArrayList<TaskReply>();

	private long taskId;
	private Task task;

	private TextView taskNameField;
	private TextView taskDescriptionField;
	private TextView taskDeadlineField;
	private TextView taskOwner;
	private TextView taskfollowercount;
	private TextView taskCommunityName;
	private TextView requirement;
	private TextView taskRepliesTitle;
	private LinearLayout mainLayout;
	private Button followTaskButton;
	private Button unfollowTaskButton;
	private Button replyTaskButton;
	private  View rootView;
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_task, container, false);
		
		app = (GrouponApplication) getActivity().getApplication();

		taskId = getArguments().getLong("taskId", -1);
		setupUI(rootView);

		if (task != null) {
			fillTaskFields();
		} else {
			setTask();
			setTaskReplies();
		}

		return rootView;
	}

	/**
	 * setup UI of this fragment.
	 * 
	 * @param rootView
	 *            root view of this fragment.
	 */
	private void setupUI(View rootView) {
		listview = (ListView) rootView.findViewById(R.id.listview);
		View listHeader = View.inflate(getActivity(), R.layout.task_header, null);

		taskNameField = (TextView) listHeader.findViewById(R.id.task_name);
		taskCommunityName = (TextView) listHeader.findViewById(R.id.task_community_name);
		taskDescriptionField = (TextView) listHeader.findViewById(R.id.task_description);
		taskDeadlineField = (TextView) listHeader.findViewById(R.id.task_deadline);
		taskOwner = (TextView) listHeader.findViewById(R.id.task_owner);
		requirement = (TextView) listHeader.findViewById(R.id.task_requirement);
		taskfollowercount = (TextView) listHeader.findViewById(R.id.task_follower_count);
		followTaskButton = (Button) listHeader.findViewById(R.id.task_follow_button);
		unfollowTaskButton = (Button) listHeader.findViewById(R.id.task_unfollow_button);
		mainLayout = (LinearLayout) listHeader.findViewById(R.id.mainLinear);
		taskRepliesTitle = (TextView) listHeader.findViewById(R.id.textview_task_replies_title);

		adapter = new TaskReplyAdapter(app, getActivity(), R.layout.listview_taskreply, taskReplies);
		adapter.setFragmentManager(getFragmentManager());
		listview.addHeaderView(listHeader);
		listview.setAdapter(adapter);

		replyTaskButton = (Button) listHeader.findViewById(R.id.task_reply_button);

		if (taskReplies.size() > 0) {
			taskRepliesTitle.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * setup UI of this fragment.
	 * 
	 * @param rootView
	 *            root view of this fragment.
	 */
	private void fillTaskFields() {
		taskNameField.setText(task.getName());
		taskCommunityName.setText(task.getCommunityName());
		taskDescriptionField.setText(task.getDescription());
		taskDeadlineField.setText(task.getDeadlineCount().toString() + " days left");
		taskOwner.setText(task.getOwnerUsername());
		String needType = task.getNeedType();

		if (needType.equals("GOODS")) {
			requirement.setText(task.getRequirementQuantity() + " more " + task.getRequirementName() + " needed");
		} else if (needType.equals("SERVICE")) {
			requirement.setText(task.getRequirementName() + " needed");
		}

		taskfollowercount.setText(task.getFollowerCount() + " followers");
		setFollowerUI();
		setNeedTypeUI();
		setTaskAttributesUI();
		replyTaskButton.setOnClickListener(replyListener);
	}

	private void setTask() {
		TaskService taskService = new TaskService(app);
		taskService.getTask(taskId, new GrouponCallback<Task>() {
			@Override
			public void onSuccess(Task task) {
				TaskFragment.this.task = task;
				fillTaskFields();
			}

			@Override
			public void onFail(String errorMessage) {
				Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
			}
		});
	}

	/**
	 * In�tialize follow task button depending on whether user is a follower.
	 * 
	 * @param rootView
	 *            root view of this fragment.
	 */
	private void setTaskReplies() {
		TaskService taskService = new TaskService(app);
		taskService.getTaskReplies(taskId, new GrouponCallback<List<TaskReply>>() {
			@Override
			public void onSuccess(List<TaskReply> response) {
				taskReplies.addAll(response);
				adapter.notifyDataSetChanged();

				if (taskReplies.size() > 0) {
					taskRepliesTitle.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void onFail(String errorMessage) {

			}
		});
	}

	private void setFollowerUI() {
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

	/**
	 * Initalize requirement ui depending on need type of task.
	 * 
	 * @param rootView
	 *            root view of this fragment.
	 */
	private void setNeedTypeUI() {
		String needType = task.getNeedType();
		if (needType.equals("GOODS")) {
			requirement.setText(task.getRequirementQuantity() + " more " + task.getRequirementName() + " needed");
		} else if (needType.equals("SERVICE")) {
			requirement.setText(task.getRequirementName() + " needed");
		} else {
			requirement.setVisibility(View.GONE);
		}
	}

	/**
	 * Initalize ui for attributes f the task.
	 * 
	 * @param rootView
	 *            root view of this fragment.
	 */
	private void setTaskAttributesUI() {
		Map<String, List<String>> m = task.getAttributeMap();
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
			replyFragment.setListen(TaskFragment.this);
			FragmentManager fm = getFragmentManager();
			replyFragment.show(fm, "reply");

		}

	};
	public void refresh(int quantity){
		if (task.getNeedType().equals("GOODS")){
			task.setRequirementQuantity(task.getRequirementQuantity()-quantity);
			requirement.setText(task.getRequirementQuantity()+ " more " + task.getRequirementName());
		}
		setTaskReplies();
	}
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
					Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
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
					Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
				}
			});
		}
	};

}
