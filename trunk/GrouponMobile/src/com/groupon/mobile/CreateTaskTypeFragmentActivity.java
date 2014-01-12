package com.groupon.mobile;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.Toast;

import com.groupon.mobile.conn.GrouponCallback;
import com.groupon.mobile.frag.CreateTaskTypeFirst;
import com.groupon.mobile.frag.CreateTaskTypeSecond;
import com.groupon.mobile.frag.CreateTaskTypeThird;
import com.groupon.mobile.model.TaskType;
import com.groupon.mobile.service.TaskTypeService;

public class CreateTaskTypeFragmentActivity extends BaseActivity implements TaskTypeStepListener {
	private static final int NUM_STEPS = 3;
	private int currentStep;
	private long communityId;
	private Fragment[] childFragments = new Fragment[3];
	private TaskType taskType;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_ACTION_BAR);
		setContentView(R.layout.activity_create_task_type);

		if (savedInstanceState != null) {
			return;
		}

		communityId = getIntent().getLongExtra("communityId", -1);
		taskType = new TaskType();
		taskType.setCommunityId(communityId);

		childFragments[0] = new CreateTaskTypeFirst();
		childFragments[1] = new CreateTaskTypeSecond();
		childFragments[2] = new CreateTaskTypeThird();

		for (Fragment f : childFragments) {
			TaskTypeStepUpdater stepUpdater = ((TaskTypeStepUpdater) f);
			stepUpdater.setTaskTypeStepListener(this);
			stepUpdater.setTaskType(taskType);
		}

		setupUI();
	}

	private void setupUI() {
		ActionBar actionBar = getActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(true);
			actionBar.setTitle("Create Task Type");
			actionBar.show();
		}

		currentStep = 0;
		changeStepFragment(currentStep, false);
	}

	@Override
	public boolean onNavigateUp() {
		finish();
		return true;
	}

	private void changeStepFragment(int index, boolean backStack) {
		FragmentManager fm = getFragmentManager();
		FragmentTransaction tr = fm.beginTransaction();
		tr.replace(R.id.frame_container, childFragments[index]);
		if (backStack) {
			tr.addToBackStack(null);
		}
		tr.commit();
	}

	@Override
	public void nextStep() {
		if (currentStep < NUM_STEPS - 1) {
			currentStep++;
			changeStepFragment(currentStep, true);
		} else if (currentStep == NUM_STEPS - 1) {
			createTaskTypeAndFinishActivity();
		}
	}

	private void createTaskTypeAndFinishActivity() {
		TaskTypeService taskTypeService = new TaskTypeService(getApp());
		taskTypeService.createTaskType(taskType, new GrouponCallback<TaskType>() {
			public void onSuccess(TaskType response) {
				finish();
			}

			public void onFail(String errorMessage) {
				Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	public void previosStep() {
		if (currentStep > 0) {
			currentStep--;
			changeStepFragment(currentStep, true);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (getFragmentManager().getBackStackEntryCount() == 0) {
				this.finish();
				return false;
			} else {
				getFragmentManager().popBackStack();
				removeCurrentFragment();
				currentStep--;
				return false;
			}
		}

		return super.onKeyDown(keyCode, event);
	}

	public void removeCurrentFragment() {
		FragmentTransaction transaction = getFragmentManager().beginTransaction();

		Fragment currentFrag = getFragmentManager().findFragmentById(R.id.frame_container);

		if (currentFrag != null)
			transaction.remove(currentFrag);

		transaction.commit();
	}
}
