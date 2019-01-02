package com.example.affereaflaw.tayo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    //private EditText inputUsername, inputPassword;
    private Button btnMasuk;
    //private TextView textDaftar, textLupaPass;
    private ProgressDialog progressDialog;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //menginstance Firebase auth
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, MainMenu.class));
            finish();
        }

        //mengatur tampilan
        setContentView(R.layout.activity_login);

        final EditText inputUsername = (EditText) findViewById(R.id.txtUsername_login);
        final EditText inputPassword = (EditText) findViewById(R.id.txtPassword_login);
        btnMasuk = (Button) findViewById(R.id.btn_Masuk);
        final TextView textDaftar = (TextView) findViewById(R.id.txtDaftar);
        final TextView textLupaPass = (TextView) findViewById(R.id.txtLupapassword);
        progressDialog = new ProgressDialog(this);

        textDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent y = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(y);
            }
        });

        textLupaPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent z = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(z);
            }
        });
        //menginstance Firebase auth
        auth = FirebaseAuth.getInstance();

        btnMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = inputUsername.getText().toString();
                String password = inputPassword.getText().toString();

                if (TextUtils.isEmpty(userName)) {
                    Toast.makeText(getApplicationContext(), "Please input Username", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Please enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressDialog.setMessage("Login...");
                progressDialog.show();

                auth.signInWithEmailAndPassword(userName, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Intent x = new Intent(LoginActivity.this, MainMenu.class);
                                    startActivity(x);
                                    finish();
                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "Login Failed, Please Check Username and Password", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }


        });

    }
}
