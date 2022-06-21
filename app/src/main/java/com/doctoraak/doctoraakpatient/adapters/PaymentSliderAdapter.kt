package com.doctoraak.doctoraakpatient.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.doctoraak.doctoraakpatient.R


class PaymentSliderAdapter(val context: Context,val images : ArrayList<Int> = ArrayList<Int>())
    : PagerAdapter() {

    override fun isViewFromObject(view: View, obj: Any) = view == obj

    override fun getCount() = images.size

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.item_payment_slider, null)
        val image = view.findViewById<ImageView>(R.id.iv_image_slider)
        image.setImageResource(images[position])
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