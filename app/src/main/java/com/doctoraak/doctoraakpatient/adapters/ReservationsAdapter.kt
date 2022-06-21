package com.doctoraak.doctoraakpatient.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.doctoraak.doctoraakpatient.R
import com.doctoraak.doctoraakpatient.model.userOrdersModels.Reservation
import com.doctoraak.doctoraakpatient.utils.Utils
import kotlinx.android.synthetic.main.appointment_item.view.*
import kotlin.collections.ArrayList

class ReservationsAdapter(val Reservation: ArrayList<Reservation>, val context: Context) :
    RecyclerView.Adapter<ReservationsAdapter.MyViewHolder>() {

    lateinit var mClickListener: ClickItemRate

    fun setOnItemRateListener(aClickListener: ClickItemRate) {
        mClickListener = aClickListener
    }

    interface ClickItemRate {
        fun onClick(model: Reservation, position: Int, aView: View)
    }

    //click for map
    lateinit var mClickListenerMap: ClickItemMap

    fun setOnItemMapListener(aClickListener: ClickItemMap) {
        mClickListenerMap = aClickListener
    }

    interface ClickItemMap {
        fun onClick(model: Reservation, position: Int, aView: View)
    }

    //cancel order
    lateinit var mClickListenerCancelOrder: ClickCancelOrder

    fun setOnItemCancelOrderListener(aClickListener: ClickCancelOrder) {
        mClickListenerCancelOrder = aClickListener
    }

    interface ClickCancelOrder {
        fun onClick(model: Reservation, position: Int, aView: View)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.appointment_item, parent, false)
        )
    }

    fun clearData() {
        if (Reservation.size > 0) {
            Reservation.clear()
        }
    }

    fun setData(data: ArrayList<Reservation>) {
        clearData()
        Reservation.addAll(data)
        this.notifyDataSetChanged()
    }

    fun deleteListItem(position: Int){
        Reservation.removeAt(position)
        this.notifyItemRemoved(position)
        this.notifyItemRangeChanged(position, this.itemCount - position)
    }


    override fun getItemCount() = Reservation.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val reservation = Reservation[position]

        InializeUI(holder, reservation , position)

        holder.map.setOnClickListener {
            mClickListenerMap.onClick(reservation , position , it)
        }

        setAnimation(position , holder)
    }

    fun setAnimation(position: Int , holder: MyViewHolder){
        val slideLeft = AnimationUtils.loadAnimation(context, R.anim.slide_right)
        val slideright = AnimationUtils.loadAnimation(context, R.anim.slide_left)
        if (position % 2 == 0)
            holder.itemView.startAnimation(slideLeft)
        else
            holder.itemView.startAnimation(slideright)
    }

    private fun InializeUI(holder: MyViewHolder, reservation: Reservation, position : Int) {
        val bookTypes = arrayListOf<String>(
            context.getString(R.string.reservation), context.getString(R.string.consultion), context.getString(R.string.continuee))

        holder.date.text = "${reservation.date} ${reservation.reservation_time.substring(0, 5)}"
        holder.tv_for_me.text = if (reservation.notes.isNullOrEmpty()) context.getString(R.string.for_me) else context.getString(R.string.for_other)

        holder.name.text = Utils.getTextForAppLanguage(reservation.clinic.doctor.name,
            reservation.clinic.doctor.name_ar,reservation.clinic.doctor.name_fr)

        Glide.with(context).load(reservation.clinic.doctor.photo).error(R.drawable.ic_doctor_placeholder)
            .placeholder(R.drawable.ic_doctor_placeholder).into(holder.image)

        holder.type.text = bookTypes[reservation.type.toInt()-1]
        holder.waitinTime.text = "${context.getString(R.string.waiting_time)} :  ${reservation.clinic.waiting_time} ${context.getString(R.string.minutes)}"
        holder.fees.text = "${context.getString(R.string.reservation)} : ${reservation.clinic.fees} ${context.getString(R.string.egp)}"
        holder.fees2.text = "${context.getString(R.string.consultion)} : ${reservation.clinic.fees2} ${context.getString(R.string.egp)}"
        holder.reservationNuber.text = "${context.getString(R.string.reserv_number)} : ${reservation.reservation_number}"
        if (Utils.getDateDiff(Utils.getCurrentDate()!!
                , Utils.getDateFormString(reservation.date)!!).toInt() == -1){
            holder.rate.visibility = View.VISIBLE
            holder.cancelOrder.visibility = View.GONE
            holder.rate.setOnClickListener {
                mClickListener.onClick(reservation , position  , it)
            }
        } else {
            holder.rate.visibility = View.GONE
            holder.cancelOrder.visibility = View.VISIBLE
            holder.cancelOrder.setOnClickListener {
                mClickListenerCancelOrder.onClick(reservation , position , it)
            }
        }

    }


    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image = view.iv_image
        val name = view.tv_doctor_name
        val type = view.tv_reservation_type
        val date = view.tv_reservation_date
        val waitinTime = view.tv_waiting_time
        val fees = view.tv_fees
        val fees2 = view.tv_fees2
        val tv_for_me = view.tv_for_me
        val reservationNuber = view.tv_reservation_number
        val rate = view.iv_rate
        val map = view.iv_address_map
        val cancelOrder = view.tv_cancel_order

    }
}