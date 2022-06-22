package com.doctoraak.doctoraakpatient.ui

import android.graphics.Bitmap
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import com.doctoraak.doctoraakpatient.R
import com.doctoraak.doctoraakpatient.databinding.ActivityAnalyticBinding
import com.doctoraak.doctoraakpatient.repository.local.SessionManager
import com.doctoraak.doctoraakpatient.utils.Utils

class AnalyticActivity : BaseActivity()
{
    private lateinit var binding: ActivityAnalyticBinding

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_analytic)

        if (Utils.checkInternetConnection(this,binding.clAnalytics)) {
            handleWebView()
        }else{
            binding.loading.visibility = View.GONE
        }

        val logo = findViewById<ImageView>(R.id.iv_oncare_logo)
        val user = SessionManager.returnUserInfo()
        if (user != null) {
            if (user.insurance!!.id == 1) {
                logo.visibility = View.VISIBLE
            }
        }
    }


    private fun handleWebView()
    {
        with(binding.webView)
        {
            settings.javaScriptEnabled = true
            webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    binding.loading.visibility = View.VISIBLE
                }
                override fun onPageFinished(view: WebView?, url: String?) {
                    binding.loading.visibility = View.GONE
                }
            }
            loadUrl("https://vcheckout.paymobsolutions.com/checkout/eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6Mjk2NzM2NzY0LCJleHAiOjE1OTY5NzM4MTd9.j7NnNemnNf9L3ftIe1ywGslgjz-8sX9j-9NLSp4FEc4/")
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        when (item?.itemId)
        {
            android.R.id.home -> {
                finishAndBack()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


}
