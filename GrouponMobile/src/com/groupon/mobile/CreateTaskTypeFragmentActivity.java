package com.groupon.mobile;




import java.util.List;



















import com.groupon.mobile.conn.GrouponCallback;
import com.groupon.mobile.frag.FormFieldsFragment;
import com.groupon.mobile.frag.FormFieldsFragment.OnFormFieldsSelectedListener;
import com.groupon.mobile.frag.ReplyFieldsFragment.OnReplyFieldsSelectedListener;
import com.groupon.mobile.frag.ReplyFieldsFragment;
import com.groupon.mobile.model.NeedType;
import com.groupon.mobile.model.TaskType;
import com.groupon.mobile.model.TaskTypeField;





import com.groupon.mobile.service.TaskTypeService;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;



public class CreateTaskTypeFragmentActivity extends BaseFragmentActivity implements OnFormFieldsSelectedListener, OnReplyFieldsSelectedListener {
	private long communityId;
	private EditText taskNameField;
	private EditText taskDescriptionField;

	private Button	formFieldsButton;
	private Button	replyFieldsButton;
	private Button createButton;
	private NeedType needType;
	private Spinner needTypeSpinner;
	private List<TaskTypeField> taskTypeFields;
	private List<TaskTypeField> replyFields; 
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_task_type_fragment);
		communityId = getIntent().getLongExtra("communityId", -1);
		setupUI();
	}
	private void setupUI() {
		createButton = (Button) findViewById(R.id.button_create);
		createButton.setOnClickListener(createButtonClickListener);
		taskNameField = (EditText) findViewById(R.id.task_name);
		needTypeSpinner = (Spinner) findViewById(R.id.need_type);
		needTypeSpinner.setOnItemSelectedListener(needTypeSpinnerListener);
		taskDescriptionField = (EditText) findViewById(R.id.task_description);
		formFieldsButton = (Button)findViewById(R.id.button_form_fields);
		formFieldsButton.setOnClickListener(formFieldsButtonClickListener);
		replyFieldsButton = (Button) findViewById(R.id.button_reply_fields);
		replyFieldsButton.setOnClickListener(replyFieldsButtonClickListener);
	}
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
				Toast.makeText(CreateTaskTypeFragmentActivity.this, "Task type name cannot be empty!", Toast.LENGTH_SHORT).show();
				return;
			}

			if (description.trim().equals("")) {
				Toast.makeText(CreateTaskTypeFragmentActivity.this, "Task type description cannot be empty!", Toast.LENGTH_SHORT).show();
				return;
			}

			TaskTypeService service = new TaskTypeService(getApp());
			TaskType taskType = new TaskType();
			taskType.setName(name);
			taskType.setDescription(description);
			taskType.setNeedType(needType);
			taskType.setCommunityId(communityId);
			taskType.setFields(taskTypeFields);
			taskType.setReplyFields(replyFields);
			service.createTaskType(taskType, new GrouponCallback<TaskType>() {
				public void onSuccess(TaskType TaskType) {
					finish();
				}

				public void onFail(String errorMessage) {
					Toast.makeText(CreateTaskTypeFragmentActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
				}
			});

		}
	};
	private OnClickListener formFieldsButtonClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			FormFieldsFragment fragment = new  FormFieldsFragment();
			Bundle args = new Bundle();			
			args.putLong("communityId", communityId);
			fragment.setArguments(args);
			FragmentManager fm =getSupportFragmentManager();
			fragment.show(fm, "form_fields_fragment");
		}
		
	};
	
	private OnClickListener replyFieldsButtonClickListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			ReplyFieldsFragment fragment = new  ReplyFieldsFragment();
			Bundle args = new Bundle();
			
			args.putLong("communityId", communityId);
			fragment.setArguments(args);
			FragmentManager fm =getSupportFragmentManager();
			fragment.show(fm, "reply_fields_fragment");		
		}
		
	};
	@Override
	public void OnFormFieldsSelected(List<TaskTypeField> taskTypeFields) {
		this.taskTypeFields= taskTypeFields;
		
	}
	@Override
	public void OnReplyFieldsSelected(List<TaskTypeField> taskTypeFields) {
		this.replyFields = taskTypeFields;
		
	}



}
