package com.helloworld.shadowsocksfreeaccountgetter;

import android.support.annotation.NonNull;
import android.util.Log;

/**
 * Created by PinkD on 2016/9/4.
 * Parse
 */
public class Parse {
    private static final String TAG = "Parse";
    private ParseCallBack callBack;

    public Parse(ParseCallBack callBack) {
        this.callBack = callBack;
    }

    public void parse(@NonNull String content) {
        if (Config.DEBUG) {
            Log.d(TAG, "parse: " + content);
        }
        //TODO
        callBack.onSuccess(new Account());
    }

    public void onFail(Throwable throwable) {
        callBack.onFail(throwable);
    }

    interface ParseCallBack {

        void onSuccess(Account account);

        void onFail(Throwable throwable);
    }

}
