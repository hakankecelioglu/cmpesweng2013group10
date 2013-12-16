package com.groupon.mobile;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.groupon.mobile.TaskTypeFragment.OnTaskTypeSelectedListener;

public class CreateTaskActivity extends BaseFragmentActivity implements OnTaskTypeSelectedListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_task);

		TaskTypeFragment taskTypeFragment = new TaskTypeFragment();
		taskTypeFragment.setArguments(getIntent().getExtras());

		getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, taskTypeFragment).commit();
		/*
		 * TaskFormFragment taskTypeFragment = new TaskFormFragment();
		 * taskTypeFragment.setArguments(getIntent().getExtras());
		 * 
		 * getSupportFragmentManager().beginTransaction()
		 * .add(R.id.fragment_container, taskTypeFragment).commit();
		 */
	}

	@Override
	public void onTaskTypeSelected(long position) {

		TaskFormFragment newFragment = new TaskFormFragment();

		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

		transaction.replace(R.id.fragment_container, newFragment);
		transaction.addToBackStack(null);

		transaction.commit();

	}

}
