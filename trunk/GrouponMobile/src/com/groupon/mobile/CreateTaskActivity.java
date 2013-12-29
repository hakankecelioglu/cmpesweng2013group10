package com.groupon.mobile;

import com.groupon.mobile.frag.TaskFormFragment;
import com.groupon.mobile.frag.TaskTypeFragment;
import com.groupon.mobile.frag.TaskTypeFragment.OnTaskTypeSelectedListener;


import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

public class CreateTaskActivity extends BaseFragmentActivity implements OnTaskTypeSelectedListener {

	private long communityId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_task);

		communityId = getIntent().getLongExtra("communityId", -1);
        TaskTypeFragment taskTypeFragment = new TaskTypeFragment();
        taskTypeFragment.setArguments(getIntent().getExtras());

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, taskTypeFragment).commit();

	}

	@Override
	public void onTaskTypeSelected(long taskTypeId) {

            TaskFormFragment newFragment = new TaskFormFragment();
            Bundle args = new Bundle();
            args.putLong("taskTypeId", taskTypeId);
            args.putLong("communityId", communityId);
            newFragment.setArguments(args);
        
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            transaction.replace(R.id.fragment_container, newFragment);
            transaction.addToBackStack(null);

            transaction.commit();		
		
	}





}
