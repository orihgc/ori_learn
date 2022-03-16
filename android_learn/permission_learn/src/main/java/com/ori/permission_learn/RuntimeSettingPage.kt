/*
 * Copyright 2018 Yan Zhenjie
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ori.permission_learn

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings

/**
 * Created by YanZhenjie on 2018/4/30.
 */
class RuntimeSettingPage(private val context: Context) {

    private val MARK = Build.MANUFACTURER.toLowerCase()

    /**
     * Start.
     *
     * @param requestCode this code will be returned in onActivityResult() when the activity exits.
     */
    fun start(isCallOnStart: Boolean, requestCode: Int) {
        var intent: Intent
        intent = when {
            MARK.contains("huawei") -> huaweiApi(context)
            MARK.contains("xiaomi") -> xiaomiApi(context)
            MARK.contains("oppo") -> oppoApi(context)
            MARK.contains("vivo") -> vivoApi(context)
            MARK.contains("meizu") -> meizuApi(context)
            else -> defaultApi(context)
        }
        try {
            if (isCallOnStart) {
                (context as Activity).startActivity(intent)
            } else {
                (context as Activity).startActivityForResult(intent, requestCode)
            }
        } catch (ignore: Exception) {
            intent = defaultApi(context)
            if (isCallOnStart) {
                (context as Activity).startActivity(intent)
            } else {
                (context as Activity).startActivityForResult(intent, requestCode)
            }
        }

    }

    private fun defaultApi(context: Context): Intent {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.fromParts("package", context.packageName, null)
        return intent
    }

    private fun huaweiApi(context: Context): Intent {
        val intent = Intent()
        intent.setClassName("com.huawei.systemmanager", "com.huawei.permissionmanager.ui.MainActivity")
        return if (hasActivity(context, intent)) intent else defaultApi(context)

    }

    private fun xiaomiApi(context: Context): Intent {
        val intent = Intent("miui.intent.action.APP_PERM_EDITOR")
        intent.putExtra("extra_pkgname", context.packageName)
        if (hasActivity(context, intent)) return intent

        intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity")
        if (hasActivity(context, intent)) return intent

        intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.PermissionsEditorActivity")
        return if (hasActivity(context, intent)) intent else defaultApi(context)

    }

    private fun vivoApi(context: Context): Intent {
        val intent = Intent()
        intent.putExtra("packagename", context.packageName)
        intent.setClassName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.SoftPermissionDetailActivity")
        if (hasActivity(context, intent)) return intent

        intent.setClassName("com.iqoo.secure", "com.iqoo.secure.safeguard.SoftPermissionDetailActivity")
        return if (hasActivity(context, intent)) intent else defaultApi(context)

    }

    private fun oppoApi(context: Context): Intent {
        val intent = Intent()
        intent.putExtra("packageName", context.packageName)
        intent.setClassName("com.color.safecenter", "com.color.safecenter.permission.PermissionManagerActivity")
        if (hasActivity(context, intent)) return intent

        intent.setClassName("com.oppo.safe", "com.oppo.safe.permission.PermissionAppListActivity")
        return if (hasActivity(context, intent)) intent else defaultApi(context)

    }

    private fun meizuApi(context: Context): Intent {
        val intent = Intent("com.meizu.safe.security.SHOW_APPSEC")
        intent.putExtra("packageName", context.packageName)
        intent.setClassName("com.meizu.safe", "com.meizu.safe.security.AppSecActivity")
        return if (hasActivity(context, intent)) intent else defaultApi(context)

    }

    private fun hasActivity(context: Context, intent: Intent): Boolean {
        val packageManager = context.packageManager
        return packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).size > 0
    }
}