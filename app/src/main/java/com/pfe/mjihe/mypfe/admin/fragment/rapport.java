package com.pfe.mjihe.mypfe.admin.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pfe.mjihe.mypfe.R;
import com.pfe.mjihe.mypfe.models.Rapport;
import com.pfe.mjihe.mypfe.models.User;
import com.pfe.mjihe.mypfe.utils.ItemClickSupport;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class rapport extends Fragment {
    SharedPreferences prefra;
    String gov, local, comun;
    private SharedPreferences.Editor edit22, edit33, edit11;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private View rootview;
    private RecyclerView recycler;
    private RapportAdapter mAdapter;
    private List<Rapport> mRapportList = new ArrayList<>();

    public rapport() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.content_raport, container, false);
        initView();
        initFirebase();
        getadressAdmin();

        return rootview;
    }

    private void initFirebase() {
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
    }

    private void initView() {
        recycler = (RecyclerView) rootview.findViewById(R.id.recycler_viewRapport);
        mAdapter = new RapportAdapter(mRapportList);
        RecyclerView.LayoutManager mLayoutmanager = new LinearLayoutManager(getActivity());
        recycler.setLayoutManager(mLayoutmanager);
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setAdapter(mAdapter);
        ItemClickSupport.addTo(recycler).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Toast.makeText(getActivity(), "testt ", Toast.LENGTH_SHORT).show();
                // do it
            }
        });
    }

    public void getadressAdmin() {
        mRef.child("user").child(mUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                Log.d("adress", "user1: " + mUser.getUid().toString());
                prefra = getActivity().getSharedPreferences("PREF", MODE_PRIVATE);

                edit33 = prefra.edit();
                edit22 = prefra.edit();
                edit11 = prefra.edit();
                edit11.putString("governoratr", user.getGouvernorat());
                edit22.putString("communr", user.getComunn());
                edit33.putString("localiter", user.getLocalite());
                edit11.apply();
                edit22.apply();
                edit33.apply();

                local = user.getLocalite();
                gov = user.getGouvernorat();
                comun = user.getComunn();

                rapportData();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void rapportData() {
        gov = prefra.getString("governoratr", null);
        local = prefra.getString("localiter", null);
        comun = prefra.getString("communr", null);
        initFirebase();
        mRef.child("Rapport").child(gov).child(comun).child(local).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot rapportSnapshot : dataSnapshot.getChildren()) {
                    Rapport mRapport = rapportSnapshot.getValue(Rapport.class);
                    mRapportList.add(mRapport);
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
