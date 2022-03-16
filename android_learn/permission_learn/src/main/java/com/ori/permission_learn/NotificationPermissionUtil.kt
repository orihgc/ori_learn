package com.ori.permission_learn

import android.app.Activity
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.os.Build
import android.provider.Settings
import java.lang.Exception

class NotificationPermissionUtil {

    fun gotoNotificationSetting(activity: Activity) {
        val appInfo: ApplicationInfo = activity.applicationInfo
        val pkg: String = activity.applicationContext.packageName
        val uid: Int = appInfo.uid
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val intent = Intent()
                intent.action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
                //这种方案适用于 API 26, 即8.0（含8.0）以上可以用
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, pkg)
                intent.putExtra(Settings.EXTRA_CHANNEL_ID, uid)
                //这种方案适用于 API21——25，即 5.0——7.1 之间的版本可以使用
                intent.putExtra("app_package", pkg)
                intent.putExtra("app_uid", uid)
                activity.startActivityForResult(intent, MainActivity.REQUEST_SETTING_NOTIFICATION)
            }
        } catch (e: Exception) {
            val intent = Intent(Settings.ACTION_SETTINGS)
            activity.startActivityForResult(intent, MainActivity.REQUEST_SETTING_NOTIFICATION)
        }
    }
}