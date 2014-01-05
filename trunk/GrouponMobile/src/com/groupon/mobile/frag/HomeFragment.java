package com.groupon.mobile.frag;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.groupon.mobile.GrouponApplication;
import com.groupon.mobile.R;
import com.groupon.mobile.conn.GrouponCallback;
import com.groupon.mobile.layout.TaskAdapter;
import com.groupon.mobile.model.Task;
import com.groupon.mobile.service.TaskService;

public class HomeFragment extends Fragment {
	private TaskAdapter arrayAdapter;
	private ArrayList<Task> tasks;
	private ListView listview;
	private Activity activity;

	public HomeFragment() {
	}

	@Override
	public void onAttach(android.app.Activity activity) {
		super.onAttach(activity);
		this.activity = activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.activity_home, container, false);
		if (tasks == null) {
			tasks = new ArrayList<Task>();
			setupUI(rootView);
			fillListView(rootView);
		} else {
			setupUI(rootView);
			arrayAdapter.notifyDataSetChanged();
		}
		return rootView;
	}

	private void setupUI(View view) {
		arrayAdapter = new TaskAdapter(getActivity(), R.layout.listview_task, tasks);
		arrayAdapter.setFragmentManager(getFragmentManager());

		listview = (ListView) view.findViewById(R.id.listview);
		listview.setAdapter(arrayAdapter);
	}

	private void fillListView(View view) {
		TaskService taskService = new TaskService((GrouponApplication) getActivity().getApplication());
		taskService.getHomeFeedTasks(onHomeFeedResponse);
	}

	private GrouponCallback<ArrayList<Task>> onHomeFeedResponse = new GrouponCallback<ArrayList<Task>>() {
		public void onSuccess(ArrayList<Task> response) {
			for (Task t : response) {
				tasks.add(t);
			}

			arrayAdapter.notifyDataSetChanged();
		}

		public void onFail(String errorMessage) {
			if (activity != null) {
				Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show();
			}
		}
	};
}