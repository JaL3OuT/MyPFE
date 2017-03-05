package com.pfe.mjihe.mypfe.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.pfe.mjihe.mypfe.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private Button bLogin;
    private Button bRegister;
    private TextView tEmail;
    private TextView tPassword;

    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initInstance();
        initListener();
    }

    private void initListener() {
        bLogin.setOnClickListener(this);
        bRegister.setOnClickListener(this);
    }

    private void initView() {
        tEmail = (TextView) findViewById(R.id.email);
        tPassword = (TextView) findViewById(R.id.password);
        bLogin = (Button) findViewById(R.id.login);
        bRegister = (Button) findViewById(R.id.register);
    }

    private void initInstance() {
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null){
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

        mDialog = new ProgressDialog(this);
        mDialog.setTitle("Chargment");
        mDialog.setMessage("Attendez SVP !!");
    }

    private boolean isEmpty() {
        boolean empty = false;
        if (TextUtils.isEmpty(tEmail.getText())) {
            tEmail.setError("Champs Vide");
            empty = true;
        }

        if (TextUtils.isEmpty(tPassword.getText())) {
            tPassword.setError("Champs Vide");
            empty = true;
        }
        return empty;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.register){
          startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        }else{
            if(!isEmpty())
                mDialog.show();
            mAuth.signInWithEmailAndPassword(tEmail.getText().toString(), tPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        mDialog.dismiss();
                        Toast.makeText(LoginActivity.this, "Loged IN", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }else{
                        Toast.makeText(LoginActivity.this, "Email ou password erronée", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
