package com.doctoraak.doctoraakpatient.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.doctoraak.doctoraakpatient.R
import com.doctoraak.doctoraakpatient.model.MedicineOrder
import kotlinx.android.synthetic.main.pharmacy_order_medicine_item.view.*

class MedicinesOrderAdapter(val orders: ArrayList<MedicineOrder>, val context: Context) :
    RecyclerView.Adapter<MedicinesOrderAdapter.MyViewHolder>() {


    lateinit var mClickListener: ClickListener

    fun setOnItemClickDeleteListener(aClickListener: ClickListener) {
        mClickListener = aClickListener
    }

    interface ClickListener {
        fun onClick(model: MedicineOrder, position: Int,aView: View)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.pharmacy_order_medicine_item, parent, false)
        )
    }

    fun clearData(){
        if (orders.size > 0){
            orders.clear()
        }
    }

    fun setData(data : ArrayList<MedicineOrder>){
        clearData()
        orders.addAll(data)
        this.notifyDataSetChanged()
    }


    override fun getItemCount() = orders.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val order = orders[position]

        holder.name.text = order.name
        holder.type.text = order.type
        holder.quantity.text = order.count

        holder.close.setOnClickListener {
            mClickListener.onClick(order ,position, it)
        }
    }

    inner class  MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
    {
        val name = view.tv_name
        val quantity = view.tv_quantity
        val type = view.tv_type
        val close = view.iv_close

    }
}