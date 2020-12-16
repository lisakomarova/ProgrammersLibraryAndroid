package com.example.programmerslibrary.Utils;

import android.content.DialogInterface;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.programmerslibrary.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ShieldActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shield);
        auth();
        FloatingActionButton fab = findViewById(R.id.fabkey);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth();
            }
        });
    }


    public void auth() {

        BiometricPrompt biometricPrompt = new BiometricPrompt.Builder(this).
                setTitle("Подтвердите свою личность").
                setSubtitle("Используйте биометтрические данные").
                setNegativeButton("cancel", getMainExecutor(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).build();
        biometricPrompt.authenticate(new CancellationSignal(), getMainExecutor(), new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                finish();
            }

            @Override
            public void onAuthenticationError(int errorCode, CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(ShieldActivity.this, errString, Toast.LENGTH_LONG).show();

            }
        });
    }
}
