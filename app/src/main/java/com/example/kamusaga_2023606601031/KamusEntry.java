package com.example.kamusaga_2023606601031;

import com.google.gson.annotations.SerializedName;

public class KamusEntry {

    @SerializedName("id")
    private int id;

    @SerializedName("bahasa_indo")
    private String bahasaIndo;

    @SerializedName("bahasa_inggris")
    private String bahasaInggris;

    @SerializedName("bahasa_korea")
    private String bahasaKorea;

    // Getter & Setter
    public int getId() {
        return id;
    }

    public String getBahasaIndo() {
        return bahasaIndo;
    }

    public void setBahasaIndo(String bahasaIndo) {
        this.bahasaIndo = bahasaIndo;
    }

    public String getBahasaInggris() {
        return bahasaInggris;
    }

    public void setBahasaInggris(String bahasaInggris) {
        this.bahasaInggris = bahasaInggris;
    }

    public String getBahasaKorea() {
        return bahasaKorea;
    }

    public void setBahasaKorea(String bahasaKorea) {
        this.bahasaKorea = bahasaKorea;
    }
}