package ru.nstu.schultetable.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TestModel {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("stWE")
    @Expose
    private double stWE;
    @SerializedName("stWU")
    @Expose
    private double stWU;
    @SerializedName("stPS")
    @Expose
    private double stPS;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("age")
    @Expose
    private int age;

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public double getStWE() {
        return stWE;
    }

    public void setStWE(double we) {
        this.stWE = we;
    }

    public double getStWU() {
        return stWU;
    }

    public void setStWU(double wu) {
        this.stWU = wu;
    }

    public double getStPS() {
        return stPS;
    }

    public void setStPS(double ps) {
        this.stPS = ps;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
