package com.pfe.mjihe.mypfe.models;

/**
 * Created by Mjihe on 31/05/2017.
 */

public class Municipalite {
    private String nomM, logo, info;
    private double latm, langm;

    public Municipalite(String nomM, String logo, String info, String coordoné, double latm, double langm) {
        this.nomM = nomM;
        this.logo = logo;
        this.info = info;
        this.latm = latm;
        this.langm = langm;
    }

    public Municipalite() {

    }

    public String getNomM() {
        return nomM;
    }

    public void setNomM(String nomM) {
        this.nomM = nomM;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }


    public double getLatm() {
        return latm;
    }

    public void setLatm(double latm) {
        this.latm = latm;
    }

    public double getLangm() {
        return langm;
    }

    public void setLangm(double langm) {
        this.langm = langm;
    }
}
