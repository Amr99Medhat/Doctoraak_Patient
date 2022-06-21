package com.doctoraak.doctoraakpatient.adapters

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.doctoraak.doctoraakpatient.R
import com.doctoraak.doctoraakpatient.model.userOrdersModels.LabOrder
import com.doctoraak.doctoraakpatient.utils.DividerItemDecorationExceptLast
import com.doctoraak.doctoraakpatient.utils.Utils
import kotlinx.android.synthetic.main.pharmacy_order_item.view.*

class LabOrdersAdapter(val order: ArrayList<LabOrder>, val context: Context)
    : RecyclerView.Adapter<LabOrdersAdapter.MyViewHolder>()
{

    //cancel order
    lateinit var mClickListenerCancelOrder: ClickCancelOrder

    fun setOnItemCancelOrderListener(aClickListener: ClickCancelOrder) {
        mClickListenerCancelOrder = aClickListener
    }

    interface ClickCancelOrder {
        fun onClick(model: LabOrder, position: Int, aView: View)
    }

    //view photo pic
    lateinit var mClickListenerViewPhoto: ClickViewPhoto

    fun setOnItemViewPhotoListener(aClickListener: ClickViewPhoto) {
        mClickListenerViewPhoto = aClickListener
    }

    interface ClickViewPhoto {
        fun onClick(url : String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.lab_order_item, parent, false)
        )
    }

    fun clearData() {
        if (order.size > 0) {
            order.clear()
        }
    }

    fun setData(data: ArrayList<LabOrder>) {
        clearData()
        order.addAll(data)
        this.notifyDataSetChanged()
    }

    fun deleteListItem(position: Int){
        order.removeAt(position)
        this.notifyItemRemoved(position)
        this.notifyItemRangeChanged(position, this.itemCount - position)
    }


    override fun getItemCount() = order.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int)
    {
        val reservation = order[position]

        InializeUI(holder, reservation)

        setAnimation(position , holder)

        holder.cancelOrder.setOnClickListener {
            mClickListenerCancelOrder.onClick(reservation , position , it)
        }
        holder.image.setOnClickListener {
            mClickListenerViewPhoto.onClick(reservation.photo)
        }
    }

    fun setAnimation(position: Int , holder: MyViewHolder){
        val slideLeft = AnimationUtils.loadAnimation(context, R.anim.slide_right)
        val slideright = AnimationUtils.loadAnimation(context, R.anim.slide_left)
        if (position % 2 == 0)
            holder.itemView.startAnimation(slideLeft)
        else
            holder.itemView.startAnimation(slideright)

    }

    private fun InializeUI(holder: MyViewHolder, reservation: LabOrder)
    {
        holder.date.text = reservation.created_at

        holder.image.visibility = if (reservation.photo.isNullOrBlank())
            View.GONE
        else
        {
            Glide.with(context).load(reservation.photo).listener(object :
                RequestListener<Drawable?> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable?>?,
                    isFirstResource: Boolean
                ): Boolean {
                    holder.image.scaleType = ImageView.ScaleType.CENTER_CROP
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable?>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }
            }).error(R.drawable.ic_lab_order)
                .placeholder(R.drawable.ic_lab_order).into(holder.image)
            View.VISIBLE
        }

        with(holder.rv_medicines)
        {
            layoutManager = LinearLayoutManager(context)
            if (reservation.details != null) {
                adapter = LabMyOrderAdapter(reservation.details, context, Utils.getAnaysis().data)
            }
            addItemDecoration(DividerItemDecorationExceptLast(context
                    , ResourcesCompat.getDrawable(resources, R.drawable.drawable_divider, null)!!), -1)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
    {
        val image = view.iv_medicine_image
        val date = view.tv_date
        val iv_expand = view.iv_expand
        val rv_medicines = view.rv_medicines
        val cancelOrder = view.tv_cancel_order
    }
}