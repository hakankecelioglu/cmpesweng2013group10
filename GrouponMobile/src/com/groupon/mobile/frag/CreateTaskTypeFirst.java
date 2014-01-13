package com.groupon.mobile.frag;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.groupon.mobile.CreateTaskTypeFragmentActivity;
import com.groupon.mobile.R;
import com.groupon.mobile.model.NeedType;
import com.groupon.mobile.model.TaskType;
/**
 * First fragment of the create task type activity. Task type name need type and task description is 
 * inputted here. A next button is provided to the second fragment which inputs form fields.
 * @author sedrik
 *
 */
public class CreateTaskTypeFirst extends Fragment {
	private EditText taskNameField;
	private EditText taskDescriptionField;

	private Button nextButton;
	private NeedType needType;
	private Spinner needTypeSpinner;

	private CreateTaskTypeFragmentActivity mActivity;
	private TaskType taskType;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_create_task_type_first, container, false);
		setupUI(rootView);
		return rootView;
	}
	/**
	 * setup UI of this fragment.
	 * @param rootView root view of this fragment.
	 */
	private void setupUI(View rootView) {
		nextButton = (Button) rootView.findViewById(R.id.button_create);
		nextButton.setOnClickListener(nextButtonClickListener);
		taskNameField = (EditText) rootView.findViewById(R.id.task_type_name);
		needTypeSpinner = (Spinner) rootView.findViewById(R.id.need_type);
		needTypeSpinner.setOnItemSelectedListener(needTypeSpinnerListener);
		taskDescriptionField = (EditText) rootView.findViewById(R.id.task_type_description);
	}

	private OnItemSelectedListener needTypeSpinnerListener = new OnItemSelectedListener() {
		public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
			if (pos == 0) {
				needType = NeedType.GOODS;
			} else if (pos == 1) {
				needType = NeedType.SERVICE;
			} else {
				needType = NeedType.ONLY_FORM;
			}
		}

		public void onNothingSelected(AdapterView<?> arg0) {
		}
	};
	/**
	 * Sets name description and need type on TaskType and move view into next fragment.
	 */
	private OnClickListener nextButtonClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			String name = taskNameField.getText().toString();
			String description = taskDescriptionField.getText().toString();

			if (name.trim().equals("")) {
				Toast.makeText(getActivity(), "Task type name cannot be empty!", Toast.LENGTH_SHORT).show();
				return;
			}

			if (description.trim().equals("")) {
				Toast.makeText(getActivity(), "Task type description cannot be empty!", Toast.LENGTH_SHORT).show();
				return;
			}

			taskType.setName(name);
			taskType.setDescription(description);
			taskType.setNeedType(needType);

			mActivity.openSecondStep();
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
