package com.pyler.enablecmcallrec;

import static de.robv.android.xposed.XposedHelpers.callMethod;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import android.content.Context;
import android.widget.Button;
import android.widget.CompoundButton;

import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_InitPackageResources.InitPackageResourcesParam;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class EnableCallRecording implements IXposedHookLoadPackage,
		IXposedHookInitPackageResources {
	public static final String DIALER = "com.android.dialer";
	public static final String INCALLUI = "com.android.incallui";
	public static final String CALL_RECORDING_SERVICE = "com.android.services.callrecorder.CallRecorderService";
	public static final String RECORDERENABLE = "com.android.incallui.CallButtonFragment";

	@Override
	public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {
		try {
			if (DIALER.equals(lpparam.packageName)) {
				findAndHookMethod(CALL_RECORDING_SERVICE, lpparam.classLoader,
						"isEnabled", Context.class,
						XC_MethodReplacement.returnConstant(true));
			//} else if (INCALLUI.equals(lpparam.packageName)) {
				XposedBridge.log("LOOOOOOG BITCH");
				findAndHookMethod(RECORDERENABLE, lpparam.classLoader,"onCreate", new XC_MethodHook() {
					@Override
					protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
						super.beforeHookedMethod(param);
					}

					@Override
					protected void afterHookedMethod(MethodHookParam param) throws Throwable {
						super.afterHookedMethod(param);

						CompoundButton x = (CompoundButton) XposedHelpers.getObjectField(XposedHelpers.getSurroundingThis(param.thisObject), "mCallRecordButton");
						x.performClick();
					}
				});
			}
		}
		catch (Exception e){
			XposedBridge.log(e);
		}
	}

	@Override
	public void handleInitPackageResources(InitPackageResourcesParam resparam)
			throws Throwable {
		if (!DIALER.equals(resparam.packageName)) {
			return;
		}
		resparam.res.setReplacement(DIALER, "bool", "call_recording_enabled",
				true);
	}


}	