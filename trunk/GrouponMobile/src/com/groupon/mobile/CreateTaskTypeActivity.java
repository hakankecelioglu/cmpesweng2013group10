package com.groupon.mobile;

import java.util.ArrayList;
import java.util.List;

import com.groupon.mobile.conn.GrouponCallback;
import com.groupon.mobile.model.Community;
import com.groupon.mobile.model.FieldAttribute;
import com.groupon.mobile.model.FieldType;
import com.groupon.mobile.model.TaskType;
import com.groupon.mobile.model.TaskTypeField;
import com.groupon.mobile.service.TaskTypeService;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class CreateTaskTypeActivity extends BaseActivity{
	private EditText taskNameField;
	private EditText taskDescriptionField;
	private Button taskTypeCreateButton;
	private Button addFormButton;
	private Spinner fieldTypeSpinner;
	private LinearLayout formFieldsLayout;
	private long communityId;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_task_type);
		communityId = getIntent().getLongExtra("communityId", -1);
		setupUI();
	}	
	private void setupUI() {
		taskNameField = (EditText) findViewById(R.id.task_name);
		taskDescriptionField = (EditText) findViewById(R.id.task_description);
		taskTypeCreateButton = (Button) findViewById(R.id.button_create_task_type);
		taskTypeCreateButton.setOnClickListener(createButtonClickListener);		
		addFormButton = (Button) findViewById(R.id.button_add_form_field);
		addFormButton.setOnClickListener(addFormButtonClickListener);
		fieldTypeSpinner=(Spinner) findViewById(R.id.form_field_type);
		formFieldsLayout=(LinearLayout) findViewById(R.id.layout_form_fields);
		formFieldsLayout.setOrientation(LinearLayout.VERTICAL);
	} 
	private OnClickListener  addFormButtonClickListener = new OnClickListener(){
		@Override
		public void onClick(View v) {
			
			LinearLayout fieldTypeLayout = new LinearLayout(getApplicationContext());
			fieldTypeLayout.setOrientation(LinearLayout.VERTICAL);
			EditText questionTitle=new EditText(getApplicationContext());			
			questionTitle.setHint("Question Title");
			
			String selected=fieldTypeSpinner.getSelectedItem().toString();
			TextView questionTitleLabel = new TextView(getApplicationContext());
			questionTitleLabel.setText("Question Title");
			if(selected.equals("Text")){
				LinearLayout textLayout = new LinearLayout(getApplicationContext());
				TextView hidden =new TextView(getApplicationContext());
				hidden.setText("Text");
				hidden.setVisibility(View.GONE);
				textLayout.setOrientation(LinearLayout.VERTICAL);
				textLayout.addView(questionTitleLabel);
				textLayout.addView(questionTitle);
				fieldTypeLayout.addView(hidden);
				fieldTypeLayout.addView(textLayout);
			}
			else if(selected.equals("CheckBox")){
				TextView hidden =new TextView(getApplicationContext());
				hidden.setText("CheckBox");
				hidden.setVisibility(View.GONE);
				EditText option=new EditText(getApplicationContext());
				option.setHint("option");
				Button newOptionButton = new Button(getApplicationContext());
				newOptionButton.setText("Add New Option");
				newOptionButton.setOnClickListener(newOptionButtonClickListener);
				LinearLayout optionsLayout = new LinearLayout(getApplicationContext());
				optionsLayout.setOrientation(LinearLayout.VERTICAL);
				optionsLayout.addView(option);
				LinearLayout checkBoxLayout = new LinearLayout(getApplicationContext());
				checkBoxLayout.setOrientation(LinearLayout.VERTICAL);
				checkBoxLayout.addView(questionTitleLabel);
				checkBoxLayout.addView(questionTitle);
				checkBoxLayout.addView(optionsLayout);
				checkBoxLayout.addView(newOptionButton);
				fieldTypeLayout.addView(hidden);
				fieldTypeLayout.addView(checkBoxLayout);
			}
			else if(selected.equals("DropDown")){
				TextView hidden =new TextView(getApplicationContext());
				hidden.setText("DropDown");
				hidden.setVisibility(View.GONE);
				EditText option=new EditText(getApplicationContext());
				option.setHint("option");
				Button newOptionButton = new Button(getApplicationContext());
				newOptionButton.setText("Add New Option");
				newOptionButton.setOnClickListener(newOptionButtonClickListener);
				LinearLayout optionsLayout  = new LinearLayout(getApplicationContext());
				optionsLayout.setOrientation(LinearLayout.VERTICAL);
				optionsLayout.addView(option);
				LinearLayout dropDownLayout = new LinearLayout(getApplicationContext());
				dropDownLayout.setOrientation(LinearLayout.VERTICAL);
				dropDownLayout.addView(questionTitleLabel);
				dropDownLayout.addView(questionTitle);
				dropDownLayout.addView(optionsLayout);
				dropDownLayout.addView(newOptionButton);
				fieldTypeLayout.addView(hidden);
				fieldTypeLayout.addView(dropDownLayout);
								
			}
			formFieldsLayout.addView(fieldTypeLayout);
		}
	};
	private OnClickListener newOptionButtonClickListener =new OnClickListener() {

		@Override
		public void onClick(View v) {
			LinearLayout parent=(LinearLayout)v.getParent();
			LinearLayout optionsLayout = (LinearLayout) parent.getChildAt(2);
			EditText option = new EditText(getApplicationContext());
			option.setHint("option");
			optionsLayout.addView(option);
			
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
			TaskType TaskType = new TaskType();
			TaskType.setName(name);
			TaskType.setDescription(description);
			Community c=new Community();
			c.setId(communityId);
			TaskType.setCommunity(c);
			List<TaskTypeField> TaskTypeFields = new ArrayList<TaskTypeField>();
			for (int i=0; i < formFieldsLayout.getChildCount(); i++){
				  LinearLayout Parent = (LinearLayout) formFieldsLayout.getChildAt(i);
			      TextView text = (TextView) Parent.getChildAt(0);
			      TaskTypeField TaskTypeField=new TaskTypeField();
			      TaskTypeField.setTaskType(TaskType);
			      String selected = (String) text.getText();
			      if(selected.equals("Text")){			    	  
			    	  LinearLayout TextLayout = (LinearLayout)Parent.getChildAt(1);
			    	  EditText questionTitle = (EditText) TextLayout.getChildAt(1);
			    	  String fieldName = questionTitle.getText().toString();
			    	  TaskTypeField.setName(fieldName);
			    	  TaskTypeField.setFieldType(FieldType.SHORT_TEXT);
			    	  
			      }
			      else if(selected.equals("DropDown")){
			    	  LinearLayout DropDownLayout = (LinearLayout)Parent.getChildAt(1);
			    	  EditText questionTitle = (EditText) DropDownLayout.getChildAt(1);
			    	  String fieldName =  questionTitle.getText().toString();
			    	  TaskTypeField.setName(fieldName);	
			    	  TaskTypeField.setFieldType(FieldType.SELECT);
			    	  List<FieldAttribute> FieldAttributes = new ArrayList<FieldAttribute>();
			    	  LinearLayout optionsLayout = (LinearLayout) DropDownLayout.getChildAt(2);
				    	  for(int j=0; j<optionsLayout.getChildCount(); j++){
				    		  EditText option = (EditText) optionsLayout.getChildAt(j);
				    		  String value = option.getText().toString();
				    		  FieldAttribute FieldAttribute = new FieldAttribute();
				    		  FieldAttribute.setName("option"+j);
				    		  FieldAttribute.setValue(value);
				    		  FieldAttribute.setTaskTypeField(TaskTypeField);
				    		  FieldAttributes.add(FieldAttribute);
				    		 
				    	  }
			    	  TaskTypeField.setAttributes(FieldAttributes);
			      }
			      else if(selected.equals("CheckBox")){
			    	  LinearLayout CheckBoxLayout = (LinearLayout)Parent.getChildAt(1);
			    	  EditText questionTitle = (EditText) CheckBoxLayout.getChildAt(1);
			    	  String fieldName = questionTitle.getText().toString();
			    	  TaskTypeField.setName(fieldName);
			    	  TaskTypeField.setFieldType(FieldType.CHECKBOX);
			    	  List<FieldAttribute> FieldAttributes = new ArrayList<FieldAttribute>();
			    	  LinearLayout optionsLayout = (LinearLayout) CheckBoxLayout.getChildAt(2);
				    	  for(int j=0; j<optionsLayout.getChildCount(); j++){
				    		  EditText option = (EditText) optionsLayout.getChildAt(j);
				    		  String value = option.getText().toString();
				    		  FieldAttribute FieldAttribute = new FieldAttribute();
				    		  FieldAttribute.setName("option"+i);
				    		  FieldAttribute.setValue(value);
				    		  FieldAttribute.setTaskTypeField(TaskTypeField);
				    		  FieldAttributes.add(FieldAttribute);    		  
				    	  }
			    	  TaskTypeField.setAttributes(FieldAttributes);
			      }
			      
			      TaskTypeFields.add(TaskTypeField);
			      		      
			}
			TaskType.setFields(TaskTypeFields);
			service.createTaskType(TaskType, new GrouponCallback<TaskType>() {
				public void onSuccess(TaskType TaskType) {
					Intent intent = new Intent (CreateTaskTypeActivity.this, CommunityActivity.class);
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
	
}
