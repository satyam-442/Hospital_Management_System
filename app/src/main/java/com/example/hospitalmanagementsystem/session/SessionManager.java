package com.example.hospitalmanagementsystem.session;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {
    SharedPreferences usersSession;
    SharedPreferences.Editor editor;
    Context context;

    public static final String SESSION_USERSESSION = "userLoginSession";
    public static final String SESSION_REMEMBERME = "rememberMe";
    private static final String IS_LOGIN = "IsLoggedIn";

    private static final String IS_REMEMBERME = "IsRememberMe";
    public static final String KEY_LOGIN_TYPE = "type";
    public static final String KEY_SESSION_ID = "userId";
    public static final String KEY_SESSION_PWD = "password";

    public SessionManager(Context _context, String sessionName){
        context = _context;
        usersSession = context.getSharedPreferences(sessionName,Context.MODE_PRIVATE);
        editor = usersSession.edit();
    }

    public void logoutUserFromSession(){
        editor.clear();
        editor.commit();
    }

    public void createRememberMeSession(String userID, String password, String typeEntity){
        editor.putBoolean(IS_REMEMBERME,true);
        editor.putString(KEY_SESSION_ID,userID);
        editor.putString(KEY_SESSION_PWD,password);
        editor.putString(KEY_LOGIN_TYPE,typeEntity);
        editor.commit();
    }

    public HashMap<String,String> getRememberMeDetailsFromSession(){
        HashMap<String, String> userData = new HashMap<>();
        userData.put(KEY_SESSION_ID, usersSession.getString(KEY_SESSION_ID,null));
        userData.put(KEY_SESSION_PWD, usersSession.getString(KEY_SESSION_PWD,null));
        userData.put(KEY_LOGIN_TYPE, usersSession.getString(KEY_LOGIN_TYPE,null));
        return userData;
    }

    public boolean checkRememberMe(){
        if (usersSession.getBoolean(IS_REMEMBERME,false)){
            return true;
        } else
            return false;
    }
}
