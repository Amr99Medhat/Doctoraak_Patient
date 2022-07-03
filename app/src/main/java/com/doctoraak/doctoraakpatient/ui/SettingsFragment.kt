package com.doctoraak.doctoraakpatient.ui

import android.annotation.SuppressLint
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.doctoraak.doctoraakpatient.R
import com.doctoraak.doctoraakpatient.databinding.ChangeLanguageDialogBinding
import com.doctoraak.doctoraakpatient.databinding.FragmentSettingsBinding
import com.doctoraak.doctoraakpatient.repository.local.SessionManager
import com.doctoraak.doctoraakpatient.utils.Utils


    class SettingsFragment : BaseFragment() {
    private lateinit var binding:FragmentSettingsBinding
    private lateinit var changeLanguageBinding: ChangeLanguageDialogBinding
    private lateinit var alert: AlertDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View{
        binding = FragmentSettingsBinding.inflate(inflater,container,false)
        changeLanguageBinding = ChangeLanguageDialogBinding.inflate(layoutInflater)
        setListener()
        return binding.root
    }
    companion object {
        @JvmStatic
        fun newInstance() =
            SettingsFragment().apply {
            }
    }
        private fun showChangeLanguageDialog() {
           val builder =AlertDialog.Builder(requireContext(), R.style.AlertDialogTheme)
            if (changeLanguageBinding.root.parent != null) {
                (changeLanguageBinding.root.parent as ViewGroup).removeView(
                    changeLanguageBinding.root
                )
            }
            builder.setView(changeLanguageBinding.root)
            alert = builder.create()
            if (SessionManager.getLang().equals("en"))
            {
                changeLanguageBinding.rbEnglish.isChecked = true
            }
            else{
                changeLanguageBinding.rbArabic.isChecked = true
            }
            if (alert.window != null) {
                alert.window!!.setBackgroundDrawable(ColorDrawable(0))
            }
            alert.show()

        }
@SuppressLint("CommitTransaction")
private fun setListener(){
    binding.btnChangeLanguage.setOnClickListener {
        showChangeLanguageDialog()
    }
    changeLanguageBinding.rbEnglish.setOnClickListener {
        SessionManager.saveLang("en")
        changelayoutDirection("en")
        Utils.changeLanguage(requireContext(), "en")
        requireActivity().recreate()
           alert.dismiss()

    }

    changeLanguageBinding.rbArabic.setOnClickListener {
        SessionManager.saveLang("ar")
        changelayoutDirection("ar")
        Utils.changeLanguage(requireContext(), "ar")
        requireActivity().recreate()
        alert.dismiss()
    }
}
}