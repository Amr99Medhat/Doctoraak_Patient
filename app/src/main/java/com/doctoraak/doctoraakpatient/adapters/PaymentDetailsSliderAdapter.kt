package com.doctoraak.doctoraakpatient.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.doctoraak.doctoraakpatient.R
import com.doctoraak.doctoraakpatient.utils.Utils


class PaymentDetailsSliderAdapter(val context: Context, val images : ArrayList<Int> = ArrayList<Int>(),
                                  val texts : ArrayList<ArrayList<String>> = ArrayList<ArrayList<String>>())
    : PagerAdapter() {

    override fun isViewFromObject(view: View, obj: Any) = view == obj

    override fun getCount() = images.size

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.item_payment_details_slider, null)
        val image = view.findViewById<ImageView>(R.id.iv_image)
        val text = view.findViewById<TextView>(R.id.tv_text)

        image.setImageResource(images[position])
        text.setText(texts[if (Utils.getAppLanguage() == "en") position + 4 else position][0])
        val viewPager = container as ViewPager
        viewPager.addView(view, 0)

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        val viewPager = container as ViewPager
        val view = obj as View
        viewPager.removeView(view)
    }
}