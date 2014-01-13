package com.groupon.mobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.groupon.mobile.conn.GrouponCallback;
import com.groupon.mobile.model.User;
import com.groupon.mobile.service.UserService;
/**
 * Activity that provides sign up screen.
 * @author seyma
 *
 */
public class SignUpActivity extends BaseActivity {

	private Button signUpButton;
	private EditText userNameField;
	private EditText passwordField;
	private EditText emailField;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);

		setupUI();
	}
	/**
	 * setup UI of this activity.
	 */
	private void setupUI() {
		userNameField = (EditText) findViewById(R.id.user_name);
		passwordField = (EditText) findViewById(R.id.user_password);
		emailField = (EditText) findViewById(R.id.e_mail);
		signUpButton = (Button) findViewById(R.id.button_sign_up);
		signUpButton.setOnClickListener(signUpListener);
	}
	/**
	 * Gets user name password and email and make a signup request to server. 
	 */
	private OnClickListener signUpListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			String username = userNameField.getText().toString();
			String password = passwordField.getText().toString();
			String email = emailField.getText().toString();

			if (username.trim().equals("") || password.trim().equals("") || email.trim().equals("")) {
				Toast.makeText(SignUpActivity.this, "Username, password and e-mail address cannot be empty!", Toast.LENGTH_SHORT).show();
				return;
			}

			UserService userService = new UserService(getApp());
			userService.signup(username, password, email, new GrouponCallback<User>() {
				public void onSuccess(User response) {
					setLoggedUser(response);

					Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
					startActivity(intent);
					finish();
				}

				public void onFail(String errorMessage) {
					Toast.makeText(SignUpActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
				}
			});
		}
	};

}
