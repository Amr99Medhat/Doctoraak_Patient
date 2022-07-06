package com.doctoraak.doctoraakpatient.ui

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.doctoraak.doctoraakpatient.R
import java.util.*



class DatePikerFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val dialog = DatePickerDialog(requireActivity(), R.style.DateDialogTheme,
            activity as DatePickerDialog.OnDateSetListener?,
            year, month, day).apply {
            setOnShowListener { a__ ->
                getButton(DialogInterface.BUTTON_POSITIVE).apply {
                    setBackgroundColor(ContextCompat.getColor(requireActivity() , R.color.transparent))
                    setTextColor(ContextCompat.getColor(requireActivity() , R.color.black))
                }
                getButton(DialogInterface.BUTTON_NEGATIVE).apply {
                    setBackgroundColor(ContextCompat.getColor(requireActivity() , R.color.transparent))
                    setTextColor(ContextCompat.getColor(requireActivity() , R.color.black))

                }
            }
        }

        return dialog
    }
}