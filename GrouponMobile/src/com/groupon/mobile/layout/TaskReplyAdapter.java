package com.groupon.mobile.layout;

import java.util.List;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.groupon.mobile.GrouponApplication;
import com.groupon.mobile.R;
import com.groupon.mobile.conn.GrouponCallback;
import com.groupon.mobile.frag.CommunityFragment;
import com.groupon.mobile.frag.ProfileFragment;
import com.groupon.mobile.frag.TaskFragment;
import com.groupon.mobile.model.FieldAttribute;
import com.groupon.mobile.model.Task;
import com.groupon.mobile.model.TaskReply;
import com.groupon.mobile.service.TaskService;

public class TaskReplyAdapter extends ArrayAdapter<TaskReply> {
	private int resource;
	private FragmentManager fragmentManager;
	private GrouponApplication app;

	public TaskReplyAdapter(GrouponApplication app, Context context, int resource, List<TaskReply> items) {
		super(context, resource, items);
		this.resource = resource;
		this.app = app;
	}

	public void setFragmentManager(FragmentManager fm) {
		this.fragmentManager = fm;
	}

	public View getView(int position, View cView, ViewGroup parent) {
		LinearLayout view;
		TaskReply taskReply = getItem(position);

		if (cView == null) {
			view = new LinearLayout(getContext());
			String inflater = Context.LAYOUT_INFLATER_SERVICE;
			LayoutInflater vi;
			vi = (LayoutInflater) getContext().getSystemService(inflater);
			vi.inflate(resource, view, true);
		} else {
			view = (LinearLayout) cView;
		}

		TextView replierName = (TextView) view.findViewById(R.id.replier_name);
		TextView replyDate = (TextView) view.findViewById(R.id.reply_date);

		replierName.setText(taskReply.getUser().getUsername());
		CharSequence seq = android.text.format.DateFormat.format("dd.MM.yyyy hh:mm:ss", taskReply.getDate());
		replyDate.setText(seq);

		LinearLayout attrArea = (LinearLayout) view.findViewById(R.id.reply_attributes);
		attrArea.removeAllViews();
		List<FieldAttribute> attributes = taskReply.getAttributes();
		if (attributes != null && attributes.size() > 0) {
			for (FieldAttribute attribute : attributes) {
				TextView textView = new TextView(getContext());

				String attrName = attribute.getName();
				if (attrName.equals("TT_RES_QUANTITY")) {
					attrName = "Amount: ";
				} else {
					attrName += ":\n";
				}

				textView.setText(attrName + attribute.getValue());
				attrArea.addView(textView);
			}
		}

		return view;
	}

	private void convertFollowButtonToUnfollow(View view) {
		Button followButton = (Button) view.findViewById(R.id.follow_button);
		Button unfollowButton = (Button) view.findViewById(R.id.unfollow_button);

		followButton.setVisibility(View.GONE);
		unfollowButton.setVisibility(View.VISIBLE);
	}

	private void convertUnfollowButtonToFollow(View view) {
		Button followButton = (Button) view.findViewById(R.id.follow_button);
		Button unfollowButton = (Button) view.findViewById(R.id.unfollow_button);

		followButton.setVisibility(View.VISIBLE);
		unfollowButton.setVisibility(View.GONE);
	}

	private View.OnClickListener taskNameClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			Long taskId = (Long) v.getTag();

			// Allow no NullPointerException!
			if (fragmentManager == null)
				return;

			Bundle bundle = new Bundle();
			bundle.putLong("taskId", taskId);

			Fragment fragment = new TaskFragment();
			fragment.setArguments(bundle);

			FragmentTransaction transaction = fragmentManager.beginTransaction();
			transaction.addToBackStack(null);
			transaction.replace(R.id.frame_container, fragment);
			transaction.commit();
		}
	};

	private View.OnClickListener communityNameClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			// Allow no NullPointerException!
			if (fragmentManager == null)
				return;

			Long communityId = (Long) v.getTag();

			Bundle bundle = new Bundle();
			bundle.putLong("communityId", communityId);

			Fragment fragment = new CommunityFragment();
			fragment.setArguments(bundle);

			FragmentTransaction transaction = fragmentManager.beginTransaction();
			transaction.addToBackStack(null);
			transaction.replace(R.id.frame_container, fragment);
			transaction.commit();
		}
	};

	private View.OnClickListener userNameClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			// Allow no NullPointerException!
			if (fragmentManager == null)
				return;

			Long userId = (Long) v.getTag();

			Bundle bundle = new Bundle();
			bundle.putLong("userId", userId);

			Fragment fragment = new ProfileFragment();
			fragment.setArguments(bundle);

			FragmentTransaction transaction = fragmentManager.beginTransaction();
			transaction.addToBackStack(null);
			transaction.replace(R.id.frame_container, fragment);
			transaction.commit();
		}
	};

	private View.OnClickListener followClickListener = new View.OnClickListener() {
		public void onClick(final View v) {
			Long taskId = (Long) v.getTag();

			TaskService taskService = new TaskService(app);
			taskService.followTask(taskId, new GrouponCallback<Task>() {
				public void onSuccess(Task response) {
					convertFollowButtonToUnfollow((View) v.getParent());
				}

				public void onFail(String errorMessage) {

				}
			});
		}
	};

	private View.OnClickListener unfollowClickListener = new View.OnClickListener() {
		public void onClick(final View v) {
			Long taskId = (Long) v.getTag();

			TaskService taskService = new TaskService(app);
			taskService.unFollowTask(taskId, new GrouponCallback<Task>() {
				public void onSuccess(Task response) {
					convertUnfollowButtonToFollow((View) v.getParent());
				}

				public void onFail(String errorMessage) {

				}
			});
		}
	};
}
