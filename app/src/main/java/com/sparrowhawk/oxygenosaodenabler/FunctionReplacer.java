package com.sparrowhawk.oxygenosaodenabler;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import de.robv.android.xposed.XC_MethodHook;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

public class FunctionReplacer implements IXposedHookLoadPackage{
    public void handleLoadPackage(final LoadPackageParam lpparam) throws Throwable {
        /*if (lpparam.packageName.equals("com.oneplus.aod")) {

            XposedBridge.log("AOD HOOKED");
            findAndHookMethod("com.oneplus.aod.Utils", lpparam.classLoader, "isSupportAlwaysOn", new XC_MethodReplacement() {
                @Override
                protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                    return true;
                }
            });

            findAndHookMethod("com.oneplus.aod.Utils", lpparam.classLoader, "updateAlwaysOnState", Context.class, "int", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    boolean mIsAlwaysOnModeEnabled = true;
                    String str = "OPAodUtils";
                    try {
                        System.setProperty("sys.aod.disable", mIsAlwaysOnModeEnabled ? "0" : "1");
                    } catch (Exception e) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("Exception e = ");
                        sb.append(e.toString());
                        Log.d(str, sb.toString());
                    }
                }
            });


        }

        if (lpparam.packageName.equals("com.oneplus.settings")) {
            XposedBridge.log("ONEPLUS SETTINGS HOOKED");

            findAndHookMethod("com.oneplus.settings.utils.OPUtils", lpparam.classLoader, "isSupportAlwaysOnDisplay", new XC_MethodReplacement() {
                @Override
                protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                    return true;
                }
            });
        }

        if (lpparam.packageName.equals("com.android.settings")) {

            XposedBridge.log("ANDROID SETTINGS HOOKED");
            findAndHookMethod("com.android.settings.display.AmbientDisplayAlwaysOnPreferenceController", lpparam.classLoader, "isAvailable","android.hardware.display.AmbientDisplayConfiguration", new XC_MethodReplacement() {
                @Override
                protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                    return true;
                }
            });
        }
*/
        if (lpparam.packageName.equals("com.android.systemui")) {
            XposedBridge.log("SYSTEMUI HOOKED");
/*
            findAndHookMethod("com.oneplus.aod.OpAodUtils", lpparam.classLoader, "isSupportAlwaysOn", new XC_MethodReplacement() {
                @Override
                protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                    return true;
                }
            });

            findAndHookMethod("com.oneplus.aod.OpAodUtils", lpparam.classLoader, "updateAlwaysOnState", Context.class, "int", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    boolean mIsAlwaysOnModeEnabled = true;
                    String str = "OPAodUtils";
                    try {
                        System.setProperty("sys.aod.disable", mIsAlwaysOnModeEnabled ? "0" : "1");
                    } catch (Exception e) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("Exception e = ");
                        sb.append(e.toString());
                        Log.d(str, sb.toString());
                    }
                }
            });

            findAndHookMethod("com.oneplus.aod.OpAodUtils", lpparam.classLoader, "isAlwaysOnEnabled", new XC_MethodReplacement() {
                @Override
                protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                    return true;
                }
            });
            */
            findAndHookMethod("com.android.systemui.statusbar.phone.DozeParameters", lpparam.classLoader, "getPulseVisibleDuration", "int", new XC_MethodReplacement() {
                @Override
                protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                    return Integer.MAX_VALUE;
                }
            });

            findAndHookMethod("com.oneplus.aod.OpAodThreeKeyStatusView", lpparam.classLoader, "onThreeKeyChanged", "int", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //run reset of AOD screen after 3 sec
                            Class<?> aodDisplayManager = XposedHelpers.findClass("com.oneplus.aod.OpAodDisplayViewManager", lpparam.classLoader);
                            XposedHelpers.callMethod(aodDisplayManager, "onScreenTurnedOff");
                        }
                    }, 3000);
                }
            });
        }
    }
}