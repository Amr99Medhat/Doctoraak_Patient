package com.doctoraak.doctoraakpatient.customView

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.databinding.DataBindingUtil
import com.doctoraak.doctoraakpatient.R
import com.doctoraak.doctoraakpatient.databinding.DialogSweetBinding
import com.doctoraak.doctoraakpatient.ui.BaseFragment
import com.doctoraak.doctoraakpatient.utils.hide
import com.doctoraak.doctoraakpatient.utils.show
import com.doctoraak.doctoraakpatient.utils.toSpannedText

class SweetDialog(context: Context, val dialogType: DialogType) : Dialog(context)
{
    private lateinit var binding: DialogSweetBinding

    companion object{
        @JvmStatic
        fun newInstance(context: Context, dialogType: DialogType = DialogType.NORMAL) =
            SweetDialog(context, dialogType)
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.inflate(LayoutInflater.from(context)
            , R.layout.dialog_sweet, null, false)
        setContentView(binding.root)

        binding.btnOk.setOnClickListener { dismiss() }
        binding.btnCancel.setOnClickListener { dismiss() }

        handleDialogStatus()
    }

    private fun handleDialogStatus()
    {
        val setLottieImage: (name: String)-> Unit = {name->
            binding.lottieImage.setAnimation(name)
            binding.lottieImage.repeatCount = 1
            binding.lottieImage.show()
            binding.ivImage.setImageResource(R.color.white)
        }

        when (dialogType)
        {
            DialogType.SUCCESS ->
            {
                binding.btnCancel.hide()
                binding.tvTitle.text = context.getString(R.string.success)
                setLottieImage("success.json")
            }
            DialogType.WARNING ->
            {
                binding.btnCancel.hide()
                binding.tvTitle.text = context.getString(R.string.warning)
                setLottieImage("warning.json")
            }
            DialogType.ERROR ->
            {
                binding.btnCancel.hide()
                binding.tvTitle.text = context.getString(R.string.error)
                setLottieImage("error.json")
            }
            DialogType.LOADING ->
            {
                setCancelable(false)
                binding.btnCancel.hide()
                binding.btnOk.hide()
                binding.tvTitle.text = context.getString(R.string.loading)
                binding.tvBody.hide()
                setLottieImage("heart_loading.json")
            }
            DialogType.NORMAL ->
            {
                binding.tvTitle.text = context.getString(R.string.warning)
                binding.lottieImage.hide()
                binding.ivImage.setBackgroundResource(R.drawable.ic_logo)
            }
        }
    }

    override fun onStart()
    {
        super.onStart()
        window?.let {
            it.setLayout(MATCH_PARENT, WRAP_CONTENT)
            it.setGravity(Gravity.CENTER)
            it.setBackgroundDrawableResource(R.color.transparent)
            it.setWindowAnimations(R.style.dialogAnimation)
        }
    }

    override fun setTitle(title: CharSequence?) {
        binding.tvTitle.text = title
    }

    fun setMessage(title: CharSequence?) {
        binding.tvBody.text = title.toString().toSpannedText()
    }

    fun setCustomImage(resId: Int)
    {
        binding.ivImage.setImageResource(resId)
        binding.lottieImage.hide()
    }

    fun setOkClickListener(okListener: View.OnClickListener) =
        binding.btnOk.setOnClickListener(okListener)

    fun setCancelClickListener(cancelListener: View.OnClickListener) =
        binding.btnCancel.setOnClickListener(cancelListener)

    fun positiveTextButton(positiveText: String) {
        binding.btnOk.text = positiveText
    }

    fun negativeTextButton(negativeText: String) {
        binding.btnCancel.text = negativeText
    }


}

enum class DialogType
{
    WARNING, SUCCESS, ERROR, LOADING, NORMAL
}