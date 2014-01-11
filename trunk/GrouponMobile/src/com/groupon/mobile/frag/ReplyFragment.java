package com.groupon.mobile.frag;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
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
import com.groupon.mobile.model.Task;
import com.groupon.mobile.model.TaskAttribute;
import com.groupon.mobile.model.TaskTypeField;
import com.groupon.mobile.service.TaskService;

public class ReplyFragment extends DialogFragment {
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

	private View getFieldView(TaskTypeField replyField) {
		FieldType f = replyField.getFieldType();
		fieldTypes.add(f);

		switch (f) {
		case SHORT_TEXT:
			return createShortTextLayout(replyField);
		case SELECT:
			return createSelectLayout(replyField);
		case CHECKBOX:
			return createCheckboxesLayout(replyField);
		default:
			return null;
		}

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

	private View createShortTextLayout(TaskTypeField taskTypeField) {
		View editTextView = View.inflate(getActivity(), R.layout.custom_field_text, null);
		TextView hintView = (TextView) editTextView.findViewById(R.id.custom_field_text_title);
		hintView.setText(taskTypeField.getName());
		return editTextView;
	}

	private OnClickListener replyButtonListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			reply();
			dismiss();

		}
	};

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

			}
		});
	}

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

			}
		});
	}

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

	private TaskAttribute convertGoodViewToTaskAttribute(View view) {
		TaskAttribute taskAttribute = new TaskAttribute();

		EditText editText = (EditText) view.findViewById(R.id.custom_field_text_input);
		taskAttribute.setName("TT_RES_QUANTITY");
		taskAttribute.setValue(editText.getText().toString());
		return taskAttribute;
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

	private int pxFromDp(float dp) {
		return (int) (dp * getActivity().getResources().getDisplayMetrics().density);
	}

}
