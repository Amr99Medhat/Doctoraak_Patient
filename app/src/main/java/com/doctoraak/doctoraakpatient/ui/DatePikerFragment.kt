package com.doctoraak.doctoraakpatient.ui

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.doctoraak.doctoraakpatient.R
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class DatePikerFragment(var fragment: Boolean) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dialog: DatePickerDialog?
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        dialog = if (fragment) {
            DatePickerDialog(requireActivity(), R.style.DateDialogTheme,
                parentFragment as DatePickerDialog.OnDateSetListener?,
                year, month, day)
        } else {
            DatePickerDialog(requireActivity(), R.style.DateDialogTheme,
                activity as DatePickerDialog.OnDateSetListener?,
                year, month, day)
        }

        dialog.apply {
            setOnShowListener { a__ ->
                getButton(DialogInterface.BUTTON_POSITIVE).apply {
                    setBackgroundColor(ContextCompat.getColor(requireActivity(),
                        R.color.transparent))
                    setTextColor(ContextCompat.getColor(requireActivity(), R.color.black))
                }
                getButton(DialogInterface.BUTTON_NEGATIVE).apply {
                    setBackgroundColor(ContextCompat.getColor(requireActivity(),
                        R.color.transparent))
                    setTextColor(ContextCompat.getColor(requireActivity(), R.color.black))

                }
            }
        }

        return dialog
    }


}