package com.sparrowhawk.oxygenosaodenabler;

import android.content.Context;
import android.os.Handler;
import android.view.ViewGroup;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import de.robv.android.xposed.XC_MethodHook;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

public class FunctionReplacer implements IXposedHookLoadPackage{
    private int notifViewSec = 10;  //num seconds notifications remain
    private int threeKeyViewSec = 3;    //num seconds threekey notifs remain

    private Object clock = "com.oneplus.aod.OpClockViewCtrl";   //hook class for showing clock again

    public void handleLoadPackage(final LoadPackageParam lpparam) throws Throwable {
        if (lpparam.packageName.equals("com.android.systemui")) {
            XposedBridge.log("SYSTEMUI HOOKED");

            findAndHookMethod("com.android.systemui.statusbar.phone.DozeParameters", lpparam.classLoader, "getPulseVisibleDuration", int.class, new XC_MethodReplacement() {
                @Override
                protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                    return Integer.MAX_VALUE;
                }
            });

            findAndHookMethod("com.oneplus.aod.OpAodDisplayViewManager", lpparam.classLoader, "getStateString", int.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(final MethodHookParam param) throws Throwable {
                    if (param.getResult() == "notification") {

                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                XposedHelpers.callMethod(param.thisObject, "updateForPulseReason", 3);
                            }
                        }, notifViewSec*1000);

                        XposedHelpers.callMethod(clock, "updateClockDB");
                    }

                    else if (param.getResult() == "threekey") {

                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                XposedHelpers.callMethod(param.thisObject, "updateForPulseReason", 3);
                            }
                        }, threeKeyViewSec*1000);
                    }
                }
            });
        }
    }
}