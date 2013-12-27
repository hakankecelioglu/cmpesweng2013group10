package com.groupon.mobile;

import java.util.List;
import com.groupon.mobile.model.Task;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import android.widget.LinearLayout;
import android.widget.TextView;

public class TaskAdapter extends ArrayAdapter<Task> {
	int resource;
	String response;
	Context context;

	public TaskAdapter(Context context, int resource, List<Task> items) {
		super(context, resource, items);
		this.resource = resource;
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
		taskName.setText(task.getName());
		taskDescription.setText(task.getDescription());
		taskBy.setText("by: " + task.getOwnerUsername());

		return alertView;
	}
}
