package com.doctoraak.doctoraakpatient.ui

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.doctoraak.doctoraakpatient.R
import com.doctoraak.doctoraakpatient.adapters.AutoCompleteBase
import com.doctoraak.doctoraakpatient.adapters.AutoCompleteTextViewAdapter
import com.doctoraak.doctoraakpatient.customView.DialogType
import com.doctoraak.doctoraakpatient.customView.SweetDialog
import com.doctoraak.doctoraakpatient.databinding.SearchAddressWithNameBinding
import com.doctoraak.doctoraakpatient.databinding.SearchByCityAndAreaWithNameBinding
import com.doctoraak.doctoraakpatient.databinding.SearchNameCityAddressIndicatorBinding
import com.doctoraak.doctoraakpatient.ui.myOrders.MyOrdersActivity
import com.doctoraak.doctoraakpatient.utils.DividerItemDecorationExceptLast
import com.doctoraak.doctoraakpatient.utils.MyContextWrapper
import com.doctoraak.doctoraakpatient.utils.Utils
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_doctor_category.*
import kotlinx.android.synthetic.main.dialog_sweet.*
import java.util.*

@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity() {
    //variables used in many activities
    protected var cityId = -1
    protected var areaId = -1
    protected var nameId = -1
    protected var latt: Double? = null
    protected var lang: Double? = null

    fun disableButton(view: View) {
        view.isEnabled = false
    }

    fun enableButton(view: View) {
        view.isEnabled = true
    }

//    override fun onStart() {
//        super.onStart()
//        setupBackClick()
//    }

    protected fun setupBackClick() {
        if (iv_back != null) {
            iv_back.setOnClickListener {
                finish()
            }
        }
    }

    //change language to selected one on onCreate in every activity because it will be reset on rotation
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Utils.changeLanguage(this, Utils.getAppLanguage())
        changelayoutDirection(Utils.getAppLanguage())

        overridePendingTransition(R.anim.activity_push_up_in, R.anim.activity_push_up_out);
    }

    override fun onPause() {
        super.onPause()
        overridePendingTransition(R.anim.activity_open_nothing_animation,
            R.anim.activity_close_translate);
    }

    protected fun setRecyclerviewLinearLayout(view: RecyclerView): LinearLayoutManager {
        val layout = LinearLayoutManager(this)
        view.layoutManager = layout
        return layout

    }

    protected fun addListDivider(view: RecyclerView) {
        view.addItemDecoration(DividerItemDecorationExceptLast(this, ResourcesCompat.getDrawable(
            resources, R.drawable.drawable_divider, null)!!
        ), -1
        )
    }

    protected open fun showSuccessDialog(msg: String, title: String) {
        val sd = SweetDialog.newInstance(this, DialogType.SUCCESS)
        sd.show()
        sd.setMessage(msg)
        sd.setTitle(title)
        sd.btn_cancel.visibility = View.GONE
        sd.btn_ok.visibility = View.VISIBLE
        sd.setCancelable(true)
        sd.setOkClickListener(View.OnClickListener {
            startActivity(Intent(this, MyOrdersActivity::class.java))
            finish()
        }
        )
    }

    protected open fun showLoginFirstDialog(title: String) {
        val sd = SweetDialog.newInstance(this, DialogType.WARNING)
        sd.show()
        sd.setMessage("")
        sd.setTitle(title)
        //sd.btn_cancel.visibility = View.GONE
        //sd.btn_ok.visibility = View.VISIBLE
        sd.setCancelable(true)
        sd.setOkClickListener(View.OnClickListener {
            sd.dismiss()
            startActivity(Intent(this, SignInActivity::class.java))
        })

        sd.setCancelClickListener(View.OnClickListener {
            sd.dismiss()
        })

    }

    protected fun setupAutoCompleteTextView(view: AutoCompleteTextView, list: ArrayList<String>) {
        val adapter = ArrayAdapter(this, R.layout.autocompletetextview_item, list)

        view.setThreshold(1);
        view.setAdapter(adapter)
    }

    /**
     * @see AutoCompleteTextViewAdapter */
    protected fun <T : AutoCompleteBase> setupAutoCompleteTextViewGeneric(view: AutoCompleteTextView,
                                                                          list: ArrayList<T>) {
        val adapter = AutoCompleteTextViewAdapter(this, R.layout.autocompletetextview_item, list)

        view.setThreshold(1);
        view.setAdapter(adapter)
    }


    protected fun setVisiblityGone(vararg views: View) {
        for (v in views) {
            v.visibility = View.GONE
        }
    }

    protected fun setVisiblityVisible(vararg views: View) {
        for (v in views) {
            v.visibility = View.VISIBLE
        }
    }

    protected fun applyAnimation(res: Int,
                                 vararg views: View,
                                 onAnimaitionFinished: () -> Unit = {}) {
        val animation = AnimationUtils
            .loadAnimation(applicationContext, res)

        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation?) {
                onAnimaitionFinished()
            }

            override fun onAnimationStart(animation: Animation?) {
            }
        })

        for (v in views)
            v.startAnimation(animation)
    }

    protected fun logd(msg: String, tag: String = "defaultClass") {
        Log.d(tag, msg)
    }

    protected fun setToolBar(toolbar: Toolbar?) {
        /*if (Utils.getUser().insurance != null) {
            Glide.with(this).load(Utils.getUser().insurance!!.url)
                .override(128)
                .into(iv_insurance)
        }*/
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    protected fun <T> launchIntent(activity: Context, cls: Class<T>)
            where T : AppCompatActivity {
        activity.startActivity(Intent(activity, cls))
    }


    protected fun toast(text: String) {
        if (text.isNotBlank()) {
            Toast.makeText(this, text, Toast.LENGTH_SHORT)
                .run {
                    show()
                }
        }
    }


    protected fun toast(textId: Int) = toast(getString(textId))

    protected fun finishAndBack() {
        finish()
        onBackPressed()
    }

    override fun attachBaseContext(newBase: Context?) {
        val lang_code = Utils.getAppLanguage()
        super.attachBaseContext(MyContextWrapper.wrap(newBase!!, lang_code!!))
    }


    protected fun changelayoutDirection(lang: String) {
        val configuration = resources.configuration
        configuration.setLayoutDirection(Locale(lang))
        //resources.updateConfiguration(configuration, resources.displayMetrics)
        this.createConfigurationContext(configuration)
    }

    protected fun showSnackbar(view: ViewGroup, msg: String) {
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show()
    }


    protected fun handleSearchWithNameCityAddressIndicator(searchIndicator: SearchNameCityAddressIndicatorBinding,
                                                           searchByCity: SearchByCityAndAreaWithNameBinding,
                                                           searchByAddress: SearchAddressWithNameBinding,
                                                           onNameClick: () -> Unit = {},
                                                           onAddressClick: () -> Unit = {},
                                                           onCityClick: () -> Unit = {}
    ) {
        val animateIndicator: (Float) -> Unit = {
            ObjectAnimator.ofFloat(searchIndicator.radiobuttonView,
                "translationX",
                it - searchIndicator.radiobuttonView.width).apply {
                duration = 600
                start()
            }
        }

        val animateSearchView: (View, View) -> Unit = { txtView, group ->
            when {
                searchByCity.root.visibility == View.VISIBLE && searchByCity.splLabName.visibility == View.GONE -> {
                    applyAnimation(R.anim.fade_out, searchByCity.root)
                    searchByCity.root.visibility = View.GONE
                }
                searchByAddress.root.visibility == View.VISIBLE -> {
                    applyAnimation(R.anim.fade_out, searchByAddress.root)
                    searchByAddress.root.visibility = View.GONE
                }
                searchByCity.splLabName.visibility == View.VISIBLE -> {
                    applyAnimation(R.anim.fade_out, searchIndicator.tvSearchByName)
                    searchByCity.root.visibility = View.GONE
                }
            }

            applyAnimation(R.anim.fade_in, group)
            group.visibility = View.VISIBLE

            animateIndicator(txtView.x)
        }

        var selectedIndex = 1
        searchIndicator.tvSearchByName.setOnClickListener {
            if (selectedIndex != 0) {
                selectedIndex = 0
                onNameClick()

                animateSearchView(it, searchByCity.root)
                searchByCity.splLabName.visibility = View.VISIBLE
                searchByCity.groupCityArea.visibility = View.GONE

                searchIndicator.tvSearchByName.setTextColor(ContextCompat.getColor(this,
                    R.color.white))
                searchIndicator.tvSearchByCity.setTextColor(ContextCompat.getColor(this,
                    R.color.black))
                searchIndicator.tvSearchByAddress.setTextColor(ContextCompat.getColor(this,
                    R.color.black))
            }
        }

        searchIndicator.tvSearchByCity.setOnClickListener {
            if (selectedIndex != 1) {
                selectedIndex = 1
                onCityClick()

                animateSearchView(it, searchByCity.root)
                searchByCity.splLabName.visibility = View.GONE
                searchByCity.groupCityArea.visibility = View.VISIBLE

                searchIndicator.tvSearchByName.setTextColor(ContextCompat.getColor(this,
                    R.color.black))
                searchIndicator.tvSearchByCity.setTextColor(ContextCompat.getColor(this,
                    R.color.white))
                searchIndicator.tvSearchByAddress.setTextColor(ContextCompat.getColor(this,
                    R.color.black))
            }
        }

        searchIndicator.tvSearchByAddress.setOnClickListener {
            if (selectedIndex != 2) {
                selectedIndex = 2
                onAddressClick()

                animateSearchView(it, searchByAddress.root)
                searchIndicator.tvSearchByName.setTextColor(ContextCompat.getColor(this,
                    R.color.black))
                searchIndicator.tvSearchByCity.setTextColor(ContextCompat.getColor(this,
                    R.color.black))
                searchIndicator.tvSearchByAddress.setTextColor(ContextCompat.getColor(this,
                    R.color.white))
            }
        }

    }
}