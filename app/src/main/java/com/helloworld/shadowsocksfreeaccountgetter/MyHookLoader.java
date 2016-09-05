package com.helloworld.shadowsocksfreeaccountgetter;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by PinkD on 2016/9/4.
 * MyHookLoader
 */
public class MyHookLoader implements IXposedHookLoadPackage {
    private static final String TAG = "MyHookLoader";
    private Context context;
    private boolean first = true;
    private Object lock;

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        XposedBridge.log(TAG + "->handleLoadPackage:packageName: " + loadPackageParam.packageName);
        if (!loadPackageParam.packageName.equals(Config.PACKAGE_NAME)) {
            return;
        }
        Log.d(TAG, "handleLoadPackage: -------------------------------");
        XposedBridge.log(TAG + "Target:" + Config.PACKAGE_NAME + ". " + Config.CLASS_NAME);
        XposedHelpers.findAndHookMethod(Config.PACKAGE_NAME + "." + Config.CLASS_NAME, loadPackageParam.classLoader, Config.CREATE_METHOD_NAME, new ContextXCMethodHook());
    }

    class ContextXCMethodHook extends XC_MethodHook implements Parse.ParseCallBack {
        @Override
        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
            context = (Context) XposedHelpers.callMethod(param.thisObject, Config.CONTEXT_METHOD_NAME);
            Toast.makeText(context, "Oh,在这儿停顿!", Toast.LENGTH_SHORT).show();
            NetWork.getResult(new Parse(this));
        }

        @Override
        public void onSuccess(List<Account> accounts) {
            if (Config.DEBUG) {
                Log.d(TAG, "onSuccess: " + accounts);
                XposedBridge.log(TAG + accounts.toString());
            }
            DBHelper.save(context, accounts);
            context = null;
        }

        @Override
        public void onFail(Throwable throwable) {
            if (Config.DEBUG) {
                XposedBridge.log(TAG + throwable);
                Log.e(TAG, "onFail: ", throwable);
            }
            context = null;
        }
    }

}
