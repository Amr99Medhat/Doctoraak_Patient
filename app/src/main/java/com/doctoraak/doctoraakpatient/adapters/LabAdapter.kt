package com.doctoraak.doctoraakpatient.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.doctoraak.doctoraakpatient.R
import com.doctoraak.doctoraakpatient.model.Lab
import com.doctoraak.doctoraakpatient.utils.PageStatus
import com.doctoraak.doctoraakpatient.utils.Utils
import com.doctoraak.doctoraakpatient.utils.toBoolean
import kotlinx.android.synthetic.main.lap_item.view.*


class LabAdapter(val context: Context) : PagingAdapter<Lab>()
{
    lateinit var mClickListener: ClickListener

    fun setOnItemClickListener(aClickListener: ClickListener) {
        mClickListener = aClickListener
    }

    interface ClickListener {
        fun onClick(model: Lab, aView: View)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when(viewType)
    {
        PageStatus.NEW_PAGE.value -> MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.lap_item, parent, false))
        else -> super.onCreateViewHolder(parent, viewType)
    }

    fun setAnimation(position: Int , holder: MyViewHolder){
        val slideLeft = AnimationUtils.loadAnimation(context, R.anim.slide_right)
        val slideright = AnimationUtils.loadAnimation(context, R.anim.slide_left)
        if (position % 2 == 0)
            holder.itemView.startAnimation(slideLeft)
        else
            holder.itemView.startAnimation(slideright)
    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
        , View.OnClickListener, PagingHolder<Lab>
    {
        override fun onClick(v: View?) {
            mClickListener.onClick(list[adapterPosition], v!!)
        }

        val name = view.tv_name
        val adress = view.tv_address
        val image = view.iv_image
        val availablity = view.tv_delivery

        init {
            itemView.setOnClickListener(this)
        }

        override fun bind(lab: Lab)
        {
            val address = "${Utils.getAreaName((lab.area).toInt())} " +
                    ", ${Utils.getCityName((lab.city).toInt())}"

            name.text = Utils.getTextForAppLanguage(lab.name,lab.nameAr,lab.nameFr)
            adress.text = address

            Glide.with(context).load(lab.photo).placeholder(R.drawable.ic_lab)
                .error(R.drawable.ic_lab)
                .into(image)

            availablity.visibility = if (lab.delivery.toBoolean()) View.VISIBLE else View.GONE
            setAnimation(adapterPosition, this)
        }
    }
}