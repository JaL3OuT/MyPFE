package com.pfe.mjihe.mypfe.admin.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pfe.mjihe.mypfe.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapTabedCitoyen extends Fragment {


    public MapTabedCitoyen() {


        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map_tabed_citoyen, container, false);
    }

}