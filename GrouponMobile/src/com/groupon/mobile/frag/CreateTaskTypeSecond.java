package com.groupon.mobile.frag;

import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

import com.groupon.mobile.R;
import com.groupon.mobile.TaskTypeStepListener;
import com.groupon.mobile.TaskTypeStepUpdater;
import com.groupon.mobile.exception.GrouponException;
import com.groupon.mobile.model.TaskType;
import com.groupon.mobile.model.TaskTypeField;

public class CreateTaskTypeSecond extends DynamicFieldsFragment implements TaskTypeStepUpdater {
	private static final String fragmentTitle = "Enter the fields which will be filled by task creators";

	private TaskTypeStepListener listener;
	private TaskType taskType;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		setupDefaults(fragmentTitle, nextButtonClickListener, getString(R.string.next_button));
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void setTaskTypeStepListener(TaskTypeStepListener listener) {
		this.listener = listener;
	}

	@Override
	public void setTaskType(TaskType taskType) {
		this.taskType = taskType;
	}

	private OnClickListener nextButtonClickListener = new OnClickListener() {
		public void onClick(View v) {
			try {
				List<TaskTypeField> fields = CreateTaskTypeSecond.super.getTaskTypeFields();
				taskType.setFields(fields);
				listener.nextStep();
			} catch (GrouponException e) {
				Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
			}
		}
	};
}
