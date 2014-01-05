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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.groupon.mobile.R;
import com.groupon.mobile.frag.CommunityFragment;
import com.groupon.mobile.frag.ProfileFragment;
import com.groupon.mobile.frag.TaskFragment;
import com.groupon.mobile.model.Task;

public class TaskAdapter extends ArrayAdapter<Task> {
	private int resource;
	private FragmentManager fragmentManager;
	private boolean communityNameClickable = true;

	public TaskAdapter(Context context, int resource, List<Task> items) {
		super(context, resource, items);
		this.resource = resource;
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

		return alertView;
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
}
