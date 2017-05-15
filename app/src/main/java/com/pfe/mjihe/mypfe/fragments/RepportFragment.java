package com.pfe.mjihe.mypfe.fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pfe.mjihe.mypfe.R;
import com.pfe.mjihe.mypfe.models.Rapport;

import java.io.ByteArrayOutputStream;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class RepportFragment extends Fragment implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int PERMISSION_REQUEST_CODE = 200;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private StorageReference mStorageReference;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private ImageView apercu;
    private Button envoyer, prendre, importer;
    private EditText rapport;
    private View rootview;
    private LatLng loc;

    public RepportFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        initializeGoogleClient();
        rootview = inflater.inflate(R.layout.fragment_repport, container, false);
        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                        PackageManager.PERMISSION_GRANTED) {
            requestPermission();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                            android.Manifest.permission.CAMERA,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        }
        initview();
        initListener();


        return rootview;
    }

    private void initListener() {
        envoyer.setOnClickListener(this);
        prendre.setOnClickListener(this);
        importer.setOnClickListener(this);
    }


    private void initview() {
        apercu = (ImageView) rootview.findViewById(R.id.imageView);
        envoyer = (Button) rootview.findViewById(R.id.envoyer);
        prendre = (Button) rootview.findViewById(R.id.prendrephoto);
        importer = (Button) rootview.findViewById(R.id.importerphoto);
        rapport = (EditText) rootview.findViewById(R.id.textrapport);

    }

    private void envoyerRapport() {
        initFirebase();
        final ProgressDialog mDialog = new ProgressDialog(getActivity());
        mDialog.setTitle("Chargment");
        mDialog.setMessage("Attendez SVP !!");
        //Log.d("HelloTag", "envoyerRapport: " + ref.getPath());
        apercu.setDrawingCacheEnabled(true);
        apercu.buildDrawingCache();
        Bitmap mp = apercu.getDrawingCache();
        StorageReference ref = mStorageReference.child("Rapport").child(mUser.getUid()).child(String.valueOf(System.currentTimeMillis() / 1000));
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        mp.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        UploadTask task = ref.putBytes(byteArrayOutputStream.toByteArray());
        task.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d("HelloUrl", "onSuccess: " + taskSnapshot.getDownloadUrl());
                Rapport rp = new Rapport(taskSnapshot.getDownloadUrl().toString(), rapport.getText().toString(), loc);
                //String key = mRef.child("Rapport").child(mUser.getUid()).getKey();
                mRef.child("Rapport").child(mUser.getUid()).child(String.valueOf(System.currentTimeMillis() / 1000)).setValue(rp).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        mDialog.dismiss();
                        Toast.makeText(getActivity(), "Rapport added !", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }

    public void initializeGoogleClient() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    void prendrePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
    }

    private void importerPhoto() {
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.prendrephoto:
                prendrePhoto();
                break;
            case R.id.envoyer:
                envoyerRapport();
                break;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            apercu.setImageBitmap(imageBitmap);
        }
    }

    public void requestPermission() {

        ActivityCompat.requestPermissions(getActivity(), new String[]{
                ACCESS_FINE_LOCATION,
                CAMERA,
        }, PERMISSION_REQUEST_CODE);
    }

    private void initFirebase() {
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mStorageReference = FirebaseStorage.getInstance("gs://jipfe-6c4b8.appspot.com").getReference();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode > PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {
                boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                if (locationAccepted && cameraAccepted) {
                    Toast.makeText(getActivity(), "Hello World", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            //Log.e("TAG", "onConnected: " + String.valueOf(mLastLocation.getLatitude()));
            loc = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
        } else {
            initGps();
            Toast.makeText(getActivity(), "No Location Found", Toast.LENGTH_SHORT).show();
            initGps();

        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("HelloUse", "onConnectionFailed: " + connectionResult.getErrorMessage());
    }

    void initGps() {
        LocationManager service = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        boolean enabled = service
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!enabled) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }
    }
}