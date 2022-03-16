package com.ori.permission_learn

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.PowerManager
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.ContextCompat.startActivity


object PowerOptUtil {

    /**
     * Vivo
     * */
    fun gotoVivoPowerPage(context: Context) {
        try {
            val intent = Intent().apply {
                component = ComponentName(
                    "com.vivo.abe",
                    "com.vivo.applicationbehaviorengine.ui.ExcessivePowerManagerActivity"
                )
                component =
                    ComponentName.unflattenFromString("com.iqoo.powersaving/.PowerSavingManagerActivity")
            }
            context.startActivity(intent)
        }catch (t:Throwable){
            val cn =
                ComponentName("com.iqoo.powersaving", "com.iqoo.powersaving.PowerSavingManagerActivity")
            val intent = Intent()
            intent.component = cn
            context.startActivity(intent)
        }
    }

    /**
     * OPPO
     * */
    fun gotoOppoPowerPage(context: Context) {
        val cn = ComponentName(
            "com.coloros.oppoguardelf",
            "com.coloros.powermanager.fuelgaue.PowerUsageModelActivity"
        )
        val intent = Intent()
        intent.component = cn
        context.startActivity(intent)
    }

    /**
     * 小米
     * */
    fun gotoXiaomiPowerPage(context: Context) {
        val intent = Intent()
        intent.action = "miui.intent.action.HIDDEN_APPS_CONFIG_ACTIVITY";
        intent.putExtra("package_name", context.packageName);
        intent.putExtra("package_label", "com.ori.permission_learn")
        intent.putExtra(Settings.EXTRA_CHANNEL_ID, context.applicationInfo.uid);
        context.startActivity(intent);
    }

    /**
     * 华为
     * */
    fun gotoHuaWeiPowerPage(context: Context) {
        val intent = Intent()
        intent.component = ComponentName(
            "com.huawei.systemmanager",
            "com.huawei.systemmanager.mainscreen.MainScreenActivity"
        )
        context.startActivity(intent)
    }

    /**
     * 其他 省电优化列表
     * */
    fun gotoOtherPowerPage(context: Context) {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        intent.component =
            ComponentName.unflattenFromString("com.android.settings/.Settings$" + "HighPowerApplicationsActivity")
        context.startActivity(intent)
    }
}