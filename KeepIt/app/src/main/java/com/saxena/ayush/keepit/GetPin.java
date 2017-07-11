package com.saxena.ayush.keepit;

/**
 * Created by Ayush on 5/23/2017.
 */

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Lincoln on 05/05/16.
 */
public class GetPin {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context _context;

    // shared pref mode

    // Shared preferences file name
    private static final String PREF_NAME = "pass-keeper-built1";

    private static final String PIN = "PIN";

    public GetPin(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, 0);

    }

    public void SetPin(String newPin) {
        editor = pref.edit();
        editor.putString(PIN,newPin);
        editor.apply();
    }

    public String getPin() {
        return pref.getString(PIN,"NOPIN");
    }

}