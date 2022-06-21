package com.doctoraak.doctoraakpatient.ui


import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.doctoraak.doctoraakpatient.R
import com.doctoraak.doctoraakpatient.utils.OrderType
import com.doctoraak.doctoraakpatient.utils.show
import kotlinx.android.synthetic.main.fragment_view_photo.*

/**
 * A simple [Fragment] subclass.
 */
class ViewPhotoFragment : DialogFragment() {

    var url = ""
    var type = ""
    companion object{
        @JvmStatic
        fun newInstance(url : String,type : String ) : ViewPhotoFragment{
            val dialog = ViewPhotoFragment()
            dialog.apply {
                arguments = Bundle().apply {
                    putString("urlPhotoKey" , url)
                    putString("type" , type)
                }
            }
            dialog.setStyle(STYLE_NORMAL, R.style.FullScreenDialogTheme)
            return dialog
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val myView = inflater.inflate(R.layout.fragment_view_photo, container, false)
        arguments.let {
            url = it!!.getString("urlPhotoKey")!!
            type = it.getString("type")!!
        }

        return myView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val temp = getImageDrawableOnError(type)
        loading.show()
        Glide.with(activity!!).load(url).listener(object :
            RequestListener<Drawable?> {
            override fun onLoadFailed(
                e: GlideException?, model: Any?, target: Target<Drawable?>?, isFirstResource: Boolean): Boolean {
                loading.visibility = View.GONE
                return false
            }

            override fun onResourceReady(
                resource: Drawable?, model: Any?,
                target: Target<Drawable?>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                loading.visibility = View.GONE
                return false
            }
        })
            .error(temp).into(iv_viewed_photo)
    }

    fun getImageDrawableOnError(type : String) :Int{
        if (type.equals(OrderType.PHARMACY.toString()))
            return R.drawable.ic_pharmacy_order
        else if (type.equals(OrderType.LAB.toString()))
            return R.drawable.ic_lab_order
        else
        return R.drawable.ic_radiology_order
    }

    override fun onResume() {
        super.onResume()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.setDimAmount(0f)
    }
}
