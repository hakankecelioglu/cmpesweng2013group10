package com.groupon.mobile.conn;

public interface GrouponCallback<T> {
	public void onSuccess(T response);

	public void onFail(String errorMessage);
}
