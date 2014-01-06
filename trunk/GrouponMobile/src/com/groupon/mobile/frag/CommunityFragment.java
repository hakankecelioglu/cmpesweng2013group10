package com.groupon.mobile.frag;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.groupon.mobile.CreateTaskActivity;
import com.groupon.mobile.CreateTaskTypeActivity;
import com.groupon.mobile.GrouponApplication;
import com.groupon.mobile.R;
import com.groupon.mobile.conn.GrouponCallback;
import com.groupon.mobile.layout.TaskAdapter;
import com.groupon.mobile.model.Community;
import com.groupon.mobile.model.Task;
import com.groupon.mobile.service.CommunityService;
import com.groupon.mobile.service.TaskService;
import com.groupon.mobile.utils.ImageUtils;

public class CommunityFragment extends Fragment {
	private ListView listview;
	private TaskAdapter arrayAdapter;
	private ArrayList<Task> tasks = new ArrayList<Task>();
	private Activity activity;

	private TextView communityNameField;
	private TextView communityDescriptionField;

	private Button createTaskButton;
	private Button createTaskTypeButton;
	private Button joinCommunityButton;
	private ImageView communityPicture;

	private GrouponApplication app;
	private Community community;

	public CommunityFragment() {
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.activity_community, container, false);
		app = (GrouponApplication) getActivity().getApplication();

		Bundle bundle = getArguments();
		Long communityId = bundle.getLong("communityId");

		setupUI(rootView);

		if (this.community != null) {
			fillCommunityFields();
		} else {
			CommunityService service = new CommunityService(app);
			service.getCommunity(communityId, onCommunityResponse);

			TaskService taskService = new TaskService(app);
			taskService.getCommunityTasks(communityId, onCommunityTasksResponse);
		}

		return rootView;
	}

	private void setupUI(View view) {
		// for root layout
		listview = (ListView) view.findViewById(R.id.listview);

		// from header layout
		View listHeader = View.inflate(getActivity(), R.layout.community_header, null);

		communityNameField = (TextView) listHeader.findViewById(R.id.communityName);
		communityDescriptionField = (TextView) listHeader.findViewById(R.id.communityDescription);
		communityPicture = (ImageView) listHeader.findViewById(R.id.community_picture);

		createTaskTypeButton = (Button) listHeader.findViewById(R.id.button_create_task_type);
		joinCommunityButton = (Button) listHeader.findViewById(R.id.button_join_community);
		createTaskButton = (Button) listHeader.findViewById(R.id.button_create_task);

		arrayAdapter = new TaskAdapter(app, getActivity(), R.layout.listview_task, tasks);
		arrayAdapter.setCommunityNameClickable(false);
		arrayAdapter.setFragmentManager(getFragmentManager());

		listview.addHeaderView(listHeader);
		listview.setAdapter(arrayAdapter);
	}

	private void fillCommunityFields() {
		if (community == null)
			return;

		communityNameField.setText(community.getName());
		communityDescriptionField.setText(community.getDescription());
		ImageUtils.loadBitmap(communityPicture, community.getPicture());

		createTaskTypeButton.setOnClickListener(createTypeButtonClickListener);
		joinCommunityButton.setOnClickListener(joinCommunityListener);
		createTaskButton.setOnClickListener(createButtonClickListener);
	}

	private OnClickListener createButtonClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {

			Intent intent = new Intent(getActivity(), CreateTaskActivity.class);
			intent.putExtra("communityId", community.getId());
			startActivity(intent);

		}
	};
	private OnClickListener createTypeButtonClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {

			Intent intent = new Intent(getActivity(), CreateTaskTypeActivity.class);
			intent.putExtra("communityId", community.getId());
			startActivity(intent);
		}
	};
	private OnClickListener joinCommunityListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			CommunityService communityService = new CommunityService(app);
			communityService.joinCommunity(community.getId(), new GrouponCallback<Community>() {

				@Override
				public void onSuccess(Community response) {
					joinCommunityButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.leave_icon, 0, 0);
					joinCommunityButton.setText(getString(R.string.leave_community));
					joinCommunityButton.setOnClickListener(leaveCommunityListener);
				}

				@Override
				public void onFail(String errorMessage) {

				}
			});
		}
	};
	private OnClickListener leaveCommunityListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			CommunityService communityService = new CommunityService(app);
			communityService.leaveCommunity(community.getId(), new GrouponCallback<Community>() {

				@Override
				public void onSuccess(Community response) {
					joinCommunityButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.join_community_icon, 0, 0);
					joinCommunityButton.setText(getString(R.string.join_community));
					joinCommunityButton.setOnClickListener(joinCommunityListener);
				}

				@Override
				public void onFail(String errorMessage) {

				}
			});
		}
	};

	private GrouponCallback<ArrayList<Task>> onCommunityTasksResponse = new GrouponCallback<ArrayList<Task>>() {
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

	private GrouponCallback<Community> onCommunityResponse = new GrouponCallback<Community>() {
		public void onSuccess(Community community) {
			CommunityFragment.this.community = community;
			fillCommunityFields();
		}

		public void onFail(String errorMessage) {
			Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
		}
	};
}
