package com.groupon.mobile.frag;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.groupon.mobile.CommunityTasksActivity;
import com.groupon.mobile.CreateTaskActivity;
import com.groupon.mobile.CreateTaskTypeActivity;
import com.groupon.mobile.GrouponApplication;
import com.groupon.mobile.R;
import com.groupon.mobile.conn.GrouponCallback;
import com.groupon.mobile.model.Community;
import com.groupon.mobile.service.CommunityService;
import com.groupon.mobile.utils.ImageUtils;

public class CommunityFragment extends Fragment {

	private Button createButton;
	private Button createTypeButton;
	private Button joinCommunityButton;
	private TextView communityNameField;
	private TextView communityDescriptionField;
	private Button taskButton;
	private long communityId;
	private ImageView communityPicture;

	private GrouponApplication app;

	public CommunityFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.activity_community, container, false);
		app = (GrouponApplication) getActivity().getApplication();

		Bundle bundle = getArguments();
		Long communityId = bundle.getLong("communityId");

		CommunityService service = new CommunityService(app);
		service.getCommunity(communityId, new GrouponCallback<Community>() {
			public void onSuccess(Community community) {
				setupUI(rootView, community.getName(), community.getDescription(), community.getPicture());
			}

			public void onFail(String errorMessage) {
				Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
			}
		});

		return rootView;
	}

	private void setupUI(View rootView, String name, String description, String pictureUrl) {
		createTypeButton = (Button) rootView.findViewById(R.id.button_create_task_type);
		createTypeButton.setOnClickListener(createTypeButtonClickListener);
		taskButton = (Button) rootView.findViewById(R.id.button_tasks_community);

		taskButton.setOnClickListener(tasksListener);
		joinCommunityButton = (Button) rootView.findViewById(R.id.button_join_community);
		joinCommunityButton.setOnClickListener(joinCommunityListener);
		createButton = (Button) rootView.findViewById(R.id.button_create_task);
		createButton.setOnClickListener(createButtonClickListener);
		communityNameField = (TextView) rootView.findViewById(R.id.communityName);
		communityNameField.setText(name);
		communityDescriptionField = (TextView) rootView.findViewById(R.id.communityDescription);
		communityDescriptionField.setText(description);
		communityPicture = (ImageView) rootView.findViewById(R.id.community_picture);
		ImageUtils.loadBitmap(communityPicture, pictureUrl);
	}

	private OnClickListener tasksListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(getActivity(), CommunityTasksActivity.class);
			intent.putExtra("communityId", communityId);
			startActivity(intent);

		}
	};
	private OnClickListener createButtonClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {

			Intent intent = new Intent(getActivity(), CreateTaskActivity.class);
			intent.putExtra("communityId", communityId);
			startActivity(intent);

		}
	};
	private OnClickListener createTypeButtonClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {

			Intent intent = new Intent(getActivity(), CreateTaskTypeActivity.class);
			intent.putExtra("communityId", communityId);
			startActivity(intent);
		}
	};
	private OnClickListener joinCommunityListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			CommunityService communityService = new CommunityService(app);
			communityService.joinCommunity(communityId, new GrouponCallback<Community>() {

				@Override
				public void onSuccess(Community response) {

					joinCommunityButton.setText("Leave");
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
			communityService.leaveCommunity(communityId, new GrouponCallback<Community>() {

				@Override
				public void onSuccess(Community response) {

					joinCommunityButton.setText("Join");
					joinCommunityButton.setOnClickListener(joinCommunityListener);

				}

				@Override
				public void onFail(String errorMessage) {

				}
			});
		}
	};
}
