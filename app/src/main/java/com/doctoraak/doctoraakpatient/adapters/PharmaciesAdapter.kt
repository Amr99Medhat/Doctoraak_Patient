package com.doctoraak.doctoraakpatient.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.doctoraak.doctoraakpatient.R
import com.doctoraak.doctoraakpatient.model.Clinic
import com.doctoraak.doctoraakpatient.model.Pharmacy
import com.doctoraak.doctoraakpatient.utils.PageStatus
import com.doctoraak.doctoraakpatient.utils.Utils
import com.doctoraak.doctoraakpatient.utils.toBoolean
import kotlinx.android.synthetic.main.pharmacy_list_item.view.*
import java.util.*

class PharmaciesAdapter(val context: Context) : PagingAdapter<Pharmacy>()
{

    lateinit var mClickListener: ClickListener

    fun setOnItemClickListener(aClickListener: ClickListener) {
        mClickListener = aClickListener
    }

    interface ClickListener {
        fun onClick(model: Pharmacy)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when(viewType) {
        PageStatus.NEW_PAGE.value -> MyViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.pharmacy_list_item, parent, false))
        else -> super.onCreateViewHolder(parent, viewType)
    }

    fun setAnimation(position: Int, holder: MyViewHolder) {
        val slideLeft = AnimationUtils.loadAnimation(context, R.anim.slide_right)
        val slideright = AnimationUtils.loadAnimation(context, R.anim.slide_left)
        if (position % 2 == 0)
            holder.itemView.startAnimation(slideLeft)
        else
            holder.itemView.startAnimation(slideright)

    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
        , View.OnClickListener, PagingHolder<Pharmacy> {
        override fun onClick(v: View?) {
            mClickListener.onClick(list[adapterPosition])
        }

        init {
            itemView.setOnClickListener(this)
        }

        val name = view.tv_name
        val image = view.iv_image
        val address = view.tv_address
        val delivery = view.tv_delivery
        
        
        override fun bind(item: Pharmacy) {
            name.text = Utils.getTextForAppLanguage(item.name , item.nameAr , item.nameFr)
            Glide.with(context).load(item.photo).error(R.drawable.ic_pharmacy).into(image)
            address.text = Utils.getAddress(item.area , item.city)
            if (!item.delivery.toBoolean())
                delivery.text = context.getText(R.string.delivery_not_available)

            setAnimation(adapterPosition , this)
        }

    }
}