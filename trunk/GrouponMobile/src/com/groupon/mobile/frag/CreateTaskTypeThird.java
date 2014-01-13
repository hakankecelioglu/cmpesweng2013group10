package com.groupon.mobile.frag;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

import com.groupon.mobile.CreateTaskTypeFragmentActivity;
import com.groupon.mobile.R;
import com.groupon.mobile.exception.GrouponException;
import com.groupon.mobile.model.TaskType;
import com.groupon.mobile.model.TaskTypeField;
/**
 * This is the final step for task type creation. User enters reply fields here and touchs save button
 * to save his task type.
 * @author sedrik
 *
 */
public class CreateTaskTypeThird extends DynamicFieldsFragment {
	private static final String fragmentTitle = "Enter the fields which will be filled by task repliers";

	private CreateTaskTypeFragmentActivity mActivity;
	private TaskType taskType;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		setupDefaults(fragmentTitle, saveButtonClickListener, getString(R.string.create_task_type_button));
		View rootView = super.onCreateView(inflater, container, savedInstanceState);
		if (taskType != null && taskType.getFields() != null) {
			setSelectedTaskTypeFields(taskType.getFields());
		}
		return rootView;
	}


	private OnClickListener saveButtonClickListener = new OnClickListener() {
		public void onClick(View v) {
			try {
				List<TaskTypeField> taskTypeFields = getTaskTypeFields();
				taskType.setReplyFields(taskTypeFields);
				mActivity.createTaskTypeAndFinishActivity();
			} catch (GrouponException e) {
				Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
			}
		}
	};

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		CreateTaskTypeFragmentActivity act = (CreateTaskTypeFragmentActivity) activity;
		this.mActivity = act;
		this.taskType = act.getTaskTypeInstance();
	};
}
