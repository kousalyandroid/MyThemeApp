package com.kousihub.mythemeapp.utils

import android.content.Context
import android.widget.Toast

fun Any.toast(mContext: Context, duration: Int = Toast.LENGTH_SHORT) {
    return Toast.makeText(mContext, this.toString(), duration).show()
}