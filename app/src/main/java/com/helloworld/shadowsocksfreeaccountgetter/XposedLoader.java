package com.helloworld.shadowsocksfreeaccountgetter;

import android.content.Context;
import android.util.Log;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

/**
 * Created by PinkD on 2016/9/4.
 * XposedLoader
 */
public class XposedLoader implements IXposedHookLoadPackage {
    private static final String TAG = "XposedLoader";
    private ClassLoader classLoader;
    private Context context;

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        if (!loadPackageParam.packageName.equals(Config.PACKAGE_NAME)) {
            return;
        }
        this.classLoader = loadPackageParam.classLoader;
        findAndHookMethod(Config.PACKAGE_NAME + "." + Config.CLASS_NAME, classLoader, Config.METHOD_NAME, new ContextXCMethodHook());
    }

    class ContextXCMethodHook extends XC_MethodHook implements Parse.ParseCallBack {
        @Override
        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
            context = (Context) param.getResult();
            NetWork.getResult(new Parse(this));
        }

        @Override
        public void onSuccess(Account account) {
            DBHelper.save(context, account);
        }

        @Override
        public void onFail(Throwable throwable) {
            if (Config.DEBUG) {
                Log.e(TAG, "onFail: ", throwable);
            }
        }
    }
}
