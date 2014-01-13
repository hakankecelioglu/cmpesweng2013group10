package com.groupon.mobile;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.groupon.mobile.conn.GrouponCallback;
import com.groupon.mobile.layout.TaskAdapter;
import com.groupon.mobile.model.Task;
import com.groupon.mobile.service.TaskService;
/**
 * This activity lists tasks belonging to a community.
 */
public class CommunityTasksActivity extends BaseActivity {
	
	TaskAdapter arrayAdapter;
	ArrayList<Task> tasks = new ArrayList<Task>();
	ListView listview;
	private long communityId;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_community_tasks);
		communityId = getIntent().getLongExtra("communityId", -1);
		setupUI();
	}
	/**
	 * setup UI of this activity.
	 */
	private void setupUI() {
		setupListView();
		listview.setOnItemClickListener(listViewListener);
	}
	/**
	 * 	ListView is filled with tasks response.
	 */
	private void setupListView() {
		listview = (ListView) findViewById(R.id.listview);
		TaskService taskService = new TaskService(getApp());
		arrayAdapter = new TaskAdapter(getApp(), CommunityTasksActivity.this, R.layout.listview_task, tasks);
		listview.setAdapter(arrayAdapter);
		taskService.getCommunityTasks(communityId, new GrouponCallback<ArrayList<Task>>() {
			public void onSuccess(ArrayList<Task> response) {
				for (Task t : response) {
					tasks.add(t);
				}

				arrayAdapter.notifyDataSetChanged();
			}

			public void onFail(String errorMessage) {
				Toast.makeText(CommunityTasksActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
			}
		});
	}

	private OnItemClickListener listViewListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			if (position >= 0 && position < tasks.size()) {
				/**
				 * TODO FIXME Task task = tasks.get(position); Intent intent =
				 * new Intent(CommunityTasksActivity.this, TaskActivity.class);
				 * intent.putExtra("taskId", task.getId());
				 * startActivity(intent);
				 */
			}
		}

	};
}