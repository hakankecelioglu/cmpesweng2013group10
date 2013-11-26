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

public class MainActivity extends BaseActivity {

	private Button loginButton;
	private EditText loginUsername;
	private EditText loginPassword;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		GrouponApplication application = (GrouponApplication) getApplication();
		if (application.getLoggedUser() != null) {
			Intent intent = new Intent(MainActivity.this, HomeActivity.class);
			startActivity(intent);
			finish();
		}

		setContentView(R.layout.home_guest);

		setupUI();
	}

	private void setupUI() {
		loginButton = (Button) findViewById(R.id.button_login);
		loginButton.setOnClickListener(onLoginButtonClick);

		loginUsername = (EditText) findViewById(R.id.login_username);
		loginPassword = (EditText) findViewById(R.id.login_password);
	}

	private OnClickListener onLoginButtonClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			String username = loginUsername.getText().toString();
			String password = loginPassword.getText().toString();

			UserService userService = new UserService(getApp());
			userService.login(username, password, new GrouponCallback<User>() {
				public void onSuccess(User response) {
					setLoggedUser(response);

					Intent intent = new Intent(MainActivity.this, HomeActivity.class);
					startActivity(intent);
					finish();
				}

				public void onFail(String errorMessage) {
					Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
				}
			});
		}
	};
}
