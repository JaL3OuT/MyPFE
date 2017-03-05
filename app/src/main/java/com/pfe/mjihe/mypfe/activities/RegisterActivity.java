package com.pfe.mjihe.mypfe.activities;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pfe.mjihe.mypfe.R;
import com.pfe.mjihe.mypfe.models.User;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private Button bRegister;

    private EditText nom;
    private EditText prenom;
    private EditText email;
    private EditText password;
    private EditText repassword;
    private Spinner gov;

    private FirebaseAuth mAuth;
    private DatabaseReference mRef;

    private ProgressDialog  mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        initInstance();
        initListener();
    }

    private void initListener() {
        bRegister.setOnClickListener(this);
    }

    private void initInstance() {
        mAuth= FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference();
        mDialog = new ProgressDialog(this);
        mDialog.setTitle("Chargment");
        mDialog.setMessage("Attendez SVP !!");
    }

    private void initView() {
        nom = (EditText) findViewById(R.id.nom);
        prenom = (EditText) findViewById(R.id.prenom);
        password = (EditText) findViewById(R.id.password);
        repassword = (EditText) findViewById(R.id.repassword);
        email = (EditText) findViewById(R.id.email);
        bRegister = (Button) findViewById(R.id.register);
        gov = (Spinner) findViewById(R.id.gov);
    }

    @Override
    public void onClick(View v) {
        if (!isEmpty()){
            mDialog.show();
            createUser(email.getText().toString(), password.getText().toString());
        }
    }

    private boolean isEmpty(){
        boolean empty = false;

        if (TextUtils.isEmpty(nom.getText())){
            nom.setError("Champs Vide");
            empty = true;
        }

        if (TextUtils.isEmpty(prenom.getText())){
            prenom.setError("Champs Vide");
            empty = true;
        }

        if (TextUtils.isEmpty(email.getText())){
            email.setError("Champs Vide");
            empty = true;
        }

        if (TextUtils.isEmpty(password.getText())){
            password.setError("Champs Vide");
            empty = true;
        }

        if (TextUtils.isEmpty(repassword.getText())){
            repassword.setError("Champs Vide");
            empty = true;
        }

        return empty;
    }

    private void createUser(final String email, final String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Log.e("Done", authResult.toString());
                        addUser(authResult.getUser().getUid());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Not DOne", e.toString());
                    }
                });
    }

    private void addUser(String uid) {
        User u = new User();
        u.setEmail(email.getText().toString());
        u.setGouvernorat(gov.getSelectedItem().toString());
        u.setNom(nom.getText().toString());
        u.setPrenom(prenom.getText().toString());
        mRef.child("user").child(uid).setValue(u).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                mDialog.dismiss();
                Toast.makeText(RegisterActivity.this, task.isSuccessful()+"", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
