package com.groupon.mobile;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.groupon.mobile.model.Community;

public class CommunityAdapter extends ArrayAdapter<Community> {

	int resource;
	String response;
	Context context;

	public CommunityAdapter(Context context, int resource, List<Community> items) {
		super(context, resource, items);
		this.resource = resource;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		LinearLayout alertView;

		Community community = getItem(position);

		if (convertView == null) {
			alertView = new LinearLayout(getContext());
			String inflater = Context.LAYOUT_INFLATER_SERVICE;
			LayoutInflater vi;
			vi = (LayoutInflater) getContext().getSystemService(inflater);
			vi.inflate(resource, alertView, true);
		} else {
			alertView = (LinearLayout) convertView;
		}

		TextView communityName = (TextView) alertView.findViewById(R.id.community_name);
		TextView communityDescription = (TextView) alertView.findViewById(R.id.community_description);

		communityName.setText(community.getName());
		communityDescription.setText(community.getDescription());

		return alertView;
	}
}