package com.pfe.mjihe.mypfe.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pfe.mjihe.mypfe.R;
import com.pfe.mjihe.mypfe.activities.AddWalletFragment;

public class WalletFragment extends Fragment {

    String nom;
    String email;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private TextView msg;
    private FloatingActionButton actionButton;
    private View rootView;
    private String testexist;
    private String sommeWallet;


    public WalletFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_wallet, container, false);
        initinstance();

        return rootView;
    }

    private void initView() {
        msg = (TextView) rootView.findViewById(R.id.msg);
        actionButton = (FloatingActionButton) rootView.findViewById(R.id.addWallet);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getActivity(), AddWalletFragment.class);
                startActivityForResult(in, 101);
            }
        });
    }

    private void initView2() {
        msg = (TextView) rootView.findViewById(R.id.msg);
        actionButton = (FloatingActionButton) rootView.findViewById(R.id.addWallet);
        actionButton.setVisibility(View.INVISIBLE);
        msg = (TextView) rootView.findViewById(R.id.msg);
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

    private void initinstance() {
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();
        mRef.child("user").child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                testexist = dataSnapshot.child("wallet").child("existe").getValue().toString();
                Log.e("connect", "onDataChange: " + dataSnapshot.child("wallet").child("existe").getValue().toString());
                if (testexist == "true") {
                    initView2();
                } else {
                    initView();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 101) {

        }
    }
}
