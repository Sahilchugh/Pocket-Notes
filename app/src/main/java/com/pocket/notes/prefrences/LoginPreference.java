package com.pocket.notes.prefrences;

import android.content.Context;
import android.content.SharedPreferences;

public class LoginPreference {

    Context context;

    public LoginPreference(Context context) {
        this.context = context;
    }

    private SharedPreferences getLoginPreference(){
        return context.getSharedPreferences("com.pocket.notes",Context.MODE_PRIVATE);
    }

    public void removeLoginPreference(){
        getLoginPreference().edit().clear().apply();
    }

    // For creating session
    public Boolean isLoggedIn(){
        return getLoginPreference().getBoolean("loggedin", false);
    }

    public void setLoggedIn(boolean b){
        getLoginPreference().edit().putBoolean("loggedin",b).apply();
    }

    public String getPhoneNumber(){
        return getLoginPreference().getString("phoneNumber", "");
    }

    public void setPhoneNumber(String phoneNumber){
        getLoginPreference().edit().putString("phoneNumber", phoneNumber).apply();
    }

    public String getEmail(){
        return getLoginPreference().getString("email","");
    }

    public void setEmail(String email){
        getLoginPreference().edit().putString("email",email).apply();
    }

    public String getName(){
        return getLoginPreference().getString("name","");
    }

    public void setName(String name){
        getLoginPreference().edit().putString("name",name).apply();
    }

    public int getId(){
        return getLoginPreference().getInt("id",0);
    }

    public void setId(int id){
        getLoginPreference().edit().putInt("id",id).apply();
    }

}
