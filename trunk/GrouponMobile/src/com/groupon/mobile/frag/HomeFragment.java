package com.groupon.mobile.frag;

import java.util.ArrayList;

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

	public HomeFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.activity_home, container, false);
		tasks = new ArrayList<Task>();
		setupListView(rootView);
		return rootView;
	}

	private void setupListView(View view) {
		listview = (ListView) view.findViewById(R.id.listview);
		TaskService taskService = new TaskService((GrouponApplication) getActivity().getApplication());

		arrayAdapter = new TaskAdapter(getActivity(), R.layout.listview_task, tasks);
		arrayAdapter.setFragmentManager(getFragmentManager());

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
	}
}