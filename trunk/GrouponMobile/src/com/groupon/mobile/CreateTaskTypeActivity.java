package com.groupon.mobile;

import java.util.ArrayList;
import java.util.List;

import com.groupon.mobile.conn.GrouponCallback;
import com.groupon.mobile.model.Community;
import com.groupon.mobile.model.FieldAttribute;
import com.groupon.mobile.model.FieldType;
import com.groupon.mobile.model.NeedType;
import com.groupon.mobile.model.TaskType;
import com.groupon.mobile.model.TaskTypeField;
import com.groupon.mobile.service.TaskTypeService;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
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
	private Spinner needTypeSpinner;
	private LinearLayout formFieldsLayout;
	private NeedType needType;
	private long communityId;
	List <String> items ;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_task_type);
		items = new ArrayList <String>();
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
		fieldTypeSpinner = (Spinner) findViewById(R.id.form_field_type);
		needTypeSpinner =  (Spinner) findViewById(R.id.need_type);
		needTypeSpinner.setOnItemSelectedListener(needTypeSpinnerListener);
		formFieldsLayout=(LinearLayout) findViewById(R.id.layout_form_fields);
		formFieldsLayout.setOrientation(LinearLayout.VERTICAL);
	} 
	private OnClickListener  addFormButtonClickListener = new OnClickListener(){
		@Override
		public void onClick(View v) {
			
		
			
			EditText questionTitle=new EditText(getApplicationContext());			
			questionTitle.setHint("Question Title");	
			
			TextView questionTitleLabel = new TextView(getApplicationContext());
			questionTitleLabel.setText("Question Title");
			
			LinearLayout layout = new LinearLayout(getApplicationContext());
			layout.setOrientation(LinearLayout.VERTICAL);
			layout.addView(questionTitleLabel);
			layout.addView(questionTitle);
			
			TextView hidden =new TextView(getApplicationContext());
			hidden.setVisibility(View.GONE);
			
			String selected=fieldTypeSpinner.getSelectedItem().toString();
			hidden.setText(selected);
			items.add(selected);
			if(selected.equals(getResources().getString(R.string.text))){	
				// no op currently
			}
			else if(selected.equals(getResources().getString(R.string.check_box))){
				
				layout.addView(optionLayout(0));
				layout.addView(optionCheckBoxButton());
			}
			else if(selected.equals(getResources().getString(R.string.dropdown))){				
				
				layout.addView(optionLayout(1));
				layout.addView(optionDropDownButton());								
			}

			formFieldsLayout.addView(layout);
		}
	};
	private LinearLayout optionLayout(int n){
		String optionStr="";
		switch(n){
			case 0:
				optionStr = "Checkbox";
				break;
			case 1:
				optionStr = "Dropdown";
				break;
		}
					
		EditText option=new EditText(getApplicationContext());
		option.setHint(optionStr+" "+"option");

		LinearLayout optionsLayout  = new LinearLayout(getApplicationContext());
		optionsLayout.setOrientation(LinearLayout.VERTICAL);
		optionsLayout.addView(option);
		if(n==1){
			option=new EditText(getApplicationContext());
			option.setHint(optionStr+" "+"option");
			optionsLayout.addView(option);
		}
		
		return optionsLayout;
	}
	private Button optionCheckBoxButton(){
		Button newOptionButton = new Button(getApplicationContext());
		newOptionButton.setText("Add New Check Box Option");
		newOptionButton.setOnClickListener(newCheckBoxOptionButtonClickListener);
		return newOptionButton;
	}
	
	private Button optionDropDownButton(){
		Button newOptionButton = new Button(getApplicationContext());
		newOptionButton.setText("Add New DropDown  Option");
		newOptionButton.setOnClickListener(newDropDownOptionButtonClickListener);
		return newOptionButton;
	}
	
	private OnClickListener newCheckBoxOptionButtonClickListener =new OnClickListener() {

		@Override
		public void onClick(View v) {
			LinearLayout parent=(LinearLayout)v.getParent();
			LinearLayout optionsLayout = (LinearLayout) parent.getChildAt(2);
			EditText option = new EditText(getApplicationContext());
			option.setHint("Checkbox Option");
			optionsLayout.addView(option);
			
		}
		
	};
	private OnClickListener newDropDownOptionButtonClickListener =new OnClickListener() {

		@Override
		public void onClick(View v) {
			LinearLayout parent=(LinearLayout)v.getParent();
			LinearLayout optionsLayout = (LinearLayout) parent.getChildAt(2);
			EditText option = new EditText(getApplicationContext());
			option.setHint("Dropdown Option");
			optionsLayout.addView(option);

		}
		
	};
	private OnItemSelectedListener needTypeSpinnerListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

			if(pos==0)
				needType=NeedType.GOODS;
			else if(pos==1)
				needType=NeedType.SERVICE;
			else
				needType=NeedType.ONLY_FORM;
			
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
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
			TaskType.setNeedType(needType);
			Community c=new Community();
			c.setId(communityId);
			TaskType.setCommunity(c);
			
			List<TaskTypeField> TaskTypeFields = getTaskTypeFields(TaskType);
			
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
	
	private List<TaskTypeField> getTaskTypeFields(TaskType TaskType){
		List<TaskTypeField> TaskTypeFields = new ArrayList<TaskTypeField>();
		for (int i=0; i < formFieldsLayout.getChildCount(); i++){
			
			  LinearLayout Layout = (LinearLayout) formFieldsLayout.getChildAt(i);
		     
		      TaskTypeField TaskTypeField=new TaskTypeField();
		      TaskTypeField.setTaskType(TaskType);

	    	  
	    	  EditText questionTitle = (EditText) Layout.getChildAt(1);		
	    	  String fieldName =  questionTitle.getText().toString();
	    	  TaskTypeField.setName(fieldName);
	    	  
		      String selected = items.get(i);
		      
		      if(selected.equals(getResources().getString(R.string.text))){			    	  
		    	  
		    	  TaskTypeField.setFieldType(FieldType.SHORT_TEXT);
		    	  
		      }
		      else if(selected.equals(getResources().getString(R.string.dropdown))){
		    	  
		    	  TaskTypeField.setFieldType(FieldType.SELECT);
		    	  LinearLayout optionsLayout = (LinearLayout) Layout.getChildAt(2);
		    	  List<FieldAttribute> FieldAttributes = FieldAttributes(optionsLayout,TaskTypeField);
		    	  TaskTypeField.setAttributes(FieldAttributes);
		      }
		      else if(selected.equals(getResources().getString(R.string.check_box))){

		    	  TaskTypeField.setFieldType(FieldType.CHECKBOX);
		    	  LinearLayout optionsLayout = (LinearLayout) Layout.getChildAt(2);
		    	  List<FieldAttribute> FieldAttributes = FieldAttributes(optionsLayout,TaskTypeField);
		    	  TaskTypeField.setAttributes(FieldAttributes);
		    	  
		      }
		      
		      TaskTypeFields.add(TaskTypeField);
		      		      
		}
		
		return TaskTypeFields;
	}
	
	private List<FieldAttribute> FieldAttributes(LinearLayout optionsLayout, TaskTypeField TaskTypeField){
		List<FieldAttribute> FieldAttributes = new ArrayList<FieldAttribute>();
	  	  for(int j=0; j<optionsLayout.getChildCount(); j++){	  
			  EditText option = (EditText) optionsLayout.getChildAt(j);
			  String value = option.getText().toString();
			  FieldAttribute FieldAttribute = new FieldAttribute();
			  FieldAttribute.setName("option"+j);
			  FieldAttribute.setValue(value);
			  FieldAttribute.setTaskTypeField(TaskTypeField);
			  FieldAttributes.add(FieldAttribute);    		  
		  }
	  	  return FieldAttributes;
	}
	
}
