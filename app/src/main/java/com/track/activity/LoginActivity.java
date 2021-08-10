package com.track.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.track.R;
import com.track.function.NetworkingFunction;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText emailEditText, passwordEditText;
    private Button loginButton, regButton;
    private Activity tActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        tActivity = LoginActivity.this;
        emailEditText = findViewById(R.id.email_editText);
        passwordEditText = findViewById(R.id.password_editText);
        loginButton = findViewById(R.id.login_button);
        regButton = findViewById(R.id.reg_button);
        loginButton.setOnClickListener(this);
        regButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.login_button:
                if (TextUtils.isEmpty(emailEditText.getText().toString())) {
                    emailEditText.setError("field empty");
                } else if (TextUtils.isEmpty(passwordEditText.getText().toString())) {
                    passwordEditText.setError("field empty");
                } else {
                    new NetworkingFunction(tActivity).loginUser(
                            emailEditText.getText().toString(),
                            passwordEditText.getText().toString()
                    );
                }
                break;

            case R.id.reg_button:
                startActivity(new Intent(tActivity, RegisterActivity.class));
                finish();
                break;
        }
    }
}
