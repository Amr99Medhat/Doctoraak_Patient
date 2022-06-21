package com.doctoraak.doctoraakpatient.utils

import android.content.Context
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.text.TextUtils
import android.view.View
import android.view.View.*
import android.widget.AutoCompleteTextView
import android.widget.Button
import com.doctoraak.doctoraakpatient.R
import com.doctoraak.doctoraakpatient.model.BaseResponse
import com.doctoraak.doctoraakpatient.repository.remote.BaseCallBack
import com.doctoraak.doctoraakpatient.repository.remote.BaseResponseListener
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
//in code : val s = str1 attach str2
infix fun String.attach(text: String) = "$this $text"

fun String.toSpannedText(): Spanned {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        return Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
    } else {
        @Suppress("DEPRECATION")
        return Html.fromHtml(this)
    }
}

fun Boolean.toInt() = if (this) 1 else 0

fun Int.toBoolean() = this == 1

fun BaseResponse.getMsg(): String {
    if (Utils.getAppLanguage().equals("ar"))
        return this.message ?: ""
    else
        return this.message_en ?: ""
}

fun Button.enable() {
    this.isEnabled = true
}

fun Button.disable() {
    this.isEnabled = false
}

fun <T> Call<T>.start(listener: BaseResponseListener<T>) {
    listener.onLoading()
    this.enqueue(BaseCallBack(listener))
}

fun View.animateOutInX(view_in: View, duration: Long = 400, translate_x: Float = 350f) {
    animate()
        .setDuration(duration)
        .translationX(-translate_x)
        .alpha(0f)
        .start()

    this.visibility = View.GONE
    with(view_in) {
        translationX = translate_x
        visibility = View.VISIBLE
        alpha = 0f
        animate()
            .setDuration(duration)
            .setStartDelay(100)
            .translationX(0f)
            .alpha(1f)
            .start()
    }
}

fun String.validateName(context: Context, textInputLayout: TextInputLayout): Boolean {
    if (TextUtils.isEmpty(this)) {
        textInputLayout.error = context.getString(R.string.name) + " " +
                context.getString(R.string.is_not_valid)

        return false
    } else {
        textInputLayout.error = null
        return true
    }
}

fun String.validateEmpty(context: Context, textInputLayout: TextInputLayout): Boolean {
    if (TextUtils.isEmpty(this)) {
        textInputLayout.error = context.getString(R.string.field) + " " + context.getString(R.string.is_not_valid)
        return false
    } else {
        textInputLayout.error = null
        return true
    }
}

fun String.validateFullName(context: Context, textInputLayout: TextInputLayout): Boolean {
    val split = this.split(" ")
    if (split.size < 2) {
        textInputLayout.error = context.getString(R.string.first_last_name_must_entered)
        return false
    } else {
        textInputLayout.error = null
        return true
    }
}

fun String.validateEmptyWithoutError(context: Context, textInputLayout: TextInputLayout): Boolean {
    if (TextUtils.isEmpty(this)) {
        textInputLayout.error = " "
        return false
    } else {
        textInputLayout.error = null
        return true
    }
}

fun String.validateInsurranceCode(context: Context, textInputLayout: TextInputLayout): Boolean {
    if (TextUtils.isEmpty(this)) {
        textInputLayout.error = context.getString(R.string.field) + " " +
                context.getString(R.string.is_not_valid)

        return false
    } else {
        textInputLayout.error = null
        return true
    }
}

fun String.validateEmptyAddress(context: Context, textInputLayout: TextInputLayout): Boolean {
    if (TextUtils.isEmpty(this)) {
        textInputLayout.error = context.getString(R.string.address) + " " +
                context.getString(R.string.is_not_valid)

        return false
    } else {
        textInputLayout.error = null
        return true
    }
}

fun String.validateEmail(context: Context, textInputLayout: TextInputLayout): Boolean {
    if (TextUtils.isEmpty(this)) {
        textInputLayout.error = context.getString(R.string.email) + " " + context.getString(R.string.is_not_valid)
        return false
    } else {
        textInputLayout.error = null
        return true
    }
}

fun String.validateAge(context: Context, textInputLayout: TextInputLayout): Boolean {
    if (TextUtils.isEmpty(this)) {
        textInputLayout.error = context.getString(R.string.age) + " " +
                context.getString(R.string.is_not_valid)

        return false
    } else {
        textInputLayout.error = null
        return true
    }
}

