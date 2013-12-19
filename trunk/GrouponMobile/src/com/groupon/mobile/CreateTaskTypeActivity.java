package com.groupon.mobile;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.groupon.mobile.conn.GrouponCallback;
import com.groupon.mobile.model.FieldAttribute;
import com.groupon.mobile.model.FieldType;
import com.groupon.mobile.model.NeedType;
import com.groupon.mobile.model.TaskType;
import com.groupon.mobile.model.TaskTypeField;
import com.groupon.mobile.service.TaskTypeService;

public class CreateTaskTypeActivity extends BaseActivity {
	private EditText taskNameField;
	private EditText taskDescriptionField;
	private Button taskTypeCreateButton;
	private Button addFormButton;
	private Spinner fieldTypeSpinner;
	private ArrayAdapter<String> fieldTypeAdapter;
	private Spinner needTypeSpinner;
	private NeedType needType;
	private long communityId;
	private List<FieldType> fieldTypes;

	// All form fields will lay out in this layout
	private LinearLayout formFieldsLayout;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_task_type);
		fieldTypes = new ArrayList<FieldType>();
		communityId = getIntent().getLongExtra("communityId", -1);
		setupUI();
	}

	private void setupUI() {
		taskNameField = (EditText) findViewById(R.id.task_name);
		taskDescriptionField = (EditText) findViewById(R.id.task_description);
		taskTypeCreateButton = (Button) findViewById(R.id.button_create_task_type);
		addFormButton = (Button) findViewById(R.id.button_add_form_field);
		fieldTypeSpinner = (Spinner) findViewById(R.id.form_field_type);
		needTypeSpinner = (Spinner) findViewById(R.id.need_type);
		formFieldsLayout = (LinearLayout) findViewById(R.id.layout_form_fields);

		taskTypeCreateButton.setOnClickListener(createButtonClickListener);
		addFormButton.setOnClickListener(addFormButtonClickListener);
		needTypeSpinner.setOnItemSelectedListener(needTypeSpinnerListener);
		formFieldsLayout.setOrientation(LinearLayout.VERTICAL);

		fieldTypeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, FieldType.getFieldTypes());
		fieldTypeSpinner.setAdapter(fieldTypeAdapter);
	}

	private OnClickListener addFormButtonClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			EditText questionTitle = new EditText(getApplicationContext());
			questionTitle.setHint("Question Title");

			TextView questionTitleLabel = new TextView(getApplicationContext());
			questionTitleLabel.setText("Question Title");

			LinearLayout layout = new LinearLayout(getApplicationContext());
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
				layout.addView(optionCheckBoxButton());
				break;
			case SELECT:
				layout.addView(optionLayout(fieldType));
				layout.addView(optionDropDownButton());
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

		EditText option = new EditText(getApplicationContext());
		option.setHint(optionStr + " " + "option");

		LinearLayout optionsLayout = new LinearLayout(getApplicationContext());
		optionsLayout.setOrientation(LinearLayout.VERTICAL);
		optionsLayout.addView(option);

		if (fieldType == FieldType.SELECT) {
			option = new EditText(getApplicationContext());
			option.setHint(optionStr + " " + "option");
			optionsLayout.addView(option);
		}

		return optionsLayout;
	}

	private Button optionCheckBoxButton() {
		Button newOptionButton = new Button(getApplicationContext());
		newOptionButton.setText("Add New Check Box Option");
		newOptionButton.setOnClickListener(newCheckBoxOptionButtonClickListener);
		return newOptionButton;
	}

	private Button optionDropDownButton() {
		Button newOptionButton = new Button(getApplicationContext());
		newOptionButton.setText("Add New DropDown  Option");
		newOptionButton.setOnClickListener(newDropDownOptionButtonClickListener);
		return newOptionButton;
	}

	private OnClickListener newCheckBoxOptionButtonClickListener = new OnClickListener() {
		public void onClick(View v) {
			LinearLayout parent = (LinearLayout) v.getParent();
			LinearLayout optionsLayout = (LinearLayout) parent.getChildAt(2);
			EditText option = new EditText(getApplicationContext());
			option.setHint("Checkbox Option");
			optionsLayout.addView(option);
		}
	};

	private OnClickListener newDropDownOptionButtonClickListener = new OnClickListener() {
		public void onClick(View v) {
			LinearLayout parent = (LinearLayout) v.getParent();
			LinearLayout optionsLayout = (LinearLayout) parent.getChildAt(2);
			EditText option = new EditText(getApplicationContext());
			option.setHint("Dropdown Option");
			optionsLayout.addView(option);
		}
	};

	private OnItemSelectedListener needTypeSpinnerListener = new OnItemSelectedListener() {
		public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
			if (pos == 0)
				needType = NeedType.GOODS;
			else if (pos == 1)
				needType = NeedType.SERVICE;
			else
				needType = NeedType.ONLY_FORM;
		}

		public void onNothingSelected(AdapterView<?> arg0) {
		}
	};

	private OnClickListener createButtonClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			String name = taskNameField.getText().toString();
			String description = taskDescriptionField.getText().toString();

			if (name.trim().equals("")) {
				Toast.makeText(CreateTaskTypeActivity.this, "Task type name cannot be empty!", Toast.LENGTH_SHORT).show();
				return;
			}

			if (description.trim().equals("")) {
				Toast.makeText(CreateTaskTypeActivity.this, "Task type description cannot be empty!", Toast.LENGTH_SHORT).show();
				return;
			}

			TaskTypeService service = new TaskTypeService(getApp());
			TaskType taskType = new TaskType();
			taskType.setName(name);
			taskType.setDescription(description);
			taskType.setNeedType(needType);
			taskType.setCommunityId(communityId);

			List<TaskTypeField> taskTypeFields = getTaskTypeFields(taskType);
			taskType.setFields(taskTypeFields);

			service.createTaskType(taskType, new GrouponCallback<TaskType>() {
				public void onSuccess(TaskType TaskType) {
					Intent intent = new Intent(CreateTaskTypeActivity.this, CommunityActivity.class);
					intent.putExtra("communityId", communityId);
					startActivity(intent);
					finish();
				}

				public void onFail(String errorMessage) {
					Toast.makeText(CreateTaskTypeActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
				}
			});

		}
	};

	private List<TaskTypeField> getTaskTypeFields(TaskType taskType) {
		List<TaskTypeField> taskTypeFields = new ArrayList<TaskTypeField>();

		for (int i = 0; i < formFieldsLayout.getChildCount(); i++) {
			LinearLayout layout = (LinearLayout) formFieldsLayout.getChildAt(i);

			TaskTypeField taskTypeField = new TaskTypeField();
			taskTypeField.setTaskType(taskType);

			EditText questionTitle = (EditText) layout.getChildAt(1);
			String fieldName = questionTitle.getText().toString();
			taskTypeField.setName(fieldName);

			FieldType fieldType = fieldTypes.get(i);
			taskTypeField.setFieldType(fieldType);

			if (fieldType == FieldType.SHORT_TEXT) {
				// Nothing to do for now
			} else if (fieldType == FieldType.SELECT) {
				LinearLayout optionsLayout = (LinearLayout) layout.getChildAt(2);
				List<FieldAttribute> FieldAttributes = fieldAttributes(optionsLayout, taskTypeField);
				taskTypeField.setAttributes(FieldAttributes);
			} else if (fieldType == FieldType.CHECKBOX) {
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
