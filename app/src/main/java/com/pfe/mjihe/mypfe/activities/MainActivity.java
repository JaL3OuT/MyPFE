package com.pfe.mjihe.mypfe.activities;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pfe.mjihe.mypfe.R;
import com.pfe.mjihe.mypfe.fragments.FragmentMap;
import com.pfe.mjihe.mypfe.fragments.RepportFragment;
import com.pfe.mjihe.mypfe.fragments.WalletFragment;
import com.pfe.mjihe.mypfe.models.Wallet;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    static FragmentManager fm;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private FirebaseUser mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("attendez svp !!!!");
        dialog.setMessage("attendez svp !!!!");
        dialog.show();
        initInstance();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        final View v = navigationView.getHeaderView(0);
        mRef.child("user").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("TAG", "onDataChange: " + dataSnapshot.getChildrenCount());
                ((TextView) v.findViewById(R.id.headernom)).setText(dataSnapshot.child("nom").getValue().toString());
                ((TextView) v.findViewById(R.id.headermail)).setText(dataSnapshot.child("email").getValue().toString());

                dialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        navigationView.setNavigationItemSelectedListener(this);
    }

    private void initInstance() {

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();
        fm = getFragmentManager();
        mUser = mAuth.getCurrentUser();

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.logout) {
            mAuth.signOut();
            Intent in = new Intent(MainActivity.this, LoginActivity.class);
            in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(in);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.map) {
            fm.beginTransaction().replace(R.id.container, new FragmentMap()).commit();
        }

        if (id == R.id.wallet) {
            mRef.child("user").child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // String testexist = dataSnapshot.child("wallet").child("existe").getValue().toString();
                    //Log.e("connect1", "onDataChange: " + dataSnapshot.child("wallet").child("existe").getValue().toString());
                    //Log.e("connect1", "onDataChange1: " + testexist);
                    // if (testexist == "true") {
                    //   fm.beginTransaction().replace(R.id.container, new Wallet_SoldeFragment()).commit();
                    //} else {
                    //   fm.beginTransaction().replace(R.id.container, new WalletFragment()).commit();
                    //}
                    String testexist;
                    Wallet w;
                    w = dataSnapshot.child("wallet").getValue(Wallet.class);
                    testexist = w.getExiste();
                    Log.e("connect1", "onDataChange1: " + testexist);
                    switch (testexist) {
                        case "true ":
                            Log.e("connect1", "onDataChange1: " + testexist);
                            Intent in = new Intent(getApplicationContext(), WalletActivity.class);
                            startActivity(in);
                            break;
                        case "false":
                            Log.e("connect", "onDataChange3: " + dataSnapshot.child("wallet").child("existe").getValue().toString());

                            fm.beginTransaction().replace(R.id.container, new WalletFragment()).commit();
                            break;
                        case "":
                            Log.e("connect1", "onDataChange1: " + testexist);
                            break;

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });


        }
        if (id == R.id.rapport) {
            fm.beginTransaction().replace(R.id.container, new RepportFragment()).commit();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
