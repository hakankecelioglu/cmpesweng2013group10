package com.groupon.mobile;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateTaskActivity extends Activity {
	private EditText taskNameField;
	private EditText taskDescriptionField;
	private EditText taskDeadlineField;
	private EditText taskTagsField;
	private EditText taskTypeField;
	private Button taskCreateButton;

	private int communityId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_task);

		Intent intent = getIntent();
		communityId = intent.getIntExtra("communityId", -1);

		setupUI();
	}

	private void setupUI() {
		taskNameField = (EditText) findViewById(R.id.task_name);
		taskDescriptionField = (EditText) findViewById(R.id.task_description);
		taskDeadlineField = (EditText) findViewById(R.id.task_deadline);
		taskTagsField = (EditText) findViewById(R.id.task_tags);
		taskTypeField = (EditText) findViewById(R.id.task_type);

		taskCreateButton = (Button) findViewById(R.id.button_task_create);
		taskCreateButton.setOnClickListener(taskCreateClickListener);
	}

	private OnClickListener taskCreateClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			String taskName = taskNameField.getText().toString();
			if (taskName.trim().equals("")) {
				Toast.makeText(CreateTaskActivity.this, "Task must have a name!", Toast.LENGTH_SHORT).show();
				return;
			}

			String description = taskDescriptionField.getText().toString();
			if (description.trim().equals("")) {
				Toast.makeText(CreateTaskActivity.this, "Task must have a description!", Toast.LENGTH_SHORT).show();
				return;
			}

			String deadline = taskDeadlineField.getText().toString();
			if (deadline.trim().equals("")) {
				Toast.makeText(CreateTaskActivity.this, "Task must have a deadline!", Toast.LENGTH_SHORT).show();
				return;
			}

			String tags = taskTagsField.getText().toString();
			String type = taskTypeField.getText().toString();

			Map<String, Object> task = new HashMap<String, Object>();
			task.put("name", taskName);
			task.put("description", description);
			task.put("deadline", deadline);
			task.put("tags", tags);
			task.put("type", type);

			int taskId = DummyController.createTask(task, communityId);

			Intent intent = new Intent(CreateTaskActivity.this, TaskActivity.class);
			intent.putExtra("taskId", taskId);
			startActivity(intent);
			CreateTaskActivity.this.finish();
		}
	};

}
