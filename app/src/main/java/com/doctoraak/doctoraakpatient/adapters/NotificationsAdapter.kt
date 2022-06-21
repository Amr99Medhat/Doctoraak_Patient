package com.doctoraak.doctoraakpatient.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.doctoraak.doctoraakpatient.R
import com.doctoraak.doctoraakpatient.model.NotificationInfo
import com.doctoraak.doctoraakpatient.utils.Utils
import kotlinx.android.synthetic.main.notification_item.view.*

class NotificationsAdapter(val notifications: ArrayList<NotificationInfo>, val context: Context) :
    RecyclerView.Adapter<NotificationsAdapter.MyViewHolder>() {


    lateinit var mClickListener: ClickListener

    fun setOnItemClickListener(aClickListener: ClickListener) {
        mClickListener = aClickListener
    }

    interface ClickListener {
        fun onClick(model: NotificationInfo, aView: View)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.notification_item, parent, false)
        )
    }

    fun clearData(){
        if (notifications.size > 0){
            notifications.clear()
        }
    }

    fun setData(data : ArrayList<NotificationInfo>){
        clearData()
        notifications.addAll(data)
        this.notifyDataSetChanged()
    }

    fun deleteItem(position: Int){
        notifications.removeAt(position)
        this.notifyItemRemoved(position)
        this.notifyItemRangeRemoved(position , this.itemCount - position)
    }

    override fun getItemCount() = notifications.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val notification = notifications[position]
        holder.date.text = Utils.getNiceTimeFormat(context,notification.created_at)
        holder.title.text = Utils.getTextForAppLanguage(notification.title_en , notification.title_ar , notification.title_en)
        holder.body.text = Utils.getTextForAppLanguage(notification.message_en , notification.message_ar , notification.message_en)
        setAnimation(position , holder)
        holder.itemView.tag = "${notification.id}&$position"
    }
    fun setAnimation(position: Int , holder: MyViewHolder){
        val slideLeft = AnimationUtils.loadAnimation(context, R.anim.slide_right)
        val slideright = AnimationUtils.loadAnimation(context, R.anim.slide_left)
        if (position % 2 == 0)
            holder.itemView.startAnimation(slideLeft)
        else
            holder.itemView.startAnimation(slideright)

    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) ,View.OnClickListener{
        override fun onClick(v: View?) {
            mClickListener.onClick(notifications[adapterPosition] , v!!)
        }

        val image = view.iv_image
        val title = view.tv_title
        val body = view.tv_body
        val date = view.tv_date

        init {
            itemView.setOnClickListener(this)
        }
    }
}