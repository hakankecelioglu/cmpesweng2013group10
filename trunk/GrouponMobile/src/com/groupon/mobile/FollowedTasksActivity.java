package com.groupon.mobile;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.groupon.mobile.conn.GrouponCallback;
import com.groupon.mobile.model.Task;
import com.groupon.mobile.service.TaskService;

public class FollowedTasksActivity  extends BaseActivity {
	TaskAdapter arrayAdapter;
	ArrayList<Task> tasks = new ArrayList<Task>();
	ListView listview;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_followed_tasks);

		setupUI();
	}

	private void setupUI() {
		setupListView();
		listview.setOnItemClickListener(listViewListener);
	}

	private void setupListView() {
		listview = (ListView) findViewById(R.id.listview);
		TaskService taskService = new TaskService(getApp());
		arrayAdapter = new TaskAdapter(FollowedTasksActivity.this, R.layout.listview_task, tasks);
		listview.setAdapter(arrayAdapter);
		taskService.getFollowedTasks(new GrouponCallback<ArrayList<Task>>() {
			public void onSuccess(ArrayList<Task> response) {
				for (Task t : response) {
					tasks.add(t);
				}

				arrayAdapter.notifyDataSetChanged();
			}

			public void onFail(String errorMessage) {
				Toast.makeText(FollowedTasksActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
			}
		});
	}

	private OnItemClickListener listViewListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			if (position >= 0 && position < tasks.size()) {
				Task task = tasks.get(position);
				Intent intent = new Intent(FollowedTasksActivity.this, TaskActivity.class);
				intent.putExtra("taskId", task.getId());
				startActivity(intent);
			}
		}

	};
}
