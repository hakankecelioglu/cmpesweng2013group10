package com.groupon.mobile.frag;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.groupon.mobile.GrouponApplication;
import com.groupon.mobile.R;
import com.groupon.mobile.conn.GrouponCallback;
import com.groupon.mobile.model.Community;
import com.groupon.mobile.service.CommunityService;

public class CreateCommunityFragment extends Fragment {

	private Button createButton;
	private EditText communityNameField;
	private EditText communityDescriptionField;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.activity_create_community, container, false);

		setupUI(rootView);

		return rootView;
	}

	private void setupUI(View rootView) {
		createButton = (Button) rootView.findViewById(R.id.button_create_community);
		createButton.setOnClickListener(createButtonClickListener);

		communityNameField = (EditText) rootView.findViewById(R.id.community_name);
		communityDescriptionField = (EditText) rootView.findViewById(R.id.community_description);
	}

	private OnClickListener createButtonClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			String name = communityNameField.getText().toString();
			String description = communityDescriptionField.getText().toString();

			if (name.trim().equals("")) {
				Toast.makeText(getActivity(), "Community name cannot be empty!", Toast.LENGTH_SHORT).show();
				return;
			}

			if (description.trim().equals("")) {
				Toast.makeText(getActivity(), "Community description cannot be empty!", Toast.LENGTH_SHORT).show();
				return;
			}

			Community community = new Community();
			community.setName(name);
			community.setDescription(description);

			CommunityService service = new CommunityService((GrouponApplication) getActivity().getApplication());
			service.createCommunity(community, new GrouponCallback<Community>() {
				public void onSuccess(Community community) {
					Long id = community.getId();

					Bundle bundle = new Bundle();
					bundle.putLong("communityId", id);

					Fragment fragment = new CommunityFragment();
					fragment.setArguments(bundle);

					FragmentTransaction transaction = getFragmentManager().beginTransaction();
					transaction.replace(R.id.frame_container, fragment);
					transaction.commit();
				}

				public void onFail(String errorMessage) {
					Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
				}
			});
		}
	};
}
