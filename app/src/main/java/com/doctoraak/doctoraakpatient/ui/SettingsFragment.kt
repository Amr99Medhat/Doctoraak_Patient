package com.doctoraak.doctoraakpatient.ui

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        arguments?.let {

        }
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
private fun setListener(){
    binding.btnChangeLanguage.setOnClickListener {
        showChangeLanguageDialog()
    }
    changeLanguageBinding.rbEnglish.setOnClickListener {
        SessionManager.saveLang("en")
        changelayoutDirection("en")
        Utils.changeLanguage(requireContext(), "en")
           alert.dismiss()
        requireActivity().recreate()
    }

    changeLanguageBinding.rbArabic.setOnClickListener {
        SessionManager.saveLang("ar")
        changelayoutDirection("ar")
        Utils.changeLanguage(requireContext(), "ar")
        alert.dismiss()
        requireActivity().recreate()
    }
}
}