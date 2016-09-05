package com.helloworld.shadowsocksfreeaccountgetter;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.util.List;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by PinkD on 2016/9/4.
 * MyHookLoader
 */
public class MyHookLoader implements IXposedHookLoadPackage {
    private static final String TAG = "MyHookLoader";
    private XSharedPreferences sharedPreferences;
    private Context context;
    private SQLiteOpenHelper openHelper;
    private boolean init;

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        if (Config.DEBUG) {
            XposedBridge.log(TAG + "->handleLoadPackage:packageName: " + loadPackageParam.packageName);
        }
        if (!loadPackageParam.packageName.equals(Config.PACKAGE_NAME)) {
            return;
        }
        if (Config.DEBUG) {
            Log.d(TAG, "handleLoadPackage: -------------------------------");
        }
        if (sharedPreferences == null) {
            sharedPreferences = new XSharedPreferences(new File(getPrefFileName(getClass().getPackage().getName(), "settings")));
        }
        if (!sharedPreferences.getBoolean("checked", true)) {
            return;
        }
        XposedHelpers.findAndHookMethod(Config.PACKAGE_NAME + "." + Config.APP_CLASS_NAME, loadPackageParam.classLoader, Config.CREATE_METHOD_NAME, new ContextXCMethodHook());
        XposedHelpers.findAndHookMethod(Config.PACKAGE_NAME + "." + Config.ACTIVITY_CLASS_NAME, loadPackageParam.classLoader, Config.AD_METHOD_NAME, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                if (!init) {
                    synchronized (param.thisObject) {
                        param.thisObject.wait(1000);
                    }
                }
                //Remove Ads
                param.setResult(null);
            }
        });
    }

    class ContextXCMethodHook extends XC_MethodHook implements Parse.ParseCallBack {
        @Override
        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
            if (!init) {
                context = (Context) XposedHelpers.callMethod(param.thisObject, Config.CONTEXT_METHOD_NAME);
                openHelper = (SQLiteOpenHelper) XposedHelpers.callMethod(param.thisObject, Config.DB_METHOD_NAME);
                Toast.makeText(context, "Oh,在这儿停顿!", Toast.LENGTH_SHORT).show();
                NetWork.getResult(new Parse(this));
                init = true;
            }
        }

        @Override
        public void onSuccess(List<Account> accounts) {
            DBSaver.save(openHelper, accounts);
            context = null;
            openHelper = null;
            init = false;
            if (Config.DEBUG) {
                Log.d(TAG, "onSuccess: " + accounts);
                XposedBridge.log(TAG + accounts.toString());
            }
        }

        @Override
        public void onFail(Throwable throwable) {
            context = null;
            openHelper = null;
            init = false;
            if (Config.DEBUG) {
                XposedBridge.log(TAG + throwable);
                Log.e(TAG, "onFail: ", throwable);
            }
        }
    }

    private static String getPrefFileName(String packageName, String preference) {
        return "/data/data/" + packageName + "/shared_prefs/" + preference + ".xml";
    }

}
