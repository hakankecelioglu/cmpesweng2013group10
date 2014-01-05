package com.groupon.mobile.frag;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.groupon.mobile.GrouponApplication;
import com.groupon.mobile.R;
import com.groupon.mobile.conn.GrouponCallback;
import com.groupon.mobile.model.TaskType;
import com.groupon.mobile.service.TaskTypeService;

public class TaskTypeFragment extends Fragment {

	OnTaskTypeSelectedListener mCallback;
	Button taskTypeSelect;
	Spinner taskTypeSpinner;
	private long communityId;
	ArrayList<String> taskTypeNames;
	ArrayList<Long> taskTypeIds;

	public interface OnTaskTypeSelectedListener {
		public void onTaskTypeSelected(long position);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_task_type, container, false);
		setupUI(v);
		return v;
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		taskTypeNames = new ArrayList<String>();
		taskTypeIds = new ArrayList<Long>();
		communityId = getArguments().getLong("communityId");

	}

	private void setupUI(View v) {
		taskTypeSelect = (Button) v.findViewById(R.id.task_type_select);
		taskTypeSelect.setOnClickListener(taskTypeSelectListener);
		taskTypeSpinner = (Spinner) v.findViewById(R.id.task_type_spinner);

		fillSpinner();
	}

	private void fillSpinner() {
		TaskTypeService service = new TaskTypeService((GrouponApplication) getActivity().getApplication());
		service.getTaskTypes(communityId, new GrouponCallback<ArrayList<TaskType>>() {

			public void onFail(String errorMessage) {

			}

			@Override
			public void onSuccess(ArrayList<TaskType> taskTypesArray) {
				List<String> taskTypeNames = new ArrayList<String>();
				for (TaskType taskType : taskTypesArray) {

					taskTypeNames.add(taskType.getName());
					taskTypeIds.add(taskType.getId());

				}
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, taskTypeNames);
				taskTypeSpinner.setAdapter(adapter);

			}
		});
	}

	private OnClickListener taskTypeSelectListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

			mCallback.onTaskTypeSelected(taskTypeIds.get(taskTypeSpinner.getSelectedItemPosition()));

		}
	};

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// This makes sure that the container activity has implemented
		// the callback interface. If not, it throws an exception
		try {
			mCallback = (OnTaskTypeSelectedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement OnHeadlineSelectedListener");
		}
	}
}
