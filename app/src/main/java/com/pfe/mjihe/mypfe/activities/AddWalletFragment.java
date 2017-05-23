package com.pfe.mjihe.mypfe.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pfe.mjihe.mypfe.R;
import com.pfe.mjihe.mypfe.models.Wallet;

public class AddWalletFragment extends Activity {

    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private EditText edinar;
    private EditText pin;
    private EditText cin;
    private Button addWallet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_wallet_fragment);
        initView();
        initFirebase();
    }

    private void initFirebase() {
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
    }

    private void initView() {
        edinar = (EditText) findViewById(R.id.edinar);
        pin = (EditText) findViewById(R.id.pin);
        cin = (EditText) findViewById(R.id.cin);
        addWallet = (Button) findViewById(R.id.add);
        addWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add();
            }
        });

        pin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //TODO : add checked
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void add() {
        if (TextUtils.isEmpty(edinar.getText())) {
            edinar.setError("Empty Field");
            return;
        }

        if (TextUtils.isEmpty(cin.getText())) {
            cin.setError("Empty Field");
            return;
        }

        if (TextUtils.isEmpty(pin.getText())) {
            pin.setError("Empty Field");
            return;
        }

        Wallet wallet = new Wallet();
        wallet.setCin(cin.getText().toString());
        wallet.setCodeEdinar(edinar.getText().toString());
        wallet.setPin(pin.getText().toString());
        wallet.setSolde(00.00);
        wallet.setExiste("true");
        mRef.child("user").child(mUser.getUid()).child("wallet").setValue(wallet).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(AddWalletFragment.this, "Wallet cree", Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(AddWalletFragment.this, WalletActivity.class);
                    startActivityForResult(in, 101);
                    finish();

                }
            }
        });


    }
}
