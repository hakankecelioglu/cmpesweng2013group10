package com.groupon.mobile;

import com.groupon.mobile.model.TaskType;

public interface TaskTypeStepUpdater {
	public void setTaskTypeStepListener(TaskTypeStepListener listener);
	public void setTaskType(TaskType taskType);
}
