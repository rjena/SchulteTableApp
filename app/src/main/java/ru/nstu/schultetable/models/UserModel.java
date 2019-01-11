package ru.nstu.schultetable.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserModel {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("login")
    @Expose
    private String login;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("bday")
    @Expose
    private String bday;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("tokenToConfirmEmail")
    @Expose
    private String tokenToConfirmEmail;
    @SerializedName("tokenToResetPassword")
    @Expose
    private String tokenToResetPassword;


    public String getUserId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String username) {
        this.login = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBday() {
        return bday;
    }

    public void setBday(String bday) {
        this.bday = bday;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTokenToConfirmEmail() {
        return tokenToConfirmEmail;
    }

    public void setTokenToConfirmEmail(String tokenToConfirmEmail) {
        this.tokenToConfirmEmail = tokenToConfirmEmail;
    }

    public String getTokenToResetPassword() {
        return tokenToResetPassword;
    }

    public void setTokenToResetPassword(String tokenToResetPassword) {
        this.tokenToResetPassword = tokenToResetPassword;
    }
}
