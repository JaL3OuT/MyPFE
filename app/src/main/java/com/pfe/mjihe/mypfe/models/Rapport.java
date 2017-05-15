package com.pfe.mjihe.mypfe.models;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Mjihe on 12/05/2017.
 */

public class Rapport {

    String uid;
    String url;
    String rapport;
    LatLng location;

    public Rapport(String uid, String url, String rapport, LatLng location) {
        this.uid = uid;
        this.url = url;
        this.rapport = rapport;
        this.location = location;
    }

    public Rapport(String url, String rapport, LatLng location) {
        this.url = url;
        this.rapport = rapport;
        this.location = location;
    }

    public String getRapport() {
        return rapport;
    }

    public void setRapport(String rapport) {
        this.rapport = rapport;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }
}
