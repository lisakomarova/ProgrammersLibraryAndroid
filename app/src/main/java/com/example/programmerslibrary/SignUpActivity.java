package com.example.programmerslibrary;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.programmerslibrary.Utils.CustomTextWatcher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passEditText;
    private Button signInButton;
    private Button signUpButton;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        usernameEditText = findViewById(R.id.username);
        passEditText = findViewById(R.id.password);
        signInButton = findViewById(R.id.button_signIn);
        signUpButton = findViewById(R.id.button_signUp);

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null){
            // авторизирован
            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }

        signInButton = findViewById(R.id.button_signIn);
        signUpButton = findViewById(R.id.button_signUp);
        signInButton.setEnabled(false);
        signUpButton.setEnabled(false);

        EditText[] edList = {usernameEditText, passEditText};
        CustomTextWatcher textWatcher = new CustomTextWatcher(edList, signInButton, signUpButton);
        for (EditText editText : edList) editText.addTextChangedListener(textWatcher);
    }
    public void singin_onClick(View view){
        mAuth.signInWithEmailAndPassword(usernameEditText.getText().toString(), passEditText.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
                else
                    Toast.makeText(SignUpActivity.this, "Wrong Login or Password", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void register_onClick(View view){
        mAuth.createUserWithEmailAndPassword(usernameEditText.getText().toString(), passEditText.getText().toString()).addOnCompleteListener(this, task -> {
            if(task.isSuccessful())
            {
                // переходим для дальнейшей регистрации
                FirebaseUser user = mAuth.getCurrentUser();
                if(user != null){
                    // авторизирован
                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            }
            else
                Toast.makeText(SignUpActivity.this, "User is already exists or invalid password or login", Toast.LENGTH_SHORT).show();
        });
    }
}
