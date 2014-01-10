package com.groupon.mobile.frag;

import java.util.List;

import com.groupon.mobile.frag.FormFieldsFragment.OnFormFieldsSelectedListener;
import com.groupon.mobile.model.TaskTypeField;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;

public class ReplyFieldsFragment extends DynamicFieldsFragment {
	public OnReplyFieldsSelectedListener mCallBack;
	public interface OnReplyFieldsSelectedListener {
		public void OnReplyFieldsSelected(List<TaskTypeField> taskTypeFields);
	}
	public ReplyFieldsFragment(){
		super("Enter the fields which will be filled by task repliers");
		super.saveButtonClickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();			
				mCallBack.OnReplyFieldsSelected(getTaskTypeFields());
			}		
		};
	}
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mCallBack = (OnReplyFieldsSelectedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement OnReplyFieldsSelectedListener");
		}
	}
}
