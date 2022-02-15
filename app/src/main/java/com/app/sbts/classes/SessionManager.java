package com.app.sbts.classes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.app.sbts.activities.AttendeeActivity;
import com.app.sbts.activities.LoginActivity;
import com.app.sbts.activities.MainActivity;
import com.app.sbts.activities.ParentActivity;

import java.util.HashMap;


public class SessionManager {

    SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    public Context context;
    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "LOGIN";
    private static final String LOGIN = "IS_LOGIN";
    public static final String USERNAME = "USERNAME";
    public static final String ROLE = "ROLE";

    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void createSession(String user, String role) {
        editor.putBoolean(LOGIN, true);
        editor.putString(USERNAME, user);
        editor.putString(ROLE, role);
        editor.apply();
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(LOGIN, false);
    }

    public void checkLogIn() {
        if (!this.isLoggedIn()) {
            Intent i = new Intent(context, LoginActivity.class);
            context.startActivity(i);
            ((MainActivity) context).finish();
        }
    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<>();
        user.put(USERNAME, sharedPreferences.getString(USERNAME, null));
        user.put(ROLE, sharedPreferences.getString(ROLE, null));

        return user;
    }

    public void logout() {
        editor.clear();
        editor.commit();
        Intent i = new Intent(context, LoginActivity.class);
        context.startActivity(i);
        try {
            ((AttendeeActivity) context).finish();
        } catch (Exception ignored) {
        }
        try{
            ((ParentActivity) context).finish();
        }catch(Exception ignored){}
    }
}
