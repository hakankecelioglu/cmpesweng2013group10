package com.groupon.mobile.frag;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.groupon.mobile.R;

public class ProfileFragment extends Fragment {
	private boolean isInEditMode = false;

	private Button editProfileButton;

	private TextView nameTextView;
	private TextView surnameTextView;
	private TextView emailTextView;
	private TextView reputationTextView;

	private EditText nameEditText;
	private EditText surnameEditText;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.profile, container, false);
		setupUI(rootView);
		return rootView;
	}

	public void setupUI(View rootView) {
		editProfileButton = (Button) rootView.findViewById(R.id.profile_edit_button);
		nameTextView = (TextView) rootView.findViewById(R.id.profile_edit_text_name);
		surnameTextView = (TextView) rootView.findViewById(R.id.profile_edit_text_surname);
		emailTextView = (TextView) rootView.findViewById(R.id.profile_edit_text_email);
		reputationTextView = (TextView) rootView.findViewById(R.id.profile_edit_text_reput);

		nameEditText = (EditText) rootView.findViewById(R.id.profile_edit_text_name_edit);
		surnameEditText = (EditText) rootView.findViewById(R.id.profile_edit_text_surname_edit);

		editProfileButton.setOnClickListener(editButtonClickListener);
	}

	private OnClickListener editButtonClickListener = new OnClickListener() {
		public void onClick(View v) {
			if (isInEditMode) {
				saveProfileInfoChanges();
				convertEditTextsToTextViews();
				isInEditMode = false;
			} else {
				convertTextViewsToEditTexts();
				isInEditMode = true;
			}
		}
	};

	private void convertTextViewsToEditTexts() {
		nameTextView.setVisibility(View.GONE);
		nameEditText.setVisibility(View.VISIBLE);

		surnameTextView.setVisibility(View.GONE);
		surnameEditText.setVisibility(View.VISIBLE);

		editProfileButton.setText("Save");
	}

	private void convertEditTextsToTextViews() {
		nameTextView.setVisibility(View.VISIBLE);
		nameEditText.setVisibility(View.GONE);

		surnameTextView.setVisibility(View.VISIBLE);
		surnameEditText.setVisibility(View.GONE);

		editProfileButton.setText("Edit Profile");
	}

	private void saveProfileInfoChanges() {
		nameTextView.setText(nameEditText.getText().toString());
		surnameTextView.setText(surnameEditText.getText().toString());
	}

}
