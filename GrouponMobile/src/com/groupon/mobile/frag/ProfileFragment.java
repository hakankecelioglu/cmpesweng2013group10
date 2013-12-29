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
import android.widget.Toast;

import com.groupon.mobile.GrouponApplication;
import com.groupon.mobile.R;
import com.groupon.mobile.conn.GrouponCallback;
import com.groupon.mobile.model.User;
import com.groupon.mobile.service.UserService;
import com.groupon.mobile.utils.StringUtils;

public class ProfileFragment extends Fragment {
	private boolean isInEditMode = false;
	private Long profileOwnerId = -1L;

	private Button editProfileButton;
	private TextView usernameTextView;

	private TextView nameTextView;
	private TextView surnameTextView;
	private TextView emailTextView;
	private TextView reputationTextView;

	private EditText nameEditText;
	private EditText surnameEditText;

	private GrouponApplication app;
	private User user;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.profile, container, false);
		app = (GrouponApplication) getActivity().getApplication();
		user = app.getLoggedUser();

		Bundle bundle = getArguments();
		if (bundle != null) {
			profileOwnerId = bundle.getLong("userId", -1);
		}

		setupUI(rootView);
		setData();
		return rootView;
	}

	public void setupUI(View rootView) {
		usernameTextView = (TextView) rootView.findViewById(R.id.profile_username);
		editProfileButton = (Button) rootView.findViewById(R.id.profile_edit_button);
		nameTextView = (TextView) rootView.findViewById(R.id.profile_edit_text_name);
		surnameTextView = (TextView) rootView.findViewById(R.id.profile_edit_text_surname);
		emailTextView = (TextView) rootView.findViewById(R.id.profile_edit_text_email);
		reputationTextView = (TextView) rootView.findViewById(R.id.profile_edit_text_reput);

		nameEditText = (EditText) rootView.findViewById(R.id.profile_edit_text_name_edit);
		surnameEditText = (EditText) rootView.findViewById(R.id.profile_edit_text_surname_edit);

		editProfileButton.setOnClickListener(editButtonClickListener);

		if (!profileOwnerId.equals(-1L) && !user.getId().equals(profileOwnerId)) {
			editProfileButton.setVisibility(View.GONE);
		}
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

		editProfileButton.setText(getString(R.string.profile_label_save_profile));
	}

	private void convertEditTextsToTextViews() {
		nameTextView.setVisibility(View.VISIBLE);
		nameEditText.setVisibility(View.GONE);

		surnameTextView.setVisibility(View.VISIBLE);
		surnameEditText.setVisibility(View.GONE);

		editProfileButton.setText(getString(R.string.profile_label_edit_profile));
	}

	private void saveProfileInfoChanges() {
		String name = nameEditText.getText().toString();
		String surname = surnameEditText.getText().toString();

		if (StringUtils.isNotBlank(name)) {
			nameTextView.setText(name);
		} else {
			nameTextView.setText(" - ");
		}

		if (StringUtils.isNotBlank(surname)) {
			surnameTextView.setText(surname);
		} else {
			surnameTextView.setText(" - ");
		}
	}

	private void setData() {
		if (profileOwnerId.equals(-1L) || user.getId().equals(profileOwnerId)) {
			// My profile
			putUserToView(user);
		} else {
			// Someone's profile
			UserService service = new UserService(app);
			service.getUserInfo(profileOwnerId, new GrouponCallback<User>() {
				public void onSuccess(User response) {
					putUserToView(response);
				}

				public void onFail(String errorMessage) {
					Toast.makeText(app.getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
				}
			});
		}
	}

	private void putUserToView(User user) {
		usernameTextView.setText(user.getUsername());
		emailTextView.setText(user.getEmail());

		if (user.getName() != null) {
			nameTextView.setText(user.getName());
			nameEditText.setText(user.getName());
		} else {
			nameTextView.setText(" - ");
			nameEditText.setText("");
		}

		if (user.getSurname() != null) {
			surnameTextView.setText(user.getSurname());
			surnameEditText.setText(user.getSurname());
		} else {
			surnameTextView.setText(" - ");
			surnameEditText.setText("");
		}

		reputationTextView.setText("0");
	}

}
