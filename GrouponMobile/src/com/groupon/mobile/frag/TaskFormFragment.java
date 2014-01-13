package com.groupon.mobile.frag;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.DatePickerDialog.OnDateSetListener;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.groupon.mobile.utils.DateUtils;
/**
 * This is a  fragment class to let to user  fill form fields of a task.
 * It creates form  interface using foorm fields previously created by task type creator 
 * @author serkan
 *
 */
public class TaskFormFragment extends Fragment implements OnDateSetListener {
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
	/**
	 * setup UI of this fragment.
	 * @param rootView
	 */
	private void setupUI(View v) {

		taskNameField = (EditText) v.findViewById(R.id.task_name);
		taskDescriptionField = (EditText) v.findViewById(R.id.task_description);
		taskDeadlineField = (EditText) v.findViewById(R.id.task_deadline);
		taskDeadlineField.setOnClickListener(deadlineClickListener);
		taskDeadlineField.setText(DateUtils.getCurrentDate());

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

	private OnClickListener deadlineClickListener = new OnClickListener() {
		public void onClick(View v) {
			showDatePickerDialog(v);
		}
	};
	private OnClickListener createButtonClickListener = new OnClickListener() {
		public void onClick(View v) {
			sendTask();
		}
	};
	/**
	 * Parses TaskAttribute list from user input
	 * @return	list of TaskAttribute s parsed
	 */
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
	/**
	 * Creates a Task instance from user input and calls send task service.
	 */
	private void sendTask() {
		String name = taskNameField.getText().toString();
		String description = taskDescriptionField.getText().toString();
		String deadline = taskDeadlineField.getText().toString();

		Task task = new Task();
		task.setName(name);
		task.setDescription(description);

		deadline = DateUtils.ddMMYYYYtoMMddYYYY(deadline);
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
		task.setTaskTypeId(taskTypeId);
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
	/**
	 * Returns a View of  .TaskTypeField
	 * @param replyField	TaskTypeField 
	 * @return view of TaskTypeField
	 */
	private View getFieldView(TaskTypeField taskTypeField) {
		FieldType f = taskTypeField.getFieldType();
		fieldTypes.add(f);

		switch (f) {
		case SHORT_TEXT:
			return createShortTextLayout(taskTypeField, f);
		case SELECT:
			return createSelectLayout(taskTypeField);
		case CHECKBOX:
			return createCheckboxesLayout(taskTypeField);
		case INTEGER:
			return createShortTextLayout(taskTypeField, f);
		case RADIO:
			return createRadioLayout(taskTypeField);
		case FLOAT:
			return createShortTextLayout(taskTypeField, f);
		case LONG_TEXT:
			return createLongTextLayout(taskTypeField);
		case DATE:
			return createDateLayout(taskTypeField);
		default:
			return null;
		}
	}
	/**
	 * Creates radio input layout by setting related  radio layouts from a TaskTypeField instance
	 * @param taskTypeField	a TaskTypeField represents reply field
	 * @return	 radio input view
	 */
	private View createRadioLayout(TaskTypeField taskTypeField) {
		View checkboxLayout = View.inflate(getActivity(), R.layout.custom_field_radios, null);
		TextView hintView = (TextView) checkboxLayout.findViewById(R.id.custom_field_radios_title);

		RadioGroup radioContainer = (RadioGroup) checkboxLayout.findViewById(R.id.radios_container);

		for (FieldAttribute fieldAttribute : taskTypeField.getAttributes()) {
			RadioButton checkBox = (RadioButton) View.inflate(getActivity(), R.layout.custom_field_radio, null);
			checkBox.setText(fieldAttribute.getValue());
			radioContainer.addView(checkBox);
		}

		hintView.setText(taskTypeField.getName());
		return checkboxLayout;
	}
	/**
	 * Creates long text layout from a TaskTypeField instance by setting related  layout parameters 
	 * @param taskTypeField represents reply field
	 * @return long text input view
	 */
	private View createLongTextLayout(TaskTypeField taskTypeField) {
		View editTextView = View.inflate(getActivity(), R.layout.custom_field_long_text, null);
		TextView hintView = (TextView) editTextView.findViewById(R.id.custom_field_text_title);
		hintView.setText(taskTypeField.getName());
		return editTextView;
	}
	/**
	 * Creates date input layout from a TaskTypeField instance by setting related  layout parameters 
	 * @param taskTypeField represents reply field
	 * @return date input view
	 */
	private View createDateLayout(TaskTypeField taskTypeField) {
		View editTextView = View.inflate(getActivity(), R.layout.custom_field_text, null);
		TextView hintView = (TextView) editTextView.findViewById(R.id.custom_field_text_title);
		hintView.setText(taskTypeField.getName());
		EditText dateView = (EditText) editTextView.findViewById(R.id.custom_field_text_input);
		dateView.setText(DateUtils.getCurrentDate());
		dateView.setOnClickListener(deadlineClickListener);
		return editTextView;
	}
	/**
	 * Creates short text input layout from a TaskTypeField instance by setting related  layout parameters 
	 * @param taskTypeField represents reply field
	 * @param f field type which determines if short text is in form of integer float or default 
	 * @return short text input view
	 */
	private View createShortTextLayout(TaskTypeField taskTypeField, FieldType f) {
		View editTextView = View.inflate(getActivity(), R.layout.custom_field_text, null);
		TextView hintView = (TextView) editTextView.findViewById(R.id.custom_field_text_title);
		hintView.setText(taskTypeField.getName());
		if (f != FieldType.SHORT_TEXT) {
			EditText integerView = (EditText) editTextView.findViewById(R.id.custom_field_text_input);
			if (f == FieldType.FLOAT)
				integerView.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
			else if (f == FieldType.INTEGER)
				integerView.setInputType(InputType.TYPE_CLASS_NUMBER);
		}
		return editTextView;
	}
	/**
	 * Creates checkbox layout from a TaskTypeField instance by setting related  layout parameters 
	 * @param taskTypeField represents reply field
	 * @return checkbox input view
	 */
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
	/**
	 * Creates select (spinner) layout from a TaskTypeField instance by setting related  layout parameters 
	 * @param taskTypeField represents reply field
	 * @return select input layout
	 */
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
	/**
	 * if need type is goods Creates requirement name layout from a TaskTypeField instance by setting related  layout parameters
	 * @param name	Label string of layout
	 * @return requirement name layout 
	 */
	private View createRequirementNameLayout(String name) {
		View editTextView = View.inflate(getActivity(), R.layout.custom_field_text, null);
		TextView hintView = (TextView) editTextView.findViewById(R.id.custom_field_text_title);
		taskRequirementName = (EditText) editTextView.findViewById(R.id.custom_field_text_input);
		hintView.setText(name);
		return editTextView;
	}
	/**
	 * if need type is goods Creates requirement quantity (as integer) layout from a TaskTypeField instance by setting related  layout parameters
	 * @param name	Label string of layout
	 * @return requirement quantity layout 
	 */
	private View createRequirementQuantityLayout(String name) {
		View editTextView = View.inflate(getActivity(), R.layout.custom_field_text, null);
		TextView hintView = (TextView) editTextView.findViewById(R.id.custom_field_text_title);
		taskRequirementQuantity = (EditText) editTextView.findViewById(R.id.custom_field_text_input);
		taskRequirementQuantity.setInputType(InputType.TYPE_CLASS_NUMBER);
		hintView.setText(name);
		return editTextView;
	}
	/**
	 * Convert a view to taskAttributes and add it to task attribute list
	 * @param fieldType	related field type of view
	 * @param view	view to be cnonverted
	 * @param taskAttributes converted view added to this.
	 */
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
		case DATE:
			taskAttributes.add(convertDateViewToTaskAttribute(view));
			break;
		case INTEGER:
			taskAttributes.add(convertIntegerViewToTaskAttribute(view));
			break;
		case RADIO:
			taskAttributes.add(convertRadioViewToTaskAttribute(view));
			break;
		case FLOAT:
			taskAttributes.add(convertFloatViewToTaskAttribute(view));
			break;
		case LONG_TEXT:
			taskAttributes.add(convertShortTextViewToTaskAttribute(view));
		}
	}
	/**
	 * Convert a date input to task Attribute
	 * @param view related view of input
	 * @return task attribute converted from view
	 */
	private TaskAttribute convertDateViewToTaskAttribute(View view) {
		TaskAttribute taskAttribute = new TaskAttribute();
		TextView textView = (TextView) view.findViewById(R.id.custom_field_text_title);
		EditText editText = (EditText) view.findViewById(R.id.custom_field_text_input);
		String date = editText.getText().toString();
		taskAttribute.setName(textView.getText().toString());
		taskAttribute.setValue(date);
		return taskAttribute;
	}
	/**
	 * Convert a float input to task Attribute
	 * @param view related view of input
	 * @return task attribute converted from view
	 */
	private TaskAttribute convertFloatViewToTaskAttribute(View view) {
		TaskAttribute taskAttribute = new TaskAttribute();
		TextView textView = (TextView) view.findViewById(R.id.custom_field_text_title);
		EditText editText = (EditText) view.findViewById(R.id.custom_field_text_input);

		taskAttribute.setName(textView.getText().toString());
		taskAttribute.setValue(editText.getText().toString());
		return taskAttribute;
	}
	/**
	 * Convert a integer input to task Attribute
	 * @param view related view of input
	 * @return task attribute converted from view
	 */
	private TaskAttribute convertIntegerViewToTaskAttribute(View view) {
		TaskAttribute taskAttribute = new TaskAttribute();
		TextView textView = (TextView) view.findViewById(R.id.custom_field_text_title);
		EditText editText = (EditText) view.findViewById(R.id.custom_field_text_input);

		taskAttribute.setName(textView.getText().toString());
		taskAttribute.setValue(editText.getText().toString());
		return taskAttribute;
	}
	/**
	 * Convert a short text input to task Attribute
	 * @param view related view of input
	 * @return task attribute converted from view
	 */
	private TaskAttribute convertShortTextViewToTaskAttribute(View view) {
		TaskAttribute taskAttribute = new TaskAttribute();
		TextView textView = (TextView) view.findViewById(R.id.custom_field_text_title);
		EditText editText = (EditText) view.findViewById(R.id.custom_field_text_input);

		taskAttribute.setName(textView.getText().toString());
		taskAttribute.setValue(editText.getText().toString());
		return taskAttribute;
	}
	/**
	 * Convert a select  input to task Attribute
	 * @param view related view of input
	 * @return task attribute converted from view
	 */
	private TaskAttribute convertSelectViewToTaskAttribute(View view) {
		TaskAttribute taskAttribute = new TaskAttribute();
		TextView textView = (TextView) view.findViewById(R.id.custom_field_select_title);
		Spinner spinner = (Spinner) view.findViewById(R.id.custom_field_select_spinner);

		taskAttribute.setName(textView.getText().toString());
		taskAttribute.setValue(spinner.getSelectedItem().toString());
		return taskAttribute;
	}
	/**
	 * Convert a checkbox input to list of task Attributes
	 * @param view related view of input
	 * @return task attribute list converted from view
	 */
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
	/**
	 * Convert chosen radio input to task Attribute
	 * @param view related view of input
	 * @return task attribute converted from view
	 */
	private TaskAttribute convertRadioViewToTaskAttribute(View view) {
		TaskAttribute taskAttribute = new TaskAttribute();
		
		TextView textView = (TextView) view.findViewById(R.id.custom_field_radios_title);
		String title = textView.getText().toString();
		taskAttribute.setName(title);
		RadioGroup radioContainer = (RadioGroup) view.findViewById(R.id.radios_container);
		if (radioContainer.getCheckedRadioButtonId() != -1) {
			int id = radioContainer.getCheckedRadioButtonId();
			View radioButton = radioContainer.findViewById(id);
			int radioId = radioContainer.indexOfChild(radioButton);
			RadioButton btn = (RadioButton) radioContainer.getChildAt(radioId);
			String selection = (String) btn.getText();
			taskAttribute.setValue(selection);
		}
		
		return taskAttribute;
	}
	/**
	 * Called when fragment is created on set task type of the task to setup UI.
	 */
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

	private EditText changed;

	public void showDatePickerDialog(View v) {
		changed = (EditText) v;
		FragmentManager fm = getFragmentManager();
		DialogFragment newFragment = new DatePickerFragment(TaskFormFragment.this);
		newFragment.show(fm, "datePicker");
	}

	private int pxFromDp(float dp) {
		return (int) (dp * getActivity().getResources().getDisplayMetrics().density);
	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, monthOfYear);
		c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

		changed.setText(sdf.format(c.getTime()));
	}

}
