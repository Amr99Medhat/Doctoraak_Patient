package com.doctoraak.doctoraakpatient.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.doctoraak.doctoraakpatient.R
import com.doctoraak.doctoraakpatient.model.Incubation
import com.doctoraak.doctoraakpatient.utils.PageStatus
import com.doctoraak.doctoraakpatient.utils.Utils
import kotlinx.android.synthetic.main.incubation_item.view.*
import java.util.*

class IncubationsAdapter(val context: Context) :
    PagingAdapter<Incubation>()
{
    val colors = arrayOf(R.drawable.circle_text_shape_color1,
        R.drawable.circle_text_shape_color3 ,R.drawable.circle_text_shape_color2
        ,R.drawable.circle_text_shape_color4)

    lateinit var mClickListener: ClickListener

    fun setOnItemClickListener(aClickListener: ClickListener) {
        mClickListener = aClickListener
    }

    interface ClickListener {
        fun onClick(model: Incubation, aView: View)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        when(viewType) {
            PageStatus.NEW_PAGE.value -> MyViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.incubation_item, parent, false))
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

    inner class  MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
        , View.OnClickListener, PagingHolder<Incubation> {
        override fun onClick(v: View?) {
            mClickListener.onClick(list[adapterPosition] , v!!)
        }
        val incuationName = view.tv_name
        val image = view.iv_image
        val availbleCount = view.tv_available_count
        val desc = view.tv_description
        val rate = view.rb_rate

        init {
            itemView.setOnClickListener(this)
        }

        override fun bind(item: Incubation)
        {
            incuationName.text = Utils.getTextForAppLanguage(item.name,item.name_ar
                ,item.name_fr)
            availbleCount.text = "${context.getString(R.string.available_seat)} ${(item.bed_number)}"
            desc.text = Utils.getTextForAppLanguage(item.description,item.description_ar,
                item.description_fr)

            image.text = Utils.getTextForAppLanguage(item.name,item.name_ar
                ,item.name_fr).substring(0 , 1).toUpperCase(Locale.US)

            image.setBackgroundResource(colors[(0..3).random()])
            try { rate.rating = (item.rate).toFloat()
            } catch (e: Exception) {}

            setAnimation(adapterPosition , this)
        }

    }
}