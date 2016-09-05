package com.helloworld.shadowsocksfreeaccountgetter;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.List;

/**
 * Created by PinkD on 2016/9/4.
 * DBSaver
 */
public class DBSaver {
    private static final String TAG = "DBSaver";

    public static boolean save(SQLiteOpenHelper sqLiteOpenHelper, List<Account> accounts) {
        if (Config.DEBUG) {
            Log.d(TAG, "save: ");
        }
        SQLiteDatabase database = sqLiteOpenHelper.getWritableDatabase();
        for (Account account : accounts) {
            database.execSQL("DELETE FROM profile WHERE host = ?", new String[]{account.getServer()});
        }
        for (Account account : accounts) {
            database.execSQL("INSERT INTO profile (name, host, remotePort, password, method, route ,chnroute ,global) VALUES (?, ?, ?, ?, ? ,'bypass-china' ,1 ,1)",
                    new String[]{account.getName(), account.getServer(), account.getPort(), account.getPassword(), account.getEncryption()});
        }
        return true;
    }

}
