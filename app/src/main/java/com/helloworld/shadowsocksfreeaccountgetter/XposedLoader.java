package com.helloworld.shadowsocksfreeaccountgetter;

import android.content.Context;
import android.util.Log;
import android.view.View;

import java.util.List;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

/**
 * Created by PinkD on 2016/9/4.
 * XposedLoader
 */
public class XposedLoader implements IXposedHookLoadPackage {
    private static final String TAG = "XposedLoader";
    private Context context;
    private boolean first = true;

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        XposedBridge.log(TAG + "handleLoadPackage:packageName: " + loadPackageParam.packageName);
        XposedBridge.log(TAG + "handleLoadPackage:loadPackageParam: " + loadPackageParam.appInfo.name);
        Log.d(TAG, "handleLoadPackage:packageName: " + loadPackageParam.packageName);
        if (!loadPackageParam.packageName.equals(Config.PACKAGE_NAME)) {
            return;
        }
        ClassLoader classLoader = loadPackageParam.classLoader;
        XposedBridge.log(TAG + "Target:" + Config.PACKAGE_NAME + ". " + Config.CLASS_NAME);
        findAndHookMethod(Config.PACKAGE_NAME + "." + Config.CLASS_NAME, classLoader, Config.METHOD_NAME, new ContextXCMethodHook());
    }

    class ContextXCMethodHook extends XC_MethodHook implements Parse.ParseCallBack {
        @Override
        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
            if (first) {
                View view = (View) param.getResult();
                context = view.getContext();
                NetWork.getResult(new Parse(this));
                first = false;
            }
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
