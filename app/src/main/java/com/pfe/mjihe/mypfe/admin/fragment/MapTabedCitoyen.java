package com.pfe.mjihe.mypfe.admin.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pfe.mjihe.mypfe.R;
import com.pfe.mjihe.mypfe.adapters.LotAdapter;
import com.pfe.mjihe.mypfe.admin.Ajoutlot;
import com.pfe.mjihe.mypfe.admin.mapLotActivity;
import com.pfe.mjihe.mypfe.models.Lot;
import com.pfe.mjihe.mypfe.models.User;
import com.pfe.mjihe.mypfe.utils.ItemClickSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapTabedCitoyen extends Fragment {
    private String gov, comun;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private View rootview;
    private Button aCarte, ajouLot;
    private FloatingActionButton nottifier;
    private RecyclerView recyclerlot;
    private LotAdapter mlotAdapter;
    private List<Lot> mLotList = new ArrayList<>();

    public MapTabedCitoyen() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.fragment_map_tabed_citoyen, container, false);
        initView();
        getadressAdmin();
        return rootview;
    }

    private void initView() {
        aCarte = (Button) rootview.findViewById(R.id.mapLot);
        aCarte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1 = new Intent(getActivity(), mapLotActivity.class);
                startActivity(i1);
            }
        });
        ajouLot = (Button) rootview.findViewById(R.id.ajoutLot);
        ajouLot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), Ajoutlot.class);
                startActivity(i);
            }
        });
        mlotAdapter = new LotAdapter(mLotList);
        nottifier = (FloatingActionButton) rootview.findViewById(R.id.notiffier);
        recyclerlot = (RecyclerView) rootview.findViewById(R.id.recycler_lot);
        RecyclerView.LayoutManager mLayoutmanager = new LinearLayoutManager(getActivity());
        recyclerlot.setLayoutManager(mLayoutmanager);
        recyclerlot.setItemAnimator(new DefaultItemAnimator());
        recyclerlot.setAdapter(mlotAdapter);
        ItemClickSupport.addTo(recyclerlot).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Toast.makeText(getActivity(), String.valueOf(position), Toast.LENGTH_SHORT).show();
                // do it
            }
        });
    }

    private void initFirebase() {
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
    }

    public void getadressAdmin() {
        initFirebase();
        mRef.child("user").child(mUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                gov = user.getGouvernorat();
                comun = user.getComunn();
                lotData();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void lotData() {
        initFirebase();
        mLotList.removeAll(mLotList);
        mRef.child("Region").child(gov).child(comun).child("Lot").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot lotSnapshot : dataSnapshot.getChildren()) {
                    Lot mlot = lotSnapshot.getValue(Lot.class);
                    mLotList.add(mlot);
                    Log.e("TAG", "onDataChange: " + mLotList.size());
                }
                mlotAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
}
}
