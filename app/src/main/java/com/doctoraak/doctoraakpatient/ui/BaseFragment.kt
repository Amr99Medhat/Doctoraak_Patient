package com.doctoraak.doctoraakpatient.ui

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.doctoraak.doctoraakpatient.customView.DialogType
import com.doctoraak.doctoraakpatient.customView.SweetDialog
import java.util.*

open class BaseFragment:Fragment() {
    protected fun <T> launchIntent(activity: Context, cls: Class<T>)
            where T : AppCompatActivity {
        activity.startActivity(Intent(activity, cls))
    }

    protected open fun showLoginFirstDialog(title: String) {
        val sd = SweetDialog.newInstance(requireContext(), DialogType.WARNING)
        sd.show()
        sd.setMessage("")
        sd.setTitle(title)
        //sd.btn_cancel.visibility = View.GONE
        //sd.btn_ok.visibility = View.VISIBLE
        sd.setCancelable(true)
        sd.setOkClickListener(View.OnClickListener {
            sd.dismiss()
            var intent = Intent(requireContext(), SignInActivity::class.java)
            requireActivity().startActivity(intent)
        })

        sd.setCancelClickListener(View.OnClickListener {
            sd.dismiss()
        })

    }

    protected fun changelayoutDirection(lang: String) {
        val configuration = resources.configuration
        configuration.setLayoutDirection(Locale(lang))
        //resources.updateConfiguration(configuration, resources.displayMetrics)
        requireContext().createConfigurationContext(configuration)
    }

    fun enableButton(view: View) {
        view.isEnabled = true
    }

    protected fun setVisiblityGone(vararg views: View) {
        for (v in views) {
            v.visibility = View.GONE
        }
    }

    fun disableButton(view: View) {
        view.isEnabled = false
    }
}