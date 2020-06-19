package com.sparrowhawk.oxygenosaodenabler;

import android.content.Context;
import android.util.Log;

import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import de.robv.android.xposed.XC_MethodHook;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

public class FunctionReplacer implements IXposedHookLoadPackage, IXposedHookInitPackageResources {
    public void handleLoadPackage(final LoadPackageParam lpparam) throws Throwable {
        if (lpparam.packageName.equals("com.oneplus.aod")) {

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

        if (lpparam.packageName.equals("com.android.systemui")) {
            XposedBridge.log("SYSTEMUI HOOKED");

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
        }
    }

    public void handleInitPackageResources(final XC_InitPackageResources.InitPackageResourcesParam ipparam) throws Throwable {
        if (!ipparam.packageName.equals("com.android.systemui")) {
            return;
        }

        ipparam.res.setReplacement("com.android.systemui", "integer", "doze_pulse_duration_visible", 6000);
    }
}