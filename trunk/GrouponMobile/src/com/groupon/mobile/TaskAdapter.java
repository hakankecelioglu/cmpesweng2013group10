package com.groupon.mobile;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.groupon.mobile.model.Task;

public class TaskAdapter extends ArrayAdapter<Task> {
	private int resource;
	private Context context;

	public TaskAdapter(Context context, int resource, List<Task> items) {
		super(context, resource, items);
		this.resource = resource;
		this.context = context;
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
		taskCommunity.setOnClickListener(communityNameClickListener);

		return alertView;
	}

	private View.OnClickListener taskNameClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			Long taskId = (Long) v.getTag();
			Intent intent = new Intent(context, TaskActivity.class);
			intent.putExtra("taskId", taskId);
			context.startActivity(intent);
		}
	};

	private View.OnClickListener communityNameClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			Long communityId = (Long) v.getTag();
			Intent intent = new Intent(context, CommunityActivity.class);
			intent.putExtra("communityId", communityId);
			context.startActivity(intent);
		}
	};

	private View.OnClickListener userNameClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			Long userId = (Long) v.getTag();
			Intent intent = new Intent(context, ProfileActivity.class);
			intent.putExtra("userId", userId);
			context.startActivity(intent);
		}
	};
}
