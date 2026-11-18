package com.example.thuchanhapp3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends BaseActivity {
    private DatabaseHelper database;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        database = new DatabaseHelper(this);
        EditText username = findViewById(R.id.username);
        EditText password = findViewById(R.id.password);
        TextView error = findViewById(R.id.login_error);
        findViewById(R.id.login_button).setOnClickListener(view -> {
            String name = username.getText().toString().trim();
            String secret = password.getText().toString();
            error.setVisibility(View.GONE);
            if (name.isEmpty() || secret.trim().isEmpty()) {
                if (name.isEmpty()) username.setError(getString(R.string.required_username));
                if (secret.trim().isEmpty()) password.setError(getString(R.string.required_password));
                error.setText(R.string.login_required);
                error.setVisibility(View.VISIBLE);
                return;
            }
            if (database.checkLogin(name, secret)) {
                startActivity(new Intent(this, MainActivity.class));
                finish();
            } else {
                error.setText(R.string.login_invalid);
                error.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override protected void onDestroy() {
        database.close();
        super.onDestroy();
    }
}
