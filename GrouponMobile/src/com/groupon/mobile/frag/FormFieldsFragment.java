package com.groupon.mobile.frag;

import java.util.List;

import android.app.Activity;


import android.view.View;
import android.view.View.OnClickListener;

import com.groupon.mobile.model.TaskTypeField;



public class FormFieldsFragment extends DynamicFieldsFragment {
	public OnFormFieldsSelectedListener mCallBack;
	public interface OnFormFieldsSelectedListener {
		public void OnFormFieldsSelected(List<TaskTypeField> taskTypeFields);
	}
	public FormFieldsFragment(){
		super("Enter the fields which will be filled by task creators");
		super.saveButtonClickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();			
				mCallBack.OnFormFieldsSelected(getTaskTypeFields());
			}		
		};
	}
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mCallBack = (OnFormFieldsSelectedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement onFormFieldsSelectedListener");
		}
	}
}
