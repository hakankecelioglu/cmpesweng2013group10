package com.groupon.mobile.frag;

import java.util.ArrayList;
import java.util.List;

import com.groupon.mobile.GrouponApplication;
import com.groupon.mobile.R;

import com.groupon.mobile.model.FieldAttribute;
import com.groupon.mobile.model.FieldType;
import com.groupon.mobile.model.NeedType;
import com.groupon.mobile.model.TaskType;
import com.groupon.mobile.model.TaskTypeField;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;


public class DynamicFieldsFragment extends DialogFragment {
	private GrouponApplication app;
	private Spinner fieldTypeSpinner;
	private ArrayAdapter<String> fieldTypeAdapter;

	private String description;
	private Button addFormButton;
	private Button saveButton;
	private long communityId;
	private List<FieldType> fieldTypes;
	private View rootView;
	private TextView TextViewDescription;
	private LinearLayout formFieldsLayout;
	public DynamicFieldsFragment(String description){
		this.description = description;
	}
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fieldTypes = new ArrayList<FieldType>();
		app = (GrouponApplication) getActivity().getApplication();
		communityId = getArguments().getLong("communityId");
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_form_fields, container, false);
		setupUI(rootView);
		return rootView;
	}
	public void onAttach(Activity activity) {
		super.onAttach(activity);



	}
	private void setupUI(View view){
		TextViewDescription = (TextView) view.findViewById(R.id.description);
		TextViewDescription.setText(description);
		addFormButton = (Button) view.findViewById(R.id.button_add_form_field);
		saveButton = (Button) view.findViewById(R.id.button_save);
		fieldTypeSpinner = (Spinner) view.findViewById(R.id.form_field_type);

		formFieldsLayout = (LinearLayout) view.findViewById(R.id.layout_form_fields);
		addFormButton.setOnClickListener(addFormButtonClickListener);
		saveButton.setOnClickListener(saveButtonClickListener);

		formFieldsLayout.setOrientation(LinearLayout.VERTICAL);
		
		fieldTypeAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, FieldType.getFieldTypes());
		fieldTypeSpinner.setAdapter(fieldTypeAdapter);		
	}
	public OnClickListener saveButtonClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			dismiss();

		}
		
	};
	private OnClickListener addFormButtonClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			EditText questionTitle = new EditText(app.getApplicationContext());
			questionTitle.setHint("Question Title");

			TextView questionTitleLabel = new TextView(app.getApplicationContext());
			questionTitleLabel.setText("Question Title");

			LinearLayout layout = new LinearLayout(app.getApplicationContext());
			layout.setOrientation(LinearLayout.VERTICAL);
			layout.addView(questionTitleLabel);
			layout.addView(questionTitle);

			String selected = fieldTypeSpinner.getSelectedItem().toString();
			FieldType fieldType = FieldType.getFromUIName(selected);

			switch (fieldType) {
			case SHORT_TEXT:
				// no op currently
				break;
			case CHECKBOX:
				layout.addView(optionLayout(fieldType));
				layout.addView(optionButton(fieldType));
				break;
			case SELECT:
				layout.addView(optionLayout(fieldType));
				layout.addView(optionButton(fieldType));
				break;
			case RADIO:
				layout.addView(optionLayout(fieldType));
				layout.addView(optionButton(fieldType));
				break;
			case DATE:
				break;
			case INTEGER:
				break;
			case FLOAT:
				break;
			case LONG_TEXT:
				break;
			default:
				return;
			}

			fieldTypes.add(fieldType);
			formFieldsLayout.addView(layout);
		}
	};

	private LinearLayout optionLayout(FieldType fieldType) {
		String optionStr = fieldType.getUIName();

		EditText option = new EditText(app.getApplicationContext());
		option.setHint(optionStr + " " + "option");

		LinearLayout optionsLayout = new LinearLayout(app.getApplicationContext());
		optionsLayout.setOrientation(LinearLayout.VERTICAL);
		optionsLayout.addView(option);

		if (fieldType == FieldType.SELECT||fieldType == FieldType.RADIO) {
			option = new EditText(app.getApplicationContext());
			option.setHint(optionStr + " " + "option");
			optionsLayout.addView(option);
		}

		return optionsLayout;
	}

	private Button optionButton(FieldType f) {
		Button newOptionButton = new Button(app.getApplicationContext());
		newOptionButton.setText("Add "+f.getUIName()+" Option");

		newOptionButton.setOnClickListener(newOptionButtonClickListener);

		return newOptionButton;
	}



	private OnClickListener newOptionButtonClickListener = new OnClickListener() {
		public void onClick(View v) {
			LinearLayout parent = (LinearLayout) v.getParent();
			
			LinearLayout optionsLayout = (LinearLayout) parent.getChildAt(2);
			EditText defaultOption = (EditText)optionsLayout.getChildAt(0);
			EditText option = new EditText(app.getApplicationContext());	
			option.setHint(defaultOption.getHint());
			optionsLayout.addView(option);
		}
	};




	public List<TaskTypeField> getTaskTypeFields() {
		List<TaskTypeField> taskTypeFields = new ArrayList<TaskTypeField>();

		for (int i = 0; i < formFieldsLayout.getChildCount(); i++) {
			LinearLayout layout = (LinearLayout) formFieldsLayout.getChildAt(i);

			TaskTypeField taskTypeField = new TaskTypeField();
			

			EditText questionTitle = (EditText) layout.getChildAt(1);
			String fieldName = questionTitle.getText().toString();
			taskTypeField.setName(fieldName);

			FieldType fieldType = fieldTypes.get(i);
			taskTypeField.setFieldType(fieldType);

			if (fieldType == FieldType.SHORT_TEXT) {
				// Nothing to do for now
			} else if (fieldType == FieldType.DATE) {
				// Nothing to do for now
			} else if (fieldType == FieldType.SELECT || fieldType == FieldType.RADIO || fieldType == FieldType.CHECKBOX) {
				LinearLayout optionsLayout = (LinearLayout) layout.getChildAt(2);
				List<FieldAttribute> FieldAttributes = fieldAttributes(optionsLayout, taskTypeField);
				taskTypeField.setAttributes(FieldAttributes);
			} 

			taskTypeFields.add(taskTypeField);

		}

		return taskTypeFields;
	}

	private List<FieldAttribute> fieldAttributes(LinearLayout optionsLayout, TaskTypeField taskTypeField) {
		List<FieldAttribute> fieldAttributes = new ArrayList<FieldAttribute>();

		for (int j = 0; j < optionsLayout.getChildCount(); j++) {
			EditText option = (EditText) optionsLayout.getChildAt(j);
			String value = option.getText().toString();

			FieldAttribute fieldAttribute = new FieldAttribute();
			fieldAttribute.setName("option" + j);
			fieldAttribute.setValue(value);
			fieldAttribute.setTaskTypeField(taskTypeField);
			fieldAttributes.add(fieldAttribute);
		}

		return fieldAttributes;
	}
}
