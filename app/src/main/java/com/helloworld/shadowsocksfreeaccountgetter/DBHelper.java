package com.helloworld.shadowsocksfreeaccountgetter;

import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by PinkD on 2016/9/4.
 * DBHelper
 */
public class DBHelper {
    private static final String TAG = "DBHelper";

    public static boolean save(Context context, List<Account> accounts) {
        if (Config.DEBUG) {
            Log.d(TAG, "save: ");
        }
        //TODO
        return false;
    }
}
