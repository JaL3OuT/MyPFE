package com.pfe.mjihe.mypfe.admin;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

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
import com.pfe.mjihe.mypfe.models.Lot;
import com.pfe.mjihe.mypfe.models.User;

public class Ajoutlot extends Activity implements View.OnClickListener {
    private EditText numlot, cin, lat, lang, taxe;
    private Spinner payement;
    private Button btnajout;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private Lot mlot;
    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajoutlot);
        initFirebase();
        intview();
    }

    private void intview() {
        numlot = (EditText) findViewById(R.id.alot);
        cin = (EditText) findViewById(R.id.cin);
        lat = (EditText) findViewById(R.id.aLatlot);
        lang = (EditText) findViewById(R.id.ajouterLangLot);
        taxe = (EditText) findViewById(R.id.ajoutertaxe);
        payement = (Spinner) findViewById(R.id.payement);
        btnajout = (Button) findViewById(R.id.btnajout);
        btnajout.setOnClickListener(this);
        mDialog = new ProgressDialog(this);
        mDialog.setTitle("Chargment");
        mDialog.setMessage("Attendez SVP !!");
    }
    @Override
    public void onClick(View v) {
        if (!isEmpty()) {
            mDialog.show();
            boolean pay = false;
            if (payement.getSelectedItem().toString() == "non payer") {
                pay = false;
            }
            if (payement.getSelectedItem().toString() == "payer") {
                pay = true;
            }
            mlot = new Lot(cin.getText().toString(), numlot.getText().toString(), Double.valueOf(String.valueOf(lat.getText())),
                    Double.valueOf(String.valueOf(lang.getText())), Double.valueOf(String.valueOf(taxe.getText())), pay);

            mRef.child("user").child(mUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    String gov = user.getGouvernorat();
                    String comun = user.getComunn();
                    mRef.child("Region").child(gov).child(comun).child("Lot").child(numlot.getText().toString()).setValue(mlot).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            mDialog.dismiss();
                            Toast.makeText(Ajoutlot.this, "lot ajouter", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
    }


    private void initFirebase() {
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
    }

    private boolean isEmpty() {
        boolean empty = false;
        if (TextUtils.isEmpty(numlot.getText())) {
            numlot.setError("champ vide");
            empty = true;
        }
        if (TextUtils.isEmpty(cin.getText())) {
            cin.setError("champ vide");
            empty = true;
        }
        if (TextUtils.isEmpty(lat.getText())) {
            lat.setError("champ vide");
            empty = true;
        }
        if (TextUtils.isEmpty(lang.getText())) {
            lang.setError("champ vide");
            empty = true;
        }
        if (TextUtils.isEmpty(taxe.getText())) {
            taxe.setError("champ vide");
            empty = true;
        }


        return empty;
    }
}
