package com.amar.photostyle.utils

import android.Manifest
import android.content.Context
import pub.devrel.easypermissions.EasyPermissions


object PermissionsUtils {
    fun hasPermissions(context: Context)=
        EasyPermissions.hasPermissions(context,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
        )
}