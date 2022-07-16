package com.doctoraak.doctoraakpatient.ui

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import com.doctoraak.doctoraakpatient.R
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*


class DatePikerFragment(var fragment: Boolean) : DialogFragment(),
    DatePickerDialog.OnDateSetListener {
    private val calendar = Calendar.getInstance()
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dialog: DatePickerDialog?

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        dialog = if (fragment) {
            DatePickerDialog(requireActivity(), this, year, month, day)
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

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        val selectedDate = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(calendar.time)
        val selectedBundle = Bundle()
        selectedBundle.putString("SELECTED_DATE", selectedDate)
        setFragmentResult("REQUEST_KEY", selectedBundle)

    }


}