package com.groupon.mobile.frag;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.DialogFragment;
import android.app.FragmentManager;
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
import android.widget.Toast;

import com.groupon.mobile.GrouponApplication;
import com.groupon.mobile.R;
import com.groupon.mobile.conn.GrouponCallback;
import com.groupon.mobile.model.FieldAttribute;
import com.groupon.mobile.model.FieldType;
import com.groupon.mobile.model.Task;
import com.groupon.mobile.model.TaskAttribute;
import com.groupon.mobile.model.TaskTypeField;
import com.groupon.mobile.service.TaskService;
import com.groupon.mobile.utils.DateUtils;
/**
 * This is a dialog fragment class to let to user reply a task.
 * It creates reply form using reply fields previously created by task type creator 
 * @author serkan
 *
 */
public class ReplyFragment extends DialogFragment implements OnDateSetListener {
	private long taskId;
	private GrouponApplication app;
	private String needType;
	private String requirementName;
	private long requirementQuantity;
	private LinearLayout replyFieldsLayout;
	private Button replyButton;
	private List<FieldType> fieldTypes = new ArrayList<FieldType>();
	private List<TaskTypeField> replyFields;
	View rootView;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		app = (GrouponApplication) getActivity().getApplication();
		taskId = getArguments().getLong("taskId");
		needType = getArguments().getString("needType");
		if (needType.equals("GOODS") || needType.equals("SERVICE"))
			requirementName = getArguments().getString("requirementName");
		if (needType.equals("GOODS"))
			requirementQuantity = getArguments().getLong("requirementQuantity");

	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_reply, container, false);
		getDialog().setTitle("Reply form");
		replyButton = (Button) rootView.findViewById(R.id.button_reply_task);
		replyButton.setOnClickListener(replyButtonListener);
		getReplyFields();
		return rootView;
	}

	public void onAttach(Activity activity) {
		super.onAttach(activity);

	}
	/**
	 * setup UI of this fragment.
	 * @param rootView root view of this fragment.
	 */
	private void setupUI(View rootView) {
		replyFieldsLayout = (LinearLayout) rootView.findViewById(R.id.holder);
		int marginTop = pxFromDp(10);
		if (needType.equals("GOODS")) {

			View goodView = View.inflate(getActivity(), R.layout.custom_reply_good_line, null);
			TextView goodName = (TextView) goodView.findViewById(R.id.custom_field_text_title);
			goodName.setText(requirementName);
			replyFieldsLayout.addView(goodView);
			((LinearLayout.LayoutParams) goodView.getLayoutParams()).setMargins(0, marginTop, 0, 0);
		}

		if (replyFields != null) {

			for (TaskTypeField replyField : replyFields) {
				View fieldView = getFieldView(replyField);
				replyFieldsLayout.addView(fieldView);
				((LinearLayout.LayoutParams) fieldView.getLayoutParams()).setMargins(0, marginTop, 0, 0);
			}
		}
		if (replyFields == null && !needType.equals("GOODS")) {
			TextView noReply = new TextView(app.getApplicationContext());
			noReply.setText("There is nothing to reply in this task");
			replyFieldsLayout.addView(noReply);
		}
	}
	/**
	 * Returns a View of  .TaskTypeField
	 * @param replyField	TaskTypeField 
	 * @return view of TaskTypeField
	 */
	private View getFieldView(TaskTypeField replyField) {
		FieldType f = replyField.getFieldType();
		fieldTypes.add(f);

		switch (f) {
		case SHORT_TEXT:
			return createShortTextLayout(replyField, f);
		case SELECT:
			return createSelectLayout(replyField);
		case CHECKBOX:
			return createCheckboxesLayout(replyField);
		case INTEGER:
			return createShortTextLayout(replyField, f);
		case RADIO:
			return createRadioLayout(replyField);
		case FLOAT:
			return createShortTextLayout(replyField, f);
		case LONG_TEXT:
			return createLongTextLayout(replyField);
		case DATE:
			return createDateLayout(replyField);
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
		dateView.setOnClickListener(dateClickListener);
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
	private OnClickListener dateClickListener = new OnClickListener() {
		public void onClick(View v) {
			showDatePickerDialog(v);
		}
	};
	private EditText changed;
	public void showDatePickerDialog(View v) {
		changed = (EditText) v;
		FragmentManager fm = getFragmentManager();
		DialogFragment newFragment = new DatePickerFragment(ReplyFragment.this);
		newFragment.show(fm, "datePicker");
	}
	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR,year);
		c.set(Calendar.MONTH, monthOfYear);
		c.set(Calendar.DAY_OF_MONTH,dayOfMonth);
	   
	    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

	    changed.setText(sdf.format(c.getTime()));	
	}
	

	private OnClickListener replyButtonListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			reply();
			dismiss();

		}
	};
	/**
	 * Calls reply service function with task attributes list parsed.
	 */
	private void reply() {
		TaskService taskService = new TaskService(app);
		List<TaskAttribute> replyAttributes = parseTaskAttributes();
		taskService.replyTask(taskId, replyAttributes, new GrouponCallback<Task>() {

			@Override
			public void onSuccess(Task response) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onFail(String errorMessage) {
				// TODO Auto-generated method stub
				Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
			}
		});
	}
	/**
	 * set reply fields of the task to create initial form.
	 */
	private void getReplyFields() {
		TaskService taskService = new TaskService(app);
		taskService.getReplyFields(taskId, new GrouponCallback<List<TaskTypeField>>() {

			@Override
			public void onSuccess(List<TaskTypeField> replyFields) {
				ReplyFragment.this.replyFields = replyFields;
				setupUI(rootView);
			}

			@Override
			public void onFail(String errorMessage) {
				Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
			}
		});
	}
	/**
	 * Parses TaskAttribute list from user input
	 * @return	list of TaskAttribute s parsed
	 */
	private List<TaskAttribute> parseTaskAttributes() {
		List<TaskTypeField> taskTypeFields = replyFields;
		List<TaskAttribute> taskAttributes = new ArrayList<TaskAttribute>();
		int base = 0;
		if (needType.equals("GOODS")) {
			View view = replyFieldsLayout.getChildAt(0);
			taskAttributes.add(convertGoodViewToTaskAttribute(view));
			base = 1;
		}
		for (int i = 0; i < taskTypeFields.size(); i++) {
			TaskTypeField taskTypeField = taskTypeFields.get(i);
			FieldType fieldType = taskTypeField.getFieldType();
			View view = replyFieldsLayout.getChildAt(base + i);
			convertViewToTaskAttribute(fieldType, view, taskAttributes);
		}

		return taskAttributes;
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
	 * Convert a good - quantity pair to task attribute
	 * @param view	related view of input
	 * @return	task attribute converted from view
	 */
	private TaskAttribute convertGoodViewToTaskAttribute(View view) {
		TaskAttribute taskAttribute = new TaskAttribute();

		EditText editText = (EditText) view.findViewById(R.id.custom_field_text_input);
		taskAttribute.setName("TT_RES_QUANTITY");
		taskAttribute.setValue(editText.getText().toString());
		return taskAttribute;
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
	
	private int pxFromDp(float dp) {
		return (int) (dp * getActivity().getResources().getDisplayMetrics().density);
	}

}
