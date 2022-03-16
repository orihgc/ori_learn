package com.ori.permission_learn

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import androidx.fragment.app.FragmentActivity
import java.lang.Exception

object BgStartUtil {
    private fun isVivoBgStartPermissionAllowed(activity: FragmentActivity): Boolean {
        return getVivoBgStartPermissionStatus(activity) == 0
    }

    @SuppressLint("Range")
    private fun getVivoBgStartPermissionStatus(context: Context): Int {
        val uri: Uri =
            Uri.parse("content://com.vivo.permissionmanager.provider.permission/start_bg_activity")
        val selection = "pkgname = ?"
        val selectionArgs = arrayOf(context.packageName)
        var state = 1
        try {
            context.contentResolver.query(uri, null, selection, selectionArgs, null)?.use {
                if (it.moveToFirst()) {
                    state = it.getInt(it.getColumnIndex("currentstate"))
                }
            }
        } catch (e: Exception) {
        }
        return state
    }
}