fun String.validateDate(context: Context, textInputLayout: TextInputLayout): Boolean {
    if (TextUtils.isEmpty(this)) {
        textInputLayout.error = context.getString(R.string.date) + " " + context.getString(R.string.is_not_valid)

        return false
    } else {
        textInputLayout.error = null
        return true
    }
}


fun Int.validateBookType(context: Context, textInputLayout: TextInputLayout): Boolean {
    if (this.equals(-1)) {
        textInputLayout.error = context.getString(R.string.book_type) + " " +
                context.getString(R.string.is_not_valid)

        return false
    } else {
        textInputLayout.error = null
        return true
    }
}

fun Int.validateShift(context: Context, textInputLayout: TextInputLayout): Boolean {
    if (this.equals(-1)) {
        textInputLayout.error = context.getString(R.string.shift) + " " + context.getString(R.string.is_not_valid)
        return false
    } else {
        textInputLayout.error = null
        return true
    }
}

fun Int.validateCity(context: Context, textInputLayout: TextInputLayout): Boolean {
    if (this.equals(-1)) {
        textInputLayout.error = context.getString(R.string.city) + " " +
                context.getString(R.string.is_not_valid)

        return false
    } else {
        textInputLayout.error = null
        return true
    }
}


fun Int.validateInsurranceId(context: Context, textInputLayout: TextInputLayout): Boolean {
    if (this.equals(-1)) {
        textInputLayout.error = context.getString(R.string.insurance) + " " +
                context.getString(R.string.is_not_valid)

        return false
    } else {
        textInputLayout.error = null
        return true
    }
}

// This will always return true cause i want to accept city as input without area;
fun Int.validateArea(context: Context, textInputLayout: TextInputLayout): Boolean {
    return true

//    if (this.equals(-1)) {
//        textInputLayout.error = context.getString(R.string.area) + " " +
//                context.getString(R.string.is_not_valid)
//
//        return false
//    } else {
//        textInputLayout.error = null
//        return true
//    }
}

fun Double?.validateLattLang(
    context: Context,
    lang: Double?,
    textInputLayout: TextInputLayout
): Boolean {
    if (this == null || lang == null) {
        textInputLayout.error = ""
        return false
    } else {
        textInputLayout.error = null
        return true
    }
}


fun String.validatePhone(context: Context, textInputLayout: TextInputLayout): Boolean {
    if (length != 11) {
        textInputLayout.error = context.getString(R.string.phone_number) + " " +
                context.getString(R.string.is_not_valid)

        return false
    } else {
        textInputLayout.error = null
        return true
    }
}

fun String.validatePassword(context: Context, textInputLayout: TextInputLayout): Boolean {
    if (length < 4) {
        textInputLayout.error =
            context.getString(R.string.password) + " " + context.getString(R.string.password_is_not_valid)
        return false
    } else {
        textInputLayout.error = null
        return true
    }
}

fun String.validatePasswordAndConfirmPassword(
    context: Context, textInputLayout: TextInputLayout
    , confirm_password: String, textInputLayoutConfirm: TextInputLayout): Boolean {
    if (length < 8)
        textInputLayout.error =
            context.getString(R.string.password) + " " + context.getString(R.string.is_not_valid)
    else {
        textInputLayout.error = null

        if (!TextUtils.equals(this, confirm_password))
            textInputLayoutConfirm.error = context.getString(R.string.confirm_password) + " " +
                    context.getString(R.string.not_match_password_field)
        else {
            textInputLayoutConfirm.error = null
            return true
        }
    }

    return false
}

fun String.validateVerificationCode(context: Context, textInputLayout: TextInputLayout): Boolean {
    if (TextUtils.isEmpty(this)) {
        textInputLayout.error =
            context.getString(R.string.verification_code) + " " + context.getString(R.string.is_not_valid)
        return false
    } else {
        textInputLayout.error = null
        return true
    }
}

fun View?.show() {
    this?.visibility = VISIBLE
}

fun View.hide() {
    this.visibility = GONE
}

fun View.invisible() {
    this.visibility = INVISIBLE
}

fun AutoCompleteTextView.removeSelectedItem(position: Int)
{
    setText("")
    setSelection(0)
    listSelection = 0
}
