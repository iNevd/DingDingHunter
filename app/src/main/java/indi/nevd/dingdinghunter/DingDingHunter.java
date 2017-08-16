package indi.nevd.dingdinghunter;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

import indi.nevd.common.network.RetrofitUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

import static de.robv.android.xposed.XposedHelpers.callMethod;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static de.robv.android.xposed.XposedHelpers.findClass;

/**
 * Created by nevd on 8/1/2017.
 */

public class DingDingHunter implements IXposedHookLoadPackage {

    private static final String TAG = "DingDingHunter";
    private static final String DINGDING_PACKAGE_NAME = "com.alibaba.android.rimet";


    @Override
    public void handleLoadPackage(final LoadPackageParam loadPackageParam) throws Throwable {
        if (loadPackageParam.packageName.equals(DINGDING_PACKAGE_NAME)){
            Log.i(TAG, "Start Hook + " + loadPackageParam.packageName);
            dingdingHook(loadPackageParam);
        }
    }

    private void dingdingHook(final LoadPackageParam loadPackageParam) throws Throwable{
        initVersion(loadPackageParam);
        Log.i(TAG, "MessageData.class: " + VersionParam.MessageData);

        // 如果VersionParam。MessageData为空，则不去hook
        if(VersionParam.MessageData.isEmpty()){
            Log.e(TAG, "error dingding version!!!");
            return;
        }

        hookMainLauncher(loadPackageParam);
        hookMessageDS(loadPackageParam);
    }

    // Hook main launcher
    private void hookMainLauncher(LoadPackageParam loadPackageParam) {
        findAndHookMethod("com.alibaba.android.rimet.biz.SplashActivity", loadPackageParam.classLoader, "onResume", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Activity activity = (Activity)param.thisObject;
                Toast.makeText(activity, "进入钉钉 By DingDingHunter", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Hook before save msg to db
    private void hookMessageDS(final LoadPackageParam loadPackageParam) {
        findAndHookMethod(VersionParam.MessageData, loadPackageParam.classLoader, "a", String.class, Collection.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                for (Object msg : (Collection) param.args[1]) {

                    String text = DingDingMsg.getMsgText(msg);
                    if(null == text || text.equals(DingDingMsg.MSG_RECALL)){
                        continue;
                    }

                    long mid = DingDingMsg.getMsgId(msg);
                    long senderId = DingDingMsg.getMsgSenderId(msg);
                    String senderName = DingDingMsg.getMsgSenderName(msg);
                    Log.i(TAG, "msg: " + mid + " ### " + text + " ### " + senderId + " ### " + senderName);
                    Map map =  new HashMap<>();
                    map.put("mid", Long.toString(mid));
                    map.put("content", text);
                    map.put("senderId", senderId);
                    map.put("type", DingDingMsg.MessageContentType.TEXT);
                    if(senderName != null && !senderName.isEmpty()){
                        map.put("senderName", senderName);
                    }

                    RetrofitUtils.getInstance().asyncPostNotice(map);
                }
            }
        });
    }


    private void initVersion(LoadPackageParam loadPackageParam) throws PackageManager.NameNotFoundException {
        Context context = (Context) callMethod(XposedHelpers.callStaticMethod(findClass("android.app.ActivityThread", null), "currentActivityThread", new Object[0]), "getSystemContext", new Object[0]);
        String versionName = context.getPackageManager().getPackageInfo(loadPackageParam.packageName, 0).versionName;
        Log.i(TAG, "Found dingding version:" + versionName);
        VersionParam.init(versionName);
    }
}
