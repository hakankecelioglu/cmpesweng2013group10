package com.groupon.mobile.conn;

import android.os.AsyncTask;

import com.groupon.mobile.exception.GrouponException;

public abstract class GrouponTask<T> extends AsyncTask<Void, Void, Void> {

	private GrouponCallback<T> callback;

	private T result;

	private GrouponException e;

	public GrouponTask(GrouponCallback<T> callback) {
		this.callback = callback;
	}

	public abstract T run() throws GrouponException;

	@Override
	protected Void doInBackground(Void... params) {
		try {
			this.result = run();
		} catch (GrouponException e) {
			this.e = e;
		}
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		if (this.callback != null) {
			if (this.e == null) {
				this.callback.onSuccess(this.result);
			} else {
				this.callback.onFail(e.getMessage());
			}
		}
	}

	public static void execute(GrouponTask<?> task) {
		task.execute(new Void[0]);
	}

}
