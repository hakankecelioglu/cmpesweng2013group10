package com.groupon.mobile;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.groupon.mobile.conn.GrouponCallback;
import com.groupon.mobile.model.Task;
import com.groupon.mobile.service.TaskService;

public class HomeActivity extends BaseActivity {
	private ImageButton profileButton;
	private Button createCommunityTaskButton;
	private Button myCommunitiesButton;
	private ImageButton logoutButton;
	private Button allCommunitiesButton;
	TaskAdapter arrayAdapter;
	ArrayList<Task> tasks = new ArrayList<Task>();
	ListView listview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		setupUI();
	}

	private void setupUI() {
		createCommunityTaskButton = (Button) findViewById(R.id.button_home_create_new_community);
		createCommunityTaskButton.setOnClickListener(createNewCommunityListener);
		myCommunitiesButton = (Button) findViewById(R.id.button_my_communities);
		allCommunitiesButton = (Button) findViewById(R.id.button_all_communities);
		allCommunitiesButton.setOnClickListener(allCommunitiesListener);
		myCommunitiesButton.setOnClickListener(myCommunitiesListener);
		logoutButton = (ImageButton) findViewById(R.id.button_logout);
		logoutButton.setOnClickListener(logoutClickListener);

		profileButton = (ImageButton) findViewById(R.id.button_home_my_profile);

		profileButton.setOnClickListener(profileClickListener);
		setupListView();
		listview.setOnItemClickListener(listViewListener);
	}

	private OnClickListener createNewCommunityListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(HomeActivity.this, CreateCommunityActivity.class);
			startActivity(intent);
		}
	};

	private OnClickListener myCommunitiesListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(HomeActivity.this, MyCommunitiesActivity.class);
			startActivity(intent);
		}
	};
	private OnClickListener allCommunitiesListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(HomeActivity.this, MyCommunitiesActivity.class);
			intent.putExtra("all", true);
			startActivity(intent);
		}
	};
	private OnClickListener profileClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
			startActivity(intent);
		}
	};

	private OnClickListener logoutClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			getApp().setAuthToken(null);
			getApp().setLoggedUser(null);

			Intent intent = new Intent(HomeActivity.this, MainActivity.class);
			startActivity(intent);
			finish();
		}
	};

	private void setupListView() {
		listview = (ListView) findViewById(R.id.listview);
		TaskService taskService = new TaskService(getApp());
		arrayAdapter = new TaskAdapter(HomeActivity.this, R.layout.listview_task, tasks);
		listview.setAdapter(arrayAdapter);
		taskService.getFollowedTasks(new GrouponCallback<ArrayList<Task>>() {
			public void onSuccess(ArrayList<Task> response) {
				for (Task t : response) {
					tasks.add(t);
				}

				arrayAdapter.notifyDataSetChanged();
			}

			public void onFail(String errorMessage) {
				Toast.makeText(HomeActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
			}
		});

	}

	private OnItemClickListener listViewListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			if (position >= 0 && position < tasks.size()) {
				Task task = tasks.get(position);
				Intent intent = new Intent(HomeActivity.this, TaskActivity.class);
				intent.putExtra("taskId", task.getId());
				startActivity(intent);
			}
		}

	};

}
