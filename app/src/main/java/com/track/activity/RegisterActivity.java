package com.track.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.track.R;
import com.track.function.NetworkingFunction;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private Activity tActivity;
    private EditText userNameEditText, emailEditText, passwordEditText, reenterPasswordEditText;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        tActivity = RegisterActivity.this;
        Toolbar toolBar = findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        setTitle("Register");
        toolBar.setNavigationIcon(R.drawable.ic_bar_cancel);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(tActivity, HomeActivity.class));
                finish();
            }
        });

        userNameEditText = findViewById(R.id.user_name_editText);
        emailEditText = findViewById(R.id.email_editText);
        passwordEditText = findViewById(R.id.password_editText);
        reenterPasswordEditText = findViewById(R.id.reenter_password_editText);
        registerButton = findViewById(R.id.register_button);
        registerButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.register_button:
                if (TextUtils.isEmpty(userNameEditText.getText().toString())) {
                    userNameEditText.setError("field empty");
                } else if (TextUtils.isEmpty(emailEditText.getText().toString())) {
                    emailEditText.setError("field empty");
                } else if (!TextUtils.equals(passwordEditText.getText().toString(), reenterPasswordEditText.getText().toString())) {
                    passwordEditText.setError("Check your password");
                    reenterPasswordEditText.setError("Password mismatch");
                } else {
                    new NetworkingFunction(tActivity).registerUser(
                            userNameEditText.getText().toString(),
                            emailEditText.getText().toString(),
                            reenterPasswordEditText.getText().toString()
                    );
                }
                break;
        }
    }
}
