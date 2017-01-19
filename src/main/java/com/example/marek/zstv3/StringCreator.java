package com.example.marek.zstv3;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;

/**
 * Created by Marek on 2017-01-19.
 */

public class StringCreator {

    public String createQuerry(String operation, String oid, SharedPreferences sharedPrefs)
    {
        String out = null;
        out = operation + "/" +
                sharedPrefs.getString("pref_key_snmp_connection_address", "127.0.0.1") + "/" +
                sharedPrefs.getString("pref_key_snmp_connection_port", "161") + "/" +
                sharedPrefs.getString("pref_key_snmp_version", "2") + "/" +
                sharedPrefs.getString("pref_key_connection_community", "community") + "/" +
                oid + ".0/";
        return out;
    }
}
