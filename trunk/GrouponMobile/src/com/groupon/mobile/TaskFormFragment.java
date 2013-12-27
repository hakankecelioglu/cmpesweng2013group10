package com.groupon.mobile;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
	TaskType taskType;
	private long communityId;
	private List<FieldType> fieldTypes;
	private EditText taskNameField;
	private EditText taskDescriptionField;
	private EditText taskDeadlineField;
	private EditText taskRequirementName;
	private EditText taskRequirementQuantity;
	private LinearLayout formFieldsLayout;

	private Button createTask;
	View v;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);      
        fieldTypes = new ArrayList<FieldType>();
        taskTypeId = getArguments().getLong("taskTypeId");   
        communityId = getArguments().getLong("communityId");   
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    		setTaskType();
    		v =  inflater.inflate(R.layout.fragment_task_form, container, false); 		
    		return v;
    }
    
    private void setupUI(View v){
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
   	
    	for (TaskTypeField taskTypeField : taskTypeFields) {
    		
    		View fieldView = getFieldView(taskTypeField);
    		formFieldsLayout.addView(fieldView);

		}
    	if(taskType.getNeedType()==NeedType.GOODS){
    		taskRequirementName = new EditText(getActivity().getApplicationContext());
    		taskRequirementName.setHint("What is the need ?");
    		taskRequirementQuantity = new EditText(getActivity().getApplicationContext());
    		taskRequirementQuantity.setHint("What is the required quantity of your need");
    		taskRequirementQuantity.setInputType(InputType.TYPE_CLASS_NUMBER);
    		formFieldsLayout.addView(taskRequirementName);
    		formFieldsLayout.addView(taskRequirementQuantity);
    	}
    	else if(taskType.getNeedType()==NeedType.SERVICE){
    		taskRequirementName = new EditText(getActivity().getApplicationContext());
    		taskRequirementName.setHint("What service do you need?");
    		formFieldsLayout.addView(taskRequirementName);
    	}
    		
    	
    }
    private OnClickListener createButtonClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			sendTask();
			
		}
	};
    private List<TaskAttribute> parseTaskAttributes(){
    	List<TaskTypeField> taskTypeFields=taskType.getFields();
    	List<TaskAttribute> taskAttributes = new ArrayList<TaskAttribute>();
    	for (int i=0; i<taskTypeFields.size(); i++) {
    	    TaskTypeField taskTypeField = taskTypeFields.get(i);
    		FieldType fieldType = taskTypeField.getFieldType();
    		TaskAttribute taskAttribute = new TaskAttribute();	
    		String name = taskTypeField.getName();	
    		String value = "";
			if (fieldType == FieldType.SHORT_TEXT) {
				value = ((EditText)formFieldsLayout.getChildAt(i)).getText().toString();
			} else if (fieldType == FieldType.SELECT) {
				LinearLayout spinnerLayout = (LinearLayout)formFieldsLayout.getChildAt(i);
				//title TextView in first index
				value = ((Spinner)spinnerLayout.getChildAt(1)).getSelectedItem().toString();
			} else if (fieldType == FieldType.CHECKBOX) {
				LinearLayout checkBoxLayout = (LinearLayout)formFieldsLayout.getChildAt(i);
				//title TextView in first index
				for(int j=1; j<checkBoxLayout.getChildCount(); j++){
					CheckBox checkBox = ((CheckBox)checkBoxLayout.getChildAt(j));
					if(checkBox.isChecked()){
						TaskAttribute checkBoxAttribute = new TaskAttribute();
						checkBoxAttribute.setName(name);			
						String checkBoxValue = checkBox.getText().toString();
						checkBoxAttribute.setValue(checkBoxValue);
						taskAttributes.add(checkBoxAttribute);
					}
				}
				continue;
			}
			taskAttribute.setName(name);
			taskAttribute.setValue(value);
			taskAttributes.add(taskAttribute);
		}
    	return taskAttributes;
    }
    private void sendTask(){
		String name = taskNameField.getText().toString();
		String description = taskDescriptionField.getText().toString();
		String deadline = taskDeadlineField.getText().toString();
		
		Task task = new Task();
		task.setName(name);
		task.setDescription(description);
		task.setDeadline(deadline);
    	if(taskType.getNeedType()==NeedType.GOODS){
    		
    		String requirementName=taskRequirementName.getText().toString();
    		task.setRequirementName(requirementName);
    		int requirementQuantity = Integer.parseInt(taskRequirementQuantity.getText().toString());
    		task.setRequirementQuantity(requirementQuantity);
    	}
    	else if(taskType.getNeedType()==NeedType.SERVICE){
    		String requirementName=taskRequirementName.getText().toString();
    		task.setRequirementName(requirementName);
    	}
		List<TaskAttribute> taskAttributes = parseTaskAttributes();
    	task.setAttributes(taskAttributes);
    	task.setNeedType(taskType.getNeedType().toString());
    	TaskService taskService = new TaskService((GrouponApplication) getActivity().getApplication());
    	taskService.createTask(task,communityId, new GrouponCallback<Task>() {
			
			@Override
			public void onSuccess(Task task) {
				Intent intent = new Intent(getActivity(), TaskActivity.class);
				Long id = task.getId();
				intent.putExtra("taskId", id);
				getActivity().startActivity(intent);
				getActivity().finish();
				
			}
			
			@Override
			public void onFail(String errorMessage) {
				// TODO Auto-generated method stub
				
			}
		});
    }
    private View getFieldView(TaskTypeField taskTypeField){
    	FieldType f = taskTypeField.getFieldType();
    	fieldTypes.add(f);
    	String name = taskTypeField.getName();
    	List<FieldAttribute> fieldAttributes = taskTypeField.getAttributes();
    	
		switch (f) {
		case SHORT_TEXT:
			EditText e = new EditText(getActivity().getApplicationContext());
			e.setHint(name);
			return e;		
		case SELECT:
			LinearLayout selectLayout = new LinearLayout(getActivity().getApplicationContext());
			selectLayout.setOrientation(LinearLayout.VERTICAL);
			TextView spinnerTitle = new TextView(getActivity().getApplicationContext());
			spinnerTitle.setText(name);
			selectLayout.addView(spinnerTitle);
			Spinner spinner = new Spinner(getActivity().getApplicationContext());
			List<String> options = new ArrayList<String>();
			for (FieldAttribute fieldAttribute : fieldAttributes) {
				options.add(fieldAttribute.getValue());
			}
		    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, options);
		    spinner.setAdapter(adapter);	

		    selectLayout.addView(spinner);
			return selectLayout;
		case CHECKBOX:
			LinearLayout checkBoxLayout = new LinearLayout(getActivity().getApplicationContext());
			checkBoxLayout.setOrientation(LinearLayout.VERTICAL);
			TextView title = new TextView(getActivity().getApplicationContext());
			title.setText(name);
			checkBoxLayout.addView(title);
			for (FieldAttribute fieldAttribute : fieldAttributes) {
				CheckBox checkBox =new CheckBox(getActivity().getApplicationContext());
				checkBox.setText(fieldAttribute.getValue());
				checkBoxLayout.addView(checkBox);
			}			
			return checkBoxLayout;
		default:
			return null;
		}
		
    }
    private void setTaskType(){
    	TaskTypeService service = new TaskTypeService((GrouponApplication)getActivity().getApplication());
		service.getTaskType(taskTypeId, new GrouponCallback<TaskType>() {

			public void onFail(String errorMessage) {
		
			}

			@Override
			public void onSuccess(TaskType taskType) {
				TaskFormFragment.this.taskType = taskType;
				setupUI(v);
			}
		});   	   	
    }
    public void showDatePickerDialog(View v) {
//        DialogFragment newFragment = new DatePickerFragment();
//        newFragment.show(getChildFragmentManager(), "datePicker");
    }
 
    
}
