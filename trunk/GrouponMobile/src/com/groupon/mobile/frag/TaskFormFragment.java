package com.groupon.mobile.frag;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.groupon.mobile.GrouponApplication;
import com.groupon.mobile.R;
import com.groupon.mobile.conn.GrouponCallback;
import com.groupon.mobile.model.FieldAttribute;
import com.groupon.mobile.model.FieldType;
import com.groupon.mobile.model.NeedType;
import com.groupon.mobile.model.Task;
import com.groupon.mobile.model.TaskAttribute;
import com.groupon.mobile.model.TaskType;
import com.groupon.mobile.model.TaskTypeField;
import com.groupon.mobile.service.TaskService;
import com.groupon.mobile.service.TaskTypeService;

public class TaskFormFragment extends Fragment {
	private long taskTypeId;
	private TaskType taskType;
	private long communityId;
	private List<FieldType> fieldTypes;
	private EditText taskNameField;
	private EditText taskDescriptionField;
	private EditText taskDeadlineField;
	private EditText taskRequirementName;
	private EditText taskRequirementQuantity;
	private LinearLayout formFieldsLayout;

	private Button createTask;
	private View rootView;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fieldTypes = new ArrayList<FieldType>();
		taskTypeId = getArguments().getLong("taskTypeId");
		communityId = getArguments().getLong("communityId");
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_task_form, container, false);
		setTaskType();
		return rootView;
	}

	private void setupUI(View v) {
		Calendar c = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		String formattedDate = df.format(c.getTime());
		taskNameField = (EditText) v.findViewById(R.id.task_name);
		taskDescriptionField = (EditText) v.findViewById(R.id.task_description);
		taskDeadlineField = (EditText) v.findViewById(R.id.task_deadline);
		taskDeadlineField.setText(formattedDate);
		taskDeadlineField.setInputType(InputType.TYPE_CLASS_DATETIME);
		createTask = (Button) v.findViewById(R.id.button_task_create);
		createTask.setOnClickListener(createButtonClickListener);

		List<TaskTypeField> taskTypeFields = taskType.getFields();
		formFieldsLayout = (LinearLayout) v.findViewById(R.id.layout_form_fields);

		int marginTop = pxFromDp(10);

		for (TaskTypeField taskTypeField : taskTypeFields) {
			View fieldView = getFieldView(taskTypeField);
			if (fieldView != null) {
				formFieldsLayout.addView(fieldView);
				((LinearLayout.LayoutParams) fieldView.getLayoutParams()).setMargins(0, marginTop, 0, 0);
			}
		}

		if (taskType.getNeedType() == NeedType.GOODS) {
			View requirementNameView = createRequirementNameLayout(getActivity().getString(R.string.question_name_of_requirement));
			View requirementQuantityView = createRequirementQuantityLayout(getActivity().getString(R.string.question_quantity_of_requirement));
			formFieldsLayout.addView(requirementNameView);
			formFieldsLayout.addView(requirementQuantityView);

			((LinearLayout.LayoutParams) requirementNameView.getLayoutParams()).setMargins(0, marginTop, 0, 0);
			((LinearLayout.LayoutParams) requirementQuantityView.getLayoutParams()).setMargins(0, marginTop, 0, 0);
		} else if (taskType.getNeedType() == NeedType.SERVICE) {
			View requirementNameView = createRequirementNameLayout(getActivity().getString(R.string.question_name_of_service));
			formFieldsLayout.addView(requirementNameView);

			((LinearLayout.LayoutParams) requirementNameView.getLayoutParams()).setMargins(0, marginTop, 0, 0);
		}
	}

	private OnClickListener createButtonClickListener = new OnClickListener() {
		public void onClick(View v) {
			sendTask();
		}
	};

	private List<TaskAttribute> parseTaskAttributes() {
		List<TaskTypeField> taskTypeFields = taskType.getFields();
		List<TaskAttribute> taskAttributes = new ArrayList<TaskAttribute>();

		for (int i = 0; i < taskTypeFields.size(); i++) {
			TaskTypeField taskTypeField = taskTypeFields.get(i);
			FieldType fieldType = taskTypeField.getFieldType();
			View view = formFieldsLayout.getChildAt(i);
			convertViewToTaskAttribute(fieldType, view, taskAttributes);
		}

		return taskAttributes;
	}

	private void sendTask() {
		String name = taskNameField.getText().toString();
		String description = taskDescriptionField.getText().toString();
		String deadline = taskDeadlineField.getText().toString();

		Task task = new Task();
		task.setName(name);
		task.setDescription(description);
		task.setDeadline(deadline);
		if (taskType.getNeedType() == NeedType.GOODS) {

			String requirementName = taskRequirementName.getText().toString();
			task.setRequirementName(requirementName);
			int requirementQuantity = Integer.parseInt(taskRequirementQuantity.getText().toString());
			task.setRequirementQuantity(requirementQuantity);
		} else if (taskType.getNeedType() == NeedType.SERVICE) {
			String requirementName = taskRequirementName.getText().toString();
			task.setRequirementName(requirementName);
		}
		List<TaskAttribute> taskAttributes = parseTaskAttributes();
		task.setAttributes(taskAttributes);
		task.setNeedType(taskType.getNeedType().toString());
		TaskService taskService = new TaskService((GrouponApplication) getActivity().getApplication());
		taskService.createTask(task, communityId, new GrouponCallback<Task>() {
			public void onSuccess(Task task) {
				TaskFragment taskFragment = new TaskFragment();
				Bundle args = new Bundle();
				args.putLong("taskId", task.getId());
				taskFragment.setArguments(args);

				FragmentTransaction transaction = getFragmentManager().beginTransaction();
				transaction.replace(R.id.frame_container, taskFragment);
				transaction.commit();
			}

			public void onFail(String errorMessage) {

			}
		});
	}

	private View getFieldView(TaskTypeField taskTypeField) {
		FieldType f = taskTypeField.getFieldType();
		fieldTypes.add(f);

		switch (f) {
		case SHORT_TEXT:
			return createShortTextLayout(taskTypeField);
		case SELECT:
			return createSelectLayout(taskTypeField);
		case CHECKBOX:
			return createCheckboxesLayout(taskTypeField);
		default:
			return null;
		}
	}

	private View createShortTextLayout(TaskTypeField taskTypeField) {
		View editTextView = View.inflate(getActivity(), R.layout.custom_field_text, null);
		TextView hintView = (TextView) editTextView.findViewById(R.id.custom_field_text_title);
		hintView.setText(taskTypeField.getName());
		return editTextView;
	}

	private View createCheckboxesLayout(TaskTypeField taskTypeField) {
		View checkboxLayout = View.inflate(getActivity(), R.layout.custom_field_checkboxes, null);
		TextView hintView = (TextView) checkboxLayout.findViewById(R.id.custom_field_checkboxes_title);

		LinearLayout checkboxContainer = (LinearLayout) checkboxLayout.findViewById(R.id.checkboxes_container);

		for (FieldAttribute fieldAttribute : taskTypeField.getAttributes()) {
			CheckBox checkBox = (CheckBox) View.inflate(getActivity(), R.layout.custom_field_checkbox, null);
			checkBox.setText(fieldAttribute.getValue());
			checkboxContainer.addView(checkBox);
		}

		hintView.setText(taskTypeField.getName());
		return checkboxLayout;
	}

	private View createSelectLayout(TaskTypeField taskTypeField) {
		View selectLayout = View.inflate(getActivity(), R.layout.custom_field_select, null);
		TextView hintView = (TextView) selectLayout.findViewById(R.id.custom_field_select_title);
		Spinner spinner = (Spinner) selectLayout.findViewById(R.id.custom_field_select_spinner);

		List<String> options = new ArrayList<String>();
		for (FieldAttribute fieldAttribute : taskTypeField.getAttributes()) {
			options.add(fieldAttribute.getValue());
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, options);
		adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
		spinner.setAdapter(adapter);

		hintView.setText(taskTypeField.getName());
		return selectLayout;
	}

	private View createRequirementNameLayout(String name) {
		View editTextView = View.inflate(getActivity(), R.layout.custom_field_text, null);
		TextView hintView = (TextView) editTextView.findViewById(R.id.custom_field_text_title);
		taskRequirementName = (EditText) editTextView.findViewById(R.id.custom_field_text_input);
		hintView.setText(name);
		return editTextView;
	}

	private View createRequirementQuantityLayout(String name) {
		View editTextView = View.inflate(getActivity(), R.layout.custom_field_text, null);
		TextView hintView = (TextView) editTextView.findViewById(R.id.custom_field_text_title);
		taskRequirementQuantity = (EditText) editTextView.findViewById(R.id.custom_field_text_input);
		taskRequirementQuantity.setInputType(InputType.TYPE_CLASS_NUMBER);
		hintView.setText(name);
		return editTextView;
	}

	private void convertViewToTaskAttribute(FieldType fieldType, View view, List<TaskAttribute> taskAttributes) {
		switch (fieldType) {
		case SHORT_TEXT:
			taskAttributes.add(convertShortTextViewToTaskAttribute(view));
			break;
		case CHECKBOX:
			taskAttributes.addAll(convertCheckboxViewToTaskAttribute(view));
			break;
		case SELECT:
			taskAttributes.add(convertSelectViewToTaskAttribute(view));
			break;
		}
	}

	private TaskAttribute convertShortTextViewToTaskAttribute(View view) {
		TaskAttribute taskAttribute = new TaskAttribute();
		TextView textView = (TextView) view.findViewById(R.id.custom_field_text_title);
		EditText editText = (EditText) view.findViewById(R.id.custom_field_text_input);

		taskAttribute.setName(textView.getText().toString());
		taskAttribute.setValue(editText.getText().toString());
		return taskAttribute;
	}

	private TaskAttribute convertSelectViewToTaskAttribute(View view) {
		TaskAttribute taskAttribute = new TaskAttribute();
		TextView textView = (TextView) view.findViewById(R.id.custom_field_select_title);
		Spinner spinner = (Spinner) view.findViewById(R.id.custom_field_select_spinner);

		taskAttribute.setName(textView.getText().toString());
		taskAttribute.setValue(spinner.getSelectedItem().toString());
		return taskAttribute;
	}

	private List<TaskAttribute> convertCheckboxViewToTaskAttribute(View view) {
		List<TaskAttribute> taskAttributes = new ArrayList<TaskAttribute>();
		TextView textView = (TextView) view.findViewById(R.id.custom_field_checkboxes_title);
		String title = textView.getText().toString();

		LinearLayout checkboxContainer = (LinearLayout) view.findViewById(R.id.checkboxes_container);
		for (int i = 0; i < checkboxContainer.getChildCount(); i++) {
			CheckBox checkbox = (CheckBox) checkboxContainer.getChildAt(i);
			if (checkbox.isChecked()) {
				TaskAttribute taskAttribute = new TaskAttribute();
				taskAttribute.setName(title);
				taskAttribute.setValue(checkbox.getText().toString());
				taskAttributes.add(taskAttribute);
			}
		}

		return taskAttributes;
	}

	private void setTaskType() {
		TaskTypeService service = new TaskTypeService((GrouponApplication) getActivity().getApplication());
		service.getTaskType(taskTypeId, new GrouponCallback<TaskType>() {
			public void onFail(String errorMessage) {

			}

			@Override
			public void onSuccess(TaskType taskType) {
				TaskFormFragment.this.taskType = taskType;
				setupUI(rootView);
			}
		});
	}

	public void showDatePickerDialog(View v) {
		// DialogFragment newFragment = new DatePickerFragment();
		// newFragment.show(getChildFragmentManager(), "datePicker");
	}

	private int pxFromDp(float dp) {
		return (int) (dp * getActivity().getResources().getDisplayMetrics().density);
	}

}
