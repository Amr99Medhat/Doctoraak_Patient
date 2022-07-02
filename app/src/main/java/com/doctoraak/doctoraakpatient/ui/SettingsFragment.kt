package com.doctoraak.doctoraakpatient.ui

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat.recreate
import com.doctoraak.doctoraakpatient.R
import com.doctoraak.doctoraakpatient.databinding.FragmentSettingsBinding
import com.doctoraak.doctoraakpatient.repository.local.SessionManager
import com.doctoraak.doctoraakpatient.utils.Utils


    class SettingsFragment : BaseFragment() {
    private lateinit var binding:FragmentSettingsBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentSettingsBinding.inflate(inflater,container,false)
        setListener()
        return binding.root

    }

    companion object {
        @JvmStatic
        fun newInstance() =
            SettingsFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }


        private fun selectLanguage() {
            val options = arrayOf<CharSequence>(
                getString(R.string.english), getString(R.string.arabic), getString(R.string.cancel)
            )

                    val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.choose_langaue))

        builder.setItems(options, DialogInterface.OnClickListener { dialog, item ->
            if (options[item].equals(getString(R.string.english))) {
                SessionManager.saveLang("en")
                changelayoutDirection("en")
                Utils.changeLanguage(requireContext(), "en")
                requireActivity().recreate()
            } else if (options[item].equals(getString(R.string.arabic))) {
                SessionManager.saveLang("ar")
                changelayoutDirection("ar")
                Utils.changeLanguage(requireContext(), "ar")
               requireActivity().recreate()
            } else {
                dialog.dismiss()
            }
        })
        builder.show()
        }
private fun setListener(){
    binding.btnChangeLanguage.setOnClickListener {
        selectLanguage()
    }
}


}