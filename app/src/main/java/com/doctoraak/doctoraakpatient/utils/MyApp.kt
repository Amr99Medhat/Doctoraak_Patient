package com.doctoraak.doctoraakpatient.utils

import android.app.Application
import com.doctoraak.doctoraakpatient.repository.local.SessionManager

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        SessionManager(this)
    }
}