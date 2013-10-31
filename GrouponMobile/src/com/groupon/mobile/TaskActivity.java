package com.groupon.mobile;

import java.util.Map;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.Button;
import android.widget.TextView;

public class TaskActivity extends Activity {
	private TextView taskNameField;
	private TextView taskDescriptionField;
	private TextView taskDeadlineField;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task);
		int id = getIntent().getIntExtra("taskId", -1);
		Map<String, Object> m = DummyController.getTask(id);
		String name = (String) m.get("name");
		String description = (String) m.get("description");
		String deadline = (String) m.get("deadline");
		setupUI(name, description, deadline);
	}

	private void setupUI(String name, String description, String deadline) {

		taskNameField = (TextView) findViewById(R.id.taskName);
		taskNameField.setText(name);
		taskDescriptionField = (TextView) findViewById(R.id.taskDescription);
		taskDescriptionField.setText(description);
		taskDeadlineField = (TextView) findViewById(R.id.taskDeadline);
		taskDeadlineField.setText(deadline);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.task, menu);
		return true;
	}

}
