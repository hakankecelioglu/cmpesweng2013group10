package com.groupon.mobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

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

			if (DummyController.admin.getUsername().equals(username) && DummyController.admin.getPassword().equals(password)) {
				GrouponApplication app = (GrouponApplication) getApplication();
				app.setLoggedUser(DummyController.admin);
				Intent intent = new Intent(MainActivity.this, HomeActivity.class);
				startActivity(intent);
				finish();
			} else {
				Toast.makeText(MainActivity.this, "Wrong username or password!", Toast.LENGTH_SHORT).show();
			}
		}
	};
}
