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
/**
 * Activity that provides interface for creating a task type. It manages 3 child fragments 
 * and transitions between them
 * @author sedrik
 * @author serkan
 *
 */
public class CreateTaskTypeFragmentActivity extends BaseActivity {
	private long communityId;
	private TaskType taskType = new TaskType();
	boolean initialized = false;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_ACTION_BAR);
		setContentView(R.layout.activity_create_task_type);

		communityId = getIntent().getLongExtra("communityId", -1);
		taskType.setCommunityId(communityId);

		setupUI();
	}
	/**
	 * setup UI of this activity.
	 */
	public TaskType getTaskTypeInstance() {
		return taskType;
	}

	private void setupUI() {
		ActionBar actionBar = getActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(true);
			actionBar.setTitle("Create Task Type");
			actionBar.show();
		}

		if (!initialized) {
			FragmentManager fm = getFragmentManager();
			FragmentTransaction tr = fm.beginTransaction();
			tr.replace(R.id.frame_container, new CreateTaskTypeFirst());
			tr.commit();
			initialized = true;
		}
	}

	public void openSecondStep() {
		FragmentManager fm = getFragmentManager();
		FragmentTransaction tr = fm.beginTransaction();
		tr.replace(R.id.frame_container, new CreateTaskTypeSecond());
		tr.addToBackStack(null);
		tr.commit();
	}

	public void openThirdStep() {
		FragmentManager fm = getFragmentManager();
		FragmentTransaction tr = fm.beginTransaction();
		tr.replace(R.id.frame_container, new CreateTaskTypeThird());
		tr.addToBackStack(null);
		tr.commit();
	}

	@Override
	public boolean onNavigateUp() {
		finish();
		return true;
	}

	public void createTaskTypeAndFinishActivity() {
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
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (getFragmentManager().getBackStackEntryCount() == 0) {
				this.finish();
				return false;
			} else {
				getFragmentManager().popBackStack();
				removeCurrentFragment();
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
