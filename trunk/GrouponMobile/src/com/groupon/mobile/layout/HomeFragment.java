package com.groupon.mobile.layout;

import java.util.ArrayList;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.groupon.mobile.GrouponApplication;
import com.groupon.mobile.R;
import com.groupon.mobile.TaskActivity;
import com.groupon.mobile.TaskAdapter;
import com.groupon.mobile.conn.GrouponCallback;
import com.groupon.mobile.model.Task;
import com.groupon.mobile.service.TaskService;

public class HomeFragment extends Fragment {
	private TaskAdapter arrayAdapter;
	private ArrayList<Task> tasks = new ArrayList<Task>();
	private ListView listview;

	public HomeFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.activity_home, container, false);
		setupListView(rootView);
		return rootView;
	}

	private void setupListView(View view) {
		listview = (ListView) view.findViewById(R.id.listview);
		TaskService taskService = new TaskService((GrouponApplication) getActivity().getApplication());
		arrayAdapter = new TaskAdapter(getActivity(), R.layout.listview_task, tasks);
		listview.setAdapter(arrayAdapter);
		taskService.getFollowedTasks(new GrouponCallback<ArrayList<Task>>() {
			public void onSuccess(ArrayList<Task> response) {
				for (Task t : response) {
					tasks.add(t);
				}

				arrayAdapter.notifyDataSetChanged();
			}

			public void onFail(String errorMessage) {
				Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
			}
		});

		listview.setOnItemClickListener(listViewListener);
	}

	private OnItemClickListener listViewListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			if (position >= 0 && position < tasks.size()) {
				Task task = tasks.get(position);
				Intent intent = new Intent(getActivity(), TaskActivity.class);
				intent.putExtra("taskId", task.getId());
				startActivity(intent);
			}
		}
	};
}