package com.groupon.mobile;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.groupon.mobile.model.Community;
import com.groupon.mobile.utils.ImageUtils;
/**
 * Custom adapter class for showing an item of community list item.
 * @author serkan
 *
 */
public class CommunityAdapter extends ArrayAdapter<Community> {

	int resource;
	String response;
	Context context;

	public CommunityAdapter(Context context, int resource, List<Community> items) {
		super(context, resource, items);
		this.resource = resource;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		LinearLayout communityView;

		Community community = getItem(position);

		if (convertView == null) {
			communityView = new LinearLayout(getContext());
			String inflater = Context.LAYOUT_INFLATER_SERVICE;
			LayoutInflater vi;
			vi = (LayoutInflater) getContext().getSystemService(inflater);
			vi.inflate(resource, communityView, true);
		} else {
			communityView = (LinearLayout) convertView;
		}
		//all view elements of a community list item is set.
		TextView communityName = (TextView) communityView.findViewById(R.id.community_name);
		TextView communityDescription = (TextView) communityView.findViewById(R.id.community_description);
		ImageView imageView = (ImageView) communityView.findViewById(R.id.community_picture);

		communityName.setText(community.getName());
		communityDescription.setText(community.getDescription());
		ImageUtils.loadBitmap(imageView, community.getPicture());

		return communityView;
	}
}