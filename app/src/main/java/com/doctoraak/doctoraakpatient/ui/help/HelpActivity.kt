package com.doctoraak.doctoraakpatient.ui.help

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import com.doctoraak.doctoraakpatient.R
import com.doctoraak.doctoraakpatient.databinding.ActivityHelpBinding
import com.doctoraak.doctoraakpatient.repository.local.SessionManager
import com.doctoraak.doctoraakpatient.ui.BaseActivity

class HelpActivity : BaseActivity() {

    private lateinit var binding : ActivityHelpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this ,R.layout.activity_help)

    }
}
