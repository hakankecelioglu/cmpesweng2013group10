package com.groupon.mobile.frag;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.groupon.mobile.R;
import com.groupon.mobile.exception.GrouponException;
import com.groupon.mobile.model.FieldAttribute;
import com.groupon.mobile.model.FieldType;
import com.groupon.mobile.model.TaskTypeField;
import com.groupon.mobile.utils.StringUtils;
/**
 * This class provides dynamic interface for creating view elements whose types
 * selected from a spinner. Currently, short text long text check box dropdown 
 * radio date integer float and long text can be chosen and added by the user.
 * @author serkan
 * @author sedrik
 */
public abstract class DynamicFieldsFragment extends Fragment {
	private Spinner fieldTypeSpinner;
	private ArrayAdapter<String> fieldTypeAdapter;

	private Button addFormButton;
	private Button saveButton;
	private List<FieldType> fieldTypes;
	private TextView textViewDescription;
	private LinearLayout formFieldsLayout;

	private OnClickListener nextButtonClickListener;
	private String nextButtonText;
	private String title;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_form_fields, container, false);
		setupUI(rootView);
		return rootView;
	}
	/**
	 * setup UI of this fragment.
	 * @param rootView root view of this fragment.
	 */
	private void setupUI(View view) {
		textViewDescription = (TextView) view.findViewById(R.id.description);
		addFormButton = (Button) view.findViewById(R.id.button_add_form_field);
		saveButton = (Button) view.findViewById(R.id.button_save);
		fieldTypeSpinner = (Spinner) view.findViewById(R.id.form_field_type);

		formFieldsLayout = (LinearLayout) view.findViewById(R.id.layout_form_fields);
		addFormButton.setOnClickListener(addFormButtonClickListener);

		formFieldsLayout.setOrientation(LinearLayout.VERTICAL);

		fieldTypeAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, FieldType.getFieldTypes());
		fieldTypeSpinner.setAdapter(fieldTypeAdapter);

		textViewDescription.setText(title);
		saveButton.setText(nextButtonText);
		saveButton.setOnClickListener(nextButtonClickListener);

		fieldTypes = new ArrayList<FieldType>();
	}

	public void setupDefaults(String fragmentTitle, OnClickListener nextButtonClickListener, String nextButtonTitle) {
		this.nextButtonClickListener = nextButtonClickListener;
		this.nextButtonText = nextButtonTitle;
		this.title = fragmentTitle;
	}

	/**
	 * Listener of add button on the screen. It looks for selected spinner value and
	 * call necessary function to create selected type's view.
	 */
	private OnClickListener addFormButtonClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			String selected = fieldTypeSpinner.getSelectedItem().toString();
			FieldType fieldType = FieldType.getFromUIName(selected);
			addCustomFieldView(fieldType);
			scrollDown();
		}
	};

	private View addCustomFieldView(FieldType fieldType) {
		View customFieldRow;

		switch (fieldType) {
		case SHORT_TEXT:
			customFieldRow = View.inflate(getActivity(), R.layout.custom_fieldtype_short_text, null);
			break;
		case CHECKBOX:
			customFieldRow = View.inflate(getActivity(), R.layout.custom_fieldtype_checkbox, null);
			setNewCheckboxOptionButtonClickListener(customFieldRow);
			break;
		case SELECT:
			customFieldRow = View.inflate(getActivity(), R.layout.custom_fieldtype_select, null);
			setNewSelectOptionButtonClickListener(customFieldRow);
			break;
		case RADIO:
			customFieldRow = View.inflate(getActivity(), R.layout.custom_fieldtype_radio, null);
			setNewRadioOptionButtonClickListener(customFieldRow);
			break;
		case DATE:
			customFieldRow = View.inflate(getActivity(), R.layout.custom_fieldtype_date, null);
			break;
		case INTEGER:
			customFieldRow = View.inflate(getActivity(), R.layout.custom_fieldtype_integer, null);
			break;
		case FLOAT:
			customFieldRow = View.inflate(getActivity(), R.layout.custom_fieldtype_float, null);
			break;
		case LONG_TEXT:
			customFieldRow = View.inflate(getActivity(), R.layout.custom_fieldtype_long_text, null);
			break;
		default:
			return null;
		}

		formFieldsLayout.addView(customFieldRow);

		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) customFieldRow.getLayoutParams();
		int margin = pxFromDp(10);
		params.setMargins(margin, margin, margin, 0);

		fieldTypes.add(fieldType);

		return customFieldRow;
	}

	protected void setSelectedTaskTypeFields(List<TaskTypeField> selectedTaskTypeFields) {
		fieldTypes.clear();
		formFieldsLayout.removeAllViews();

		for (TaskTypeField selected : selectedTaskTypeFields) {
			FieldType fieldType = selected.getFieldType();
			View view = addCustomFieldView(fieldType);

			EditText questionField = (EditText) view.findViewById(R.id.custom_fieldtype_question_edittext);
			questionField.setText(selected.getName());

			fillFieldOptions(fieldType, selected.getAttributes(), view);
		}
	}


	/**
	 * sets click listener of an add option button of a radio group
	 * @param parent parent radio button view
	 */

	private void fillFieldOptions(FieldType fieldType, List<FieldAttribute> attributes, View parent) {
		int optionLayoutId;

		switch (fieldType) {
		case CHECKBOX:
			optionLayoutId = R.layout.custom_fieldtype_checkbox_option;
			break;
		case SELECT:
			optionLayoutId = R.layout.custom_fieldtype_select_option;
			break;
		case RADIO:
			optionLayoutId = R.layout.custom_fieldtype_radio_option;
			break;
		default:
			return;
		}

		LinearLayout optionsLayout = (LinearLayout) parent.findViewById(R.id.custom_fieldtype_options_layout);
		optionsLayout.removeAllViews();

		for (FieldAttribute attribute : attributes) {
			if (attribute.getName().startsWith("option")) {
				View optionLayout = View.inflate(getActivity(), optionLayoutId, null);
				EditText optionValue = (EditText) optionLayout.findViewById(R.id.custom_fieldtype_option_value);
				optionValue.setText(attribute.getValue());
				optionsLayout.addView(optionLayout);
			}
		}
	}

	private void setNewRadioOptionButtonClickListener(View parent) {
		Button button = (Button) parent.findViewById(R.id.custom_fieldtype_radio_add_option_btn);
		button.setOnClickListener(newRadioOptionButtonClickListener);
	}
	/**
	 * A new option input is added to associated radio view
	 */
	private OnClickListener newRadioOptionButtonClickListener = new OnClickListener() {
		public void onClick(View v) {
			ViewGroup parent = (ViewGroup) v.getParent();

			LinearLayout optionsLayout = (LinearLayout) parent.findViewById(R.id.custom_fieldtype_options_layout);
			View singleOption = View.inflate(getActivity(), R.layout.custom_fieldtype_radio_option, null);
			optionsLayout.addView(singleOption);

			scrollDown();
		}
	};
	/**
	 * sets click listener of an add option button of a dropdown
	 * @param parent parent select view
	 */
	private void setNewSelectOptionButtonClickListener(View parent) {
		Button button = (Button) parent.findViewById(R.id.custom_fieldtype_select_add_option_btn);
		button.setOnClickListener(newSelectOptionButtonClickListener);
	}
	/**
	 * A new option input is added to associated select view
	 */
	private OnClickListener newSelectOptionButtonClickListener = new OnClickListener() {
		public void onClick(View v) {
			ViewGroup parent = (ViewGroup) v.getParent();

			LinearLayout optionsLayout = (LinearLayout) parent.findViewById(R.id.custom_fieldtype_options_layout);
			View singleOption = View.inflate(getActivity(), R.layout.custom_fieldtype_select_option, null);
			optionsLayout.addView(singleOption);

			scrollDown();
		}
	};
	/**
	 * sets click listener of an add option button of a checkbox layout
	 * @param parent parent select view
	 */
	private void setNewCheckboxOptionButtonClickListener(View parent) {
		Button button = (Button) parent.findViewById(R.id.custom_fieldtype_checkbox_add_option_btn);
		button.setOnClickListener(newCheckboxOptionButtonClickListener);
	}
	/**
	 * A new option input is added to associated select view
	 */
	private OnClickListener newCheckboxOptionButtonClickListener = new OnClickListener() {
		public void onClick(View v) {
			ViewGroup parent = (ViewGroup) v.getParent();

			LinearLayout optionsLayout = (LinearLayout) parent.findViewById(R.id.custom_fieldtype_options_layout);
			View singleOption = View.inflate(getActivity(), R.layout.custom_fieldtype_checkbox_option, null);
			optionsLayout.addView(singleOption);

			scrollDown();
		}
	};
	/**
	 * Parse task type fields from the inputs set by user
	 * @return parsed task type field list
	 * @throws GrouponException
	 */
	public List<TaskTypeField> getTaskTypeFields() throws GrouponException {
		List<TaskTypeField> taskTypeFields = new ArrayList<TaskTypeField>();

		for (int i = 0; i < formFieldsLayout.getChildCount(); i++) {
			RelativeLayout layout = (RelativeLayout) formFieldsLayout.getChildAt(i);

			TaskTypeField taskTypeField = new TaskTypeField();

			EditText questionTitle = (EditText) layout.findViewById(R.id.custom_fieldtype_question_edittext);
			String fieldName = questionTitle.getText().toString();

			if (!StringUtils.isNotBlank(fieldName)) {
				throw new GrouponException("A question cannot be empty!");
			}

			taskTypeField.setName(fieldName);

			FieldType fieldType = fieldTypes.get(i);
			taskTypeField.setFieldType(fieldType);

			if (fieldType == FieldType.SHORT_TEXT) {
				// Nothing to do for now
			} else if (fieldType == FieldType.DATE) {
				// Nothing to do for now
			} else if (fieldType == FieldType.SELECT || fieldType == FieldType.RADIO || fieldType == FieldType.CHECKBOX) {
				LinearLayout optionsLayout = (LinearLayout) layout.findViewById(R.id.custom_fieldtype_options_layout);
				List<FieldAttribute> fieldAttributes = getFieldAttributes(optionsLayout, taskTypeField);
				taskTypeField.setAttributes(fieldAttributes);
			}

			taskTypeFields.add(taskTypeField);
		}

		return taskTypeFields;
	}
	/**
	 * Parse list of field attributes of view types with options such as checbox select and radio
	 * 
	 * @param optionsLayout option layout parsed
	 * @param taskTypeField	associated task type fields of field attributes
	 * @return list of field Attributes parsed
	 * @throws GrouponException
	 */
	private List<FieldAttribute> getFieldAttributes(LinearLayout optionsLayout, TaskTypeField taskTypeField) throws GrouponException {
		List<FieldAttribute> fieldAttributes = new ArrayList<FieldAttribute>();

		for (int j = 0; j < optionsLayout.getChildCount(); j++) {
			ViewGroup optionLayout = (ViewGroup) optionsLayout.getChildAt(j);
			EditText option = (EditText) optionLayout.findViewById(R.id.custom_fieldtype_option_value);
			String value = option.getText().toString();

			if (!StringUtils.isNotBlank(value)) {
				throw new GrouponException("An option cannot be empty!");
			}

			FieldAttribute fieldAttribute = new FieldAttribute();
			fieldAttribute.setName("option" + j);
			fieldAttribute.setValue(value);
			fieldAttribute.setTaskTypeField(taskTypeField);
			fieldAttributes.add(fieldAttribute);
		}

		return fieldAttributes;
	}

	private int pxFromDp(float dp) {
		return (int) (dp * getActivity().getResources().getDisplayMetrics().density);
	}

	private void scrollDown() {
		getView().post(new Runnable() {
			public void run() {
				((ScrollView) getView()).fullScroll(ScrollView.FOCUS_DOWN);
			}
		});
	}
}
