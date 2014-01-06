package com.groupon.mobile.layout;

import java.util.List;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.groupon.mobile.GrouponApplication;
import com.groupon.mobile.R;
import com.groupon.mobile.conn.GrouponCallback;
import com.groupon.mobile.frag.CommunityFragment;
import com.groupon.mobile.frag.ProfileFragment;
import com.groupon.mobile.frag.TaskFragment;
import com.groupon.mobile.model.Task;
import com.groupon.mobile.service.TaskService;

public class TaskAdapter extends ArrayAdapter<Task> {
	private int resource;
	private FragmentManager fragmentManager;
	private boolean communityNameClickable = true;
	private GrouponApplication app;

	public TaskAdapter(GrouponApplication app, Context context, int resource, List<Task> items) {
		super(context, resource, items);
		this.resource = resource;
		this.app = app;
	}

	public void setCommunityNameClickable(boolean c) {
		this.communityNameClickable = c;
	}

	public void setFragmentManager(FragmentManager fm) {
		this.fragmentManager = fm;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		LinearLayout alertView;

		Task task = getItem(position);

		if (convertView == null) {
			alertView = new LinearLayout(getContext());
			String inflater = Context.LAYOUT_INFLATER_SERVICE;
			LayoutInflater vi;
			vi = (LayoutInflater) getContext().getSystemService(inflater);
			vi.inflate(resource, alertView, true);
		} else {
			alertView = (LinearLayout) convertView;
		}

		TextView taskName = (TextView) alertView.findViewById(R.id.task_name);
		TextView taskDescription = (TextView) alertView.findViewById(R.id.task_description);
		TextView taskBy = (TextView) alertView.findViewById(R.id.task_by);
		TextView taskCommunity = (TextView) alertView.findViewById(R.id.task_community_name);
		Button followButton = (Button) alertView.findViewById(R.id.follow_button);
		Button unfollowButton = (Button) alertView.findViewById(R.id.unfollow_button);

		taskName.setText(task.getName());
		taskName.setTag(task.getId());
		taskName.setOnClickListener(taskNameClickListener);

		taskDescription.setText(task.getDescription());

		taskBy.setText(task.getOwnerUsername());
		taskBy.setTag(task.getOwnerId());
		taskBy.setOnClickListener(userNameClickListener);

		taskCommunity.setText(task.getCommunityName());
		taskCommunity.setTag(task.getCommunityId());
		if (communityNameClickable) {
			taskCommunity.setOnClickListener(communityNameClickListener);
		}

		followButton.setTag(task.getId());
		followButton.setOnClickListener(followClickListener);

		unfollowButton.setTag(task.getId());
		unfollowButton.setOnClickListener(unfollowClickListener);

		if (task.isFollower()) {
			convertFollowButtonToUnfollow(alertView);
		} else {
			convertUnfollowButtonToFollow(alertView);
		}

		return alertView;
	}

	private void convertFollowButtonToUnfollow(View view) {
		Button followButton = (Button) view.findViewById(R.id.follow_button);
		Button unfollowButton = (Button) view.findViewById(R.id.unfollow_button);

		followButton.setVisibility(View.GONE);
		unfollowButton.setVisibility(View.VISIBLE);
	}

	private void convertUnfollowButtonToFollow(View view) {
		Button followButton = (Button) view.findViewById(R.id.follow_button);
		Button unfollowButton = (Button) view.findViewById(R.id.unfollow_button);

		followButton.setVisibility(View.VISIBLE);
		unfollowButton.setVisibility(View.GONE);
	}

	private View.OnClickListener taskNameClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			Long taskId = (Long) v.getTag();

			// Allow no NullPointerException!
			if (fragmentManager == null)
				return;

			Bundle bundle = new Bundle();
			bundle.putLong("taskId", taskId);

			Fragment fragment = new TaskFragment();
			fragment.setArguments(bundle);

			FragmentTransaction transaction = fragmentManager.beginTransaction();
			transaction.addToBackStack(null);
			transaction.replace(R.id.frame_container, fragment);
			transaction.commit();
		}
	};

	private View.OnClickListener communityNameClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			// Allow no NullPointerException!
			if (fragmentManager == null)
				return;

			Long communityId = (Long) v.getTag();

			Bundle bundle = new Bundle();
			bundle.putLong("communityId", communityId);

			Fragment fragment = new CommunityFragment();
			fragment.setArguments(bundle);

			FragmentTransaction transaction = fragmentManager.beginTransaction();
			transaction.addToBackStack(null);
			transaction.replace(R.id.frame_container, fragment);
			transaction.commit();
		}
	};

	private View.OnClickListener userNameClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			// Allow no NullPointerException!
			if (fragmentManager == null)
				return;

			Long userId = (Long) v.getTag();

			Bundle bundle = new Bundle();
			bundle.putLong("userId", userId);

			Fragment fragment = new ProfileFragment();
			fragment.setArguments(bundle);

			FragmentTransaction transaction = fragmentManager.beginTransaction();
			transaction.addToBackStack(null);
			transaction.replace(R.id.frame_container, fragment);
			transaction.commit();
		}
	};

	private View.OnClickListener followClickListener = new View.OnClickListener() {
		public void onClick(final View v) {
			Long taskId = (Long) v.getTag();

			TaskService taskService = new TaskService(app);
			taskService.followTask(taskId, new GrouponCallback<Task>() {
				public void onSuccess(Task response) {
					convertFollowButtonToUnfollow((View) v.getParent());
				}

				public void onFail(String errorMessage) {

				}
			});
		}
	};

	private View.OnClickListener unfollowClickListener = new View.OnClickListener() {
		public void onClick(final View v) {
			Long taskId = (Long) v.getTag();

			TaskService taskService = new TaskService(app);
			taskService.unFollowTask(taskId, new GrouponCallback<Task>() {
				public void onSuccess(Task response) {
					convertUnfollowButtonToFollow((View) v.getParent());
				}

				public void onFail(String errorMessage) {

				}
			});
		}
	};
}
