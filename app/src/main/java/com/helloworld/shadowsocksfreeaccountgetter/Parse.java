package com.helloworld.shadowsocksfreeaccountgetter;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.robv.android.xposed.XposedBridge;

/**
 * Created by PinkD on 2016/9/4.
 * Parse
 */
public class Parse {
    public static final int SERVER = 0;
    public static final int PORT = 1;
    public static final int PASS = 2;
    public static final int ENCRYPTION = 3;

    private List<Account> accounts;
    private static final String TAG = "Parse";
    private ParseCallBack callBack;
    private Pattern[] patterns = new Pattern[4];
    private Matcher[] matchers = new Matcher[4];

    public Parse(ParseCallBack callBack) {
        this.callBack = callBack;
        patterns[SERVER] = Pattern.compile("<h4>[\\w]服务器地址:(.*)</h4>");
        patterns[PORT] = Pattern.compile("<h4>端口:(.*)</h4>");
        patterns[PASS] = Pattern.compile("<h4>[\\w]密码:(.*)</h4>");
        patterns[ENCRYPTION] = Pattern.compile("<h4>加密方式:(.*)</h4>");
    }

    public void parse(@NonNull String content) {
        if (Config.DEBUG) {
            XposedBridge.log(TAG + content);
            Log.d(TAG, "parse: " + content);
        }
        if (accounts == null) {
            accounts = new ArrayList<>();
        } else {
            accounts.clear();
        }
        for (int i = 0; i < patterns.length; i++) {
            matchers[i] = patterns[i].matcher(content);
        }
        for (int i = 0; i < matchers[0].groupCount(); i++) {
            accounts.add(new Account("vpn" + i));
        }
        for (int i = 0; i < matchers.length; i++) {
            if (matchers[i].find()) {
                for (int j = 0; j < matchers[i].groupCount(); j++) {
                    switch (i) {
                        case SERVER:
                            accounts.get(j).setServer(matchers[i].group(j));
                            break;
                        case PORT:
                            accounts.get(j).setPort(matchers[i].group(j));
                            break;
                        case PASS:
                            accounts.get(j).setPassword(matchers[i].group(j));
                            break;
                        case ENCRYPTION:
                            accounts.get(j).setEncryption(matchers[i].group(j));
                            break;
                    }
                }
            }
        }

        //TODO
        callBack.onSuccess(accounts);
    }

    public void onFail(Throwable throwable) {
        callBack.onFail(throwable);
    }

    interface ParseCallBack {

        void onSuccess(List<Account> accounts);

        void onFail(Throwable throwable);
    }

}
