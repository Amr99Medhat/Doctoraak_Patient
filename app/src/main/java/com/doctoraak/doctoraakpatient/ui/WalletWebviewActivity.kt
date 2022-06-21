package com.doctoraak.doctoraakpatient.ui

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.doctoraak.doctoraakpatient.R
import kotlinx.android.synthetic.main.activity_wallet_webview.*

class WalletWebviewActivity : BaseActivity() {

    lateinit var webview : WebView
    var urll = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wallet_webview)
        webview = findViewById(R.id.web_view);
        urll = intent.getStringExtra("url") ?:""

        handleWebView()
    }

    private fun handleWebView()
    {
        with(webview)
        {
            settings.javaScriptEnabled = true
            settings.apply {
                setJavaScriptEnabled(true)
                setLoadWithOverviewMode(true)
                setUseWideViewPort(true)
                setSupportZoom(true)
                setBuiltInZoomControls(false)

                setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN)
                setCacheMode(WebSettings.LOAD_NO_CACHE)
                setDomStorageEnabled(true)
                webview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY)
                webview.setScrollbarFadingEnabled(true)
            }
            webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    loading.visibility = android.view.View.VISIBLE
                }
                override fun onPageFinished(view: WebView?, url: String?) {
                    loading.visibility = android.view.View.GONE
                }
            }

            loadUrl("$urll/")
        }
    }
}