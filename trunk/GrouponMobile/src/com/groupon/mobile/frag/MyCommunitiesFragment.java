package com.groupon.mobile.frag;

import java.util.ArrayList;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.groupon.mobile.CommunityAdapter;
import com.groupon.mobile.GrouponApplication;
import com.groupon.mobile.R;
import com.groupon.mobile.conn.GrouponCallback;
import com.groupon.mobile.model.Community;
import com.groupon.mobile.service.CommunityService;

public class MyCommunitiesFragment extends Fragment {

	private CommunityAdapter arrayAdapter;
	private ArrayList<Community> communities = new ArrayList<Community>();
	private ListView listview;
	private boolean isAll;
	private boolean isOpen = false;

	private GrouponApplication app;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.activity_my_communities, container, false);
		app = (GrouponApplication) getActivity().getApplication();

		Bundle bundle = getArguments();
		isAll = bundle == null ? false : bundle.getBoolean("all", false);

		setupUI(rootView);
		isOpen = true;

		return rootView;
	}

	private void setupUI(View rootView) {
		setupListView(rootView);
		listview.setOnItemClickListener(listViewListener);
	}

	private void setupListView(View rootView) {
		listview = (ListView) rootView.findViewById(R.id.listview);
		arrayAdapter = new CommunityAdapter(getActivity(), R.layout.listview_community, communities);
		listview.setAdapter(arrayAdapter);

		if (isOpen) {
			arrayAdapter.notifyDataSetChanged();
		} else {
			CommunityService communityService = new CommunityService(app);
			communityService.getCommunities(isAll, new GrouponCallback<ArrayList<Community>>() {
				public void onSuccess(ArrayList<Community> response) {
					for (Community c : response) {
						communities.add(c);
					}

					arrayAdapter.notifyDataSetChanged();
				}

				public void onFail(String errorMessage) {
					Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
				}
			});
		}
	}

	private OnItemClickListener listViewListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			if (position >= 0 && position < communities.size()) {
				Community community = communities.get(position);

				Bundle bundle = new Bundle();
				bundle.putLong("communityId", community.getId());

				Fragment fragment = new CommunityFragment();
				fragment.setArguments(bundle);

				FragmentTransaction transaction = getFragmentManager().beginTransaction();
				transaction.addToBackStack(null);
				transaction.replace(R.id.frame_container, fragment);
				transaction.commit();
			}
		}
	};

}
