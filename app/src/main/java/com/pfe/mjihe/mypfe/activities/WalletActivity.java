package com.pfe.mjihe.mypfe.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pfe.mjihe.mypfe.R;
import com.pfe.mjihe.mypfe.fragments.WalletFragment;
import com.pfe.mjihe.mypfe.models.User;
import com.pfe.mjihe.mypfe.models.Wallet;

public class WalletActivity extends Activity implements View.OnClickListener {
    private String sommeWallet;
    private Button recharge, sup;
    private TextView msg;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        initinstance();
        initView();
    }

    public void onClick(View v) {
        User u = new User();
        Wallet w = new Wallet();
        u.setWallet(w);
        mRef.child("user").child(mUser.getUid()).child("wallet").setValue(w).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                startActivity(new Intent(WalletActivity.this, WalletFragment.class));
                finish();
            }
        });
    }

    private void initinstance() {
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();
    }

    private void initView() {
        recharge = (Button) findViewById(R.id.recharge);
        sup = (Button) findViewById(R.id.supprimer);
        msg = (TextView) findViewById(R.id.msg);
        sup.setOnClickListener(this);

        mRef.child("user").child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                sommeWallet = dataSnapshot.child("wallet").child("solde").getValue().toString();
                msg.setText(sommeWallet);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
