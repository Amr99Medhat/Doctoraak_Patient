package com.doctoraak.doctoraakpatient.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.doctoraak.doctoraakpatient.R
import com.doctoraak.doctoraakpatient.model.Medicine
import com.doctoraak.doctoraakpatient.model.MedicineType
import com.doctoraak.doctoraakpatient.model.userOrdersModels.Detail
import com.doctoraak.doctoraakpatient.utils.Utils
import kotlinx.android.synthetic.main.pharmacy_order_medicine_item.view.*

class MedicinesMyOrderAdapter(
    val orders: ArrayList<Detail>, val context: Context, val medicineNames: ArrayList<Medicine>
    , val medicineType: ArrayList<MedicineType>) :
    RecyclerView.Adapter<MedicinesMyOrderAdapter.MyViewHolder>()
{

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

    fun setData(data : ArrayList<Detail>){
        clearData()
        orders.addAll(data)
        this.notifyDataSetChanged()
    }

    override fun getItemCount() = orders.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int)
    {
        val order = orders[position]

        medicineNames.find { it.id == order.medicine_id }?.let {
            holder.name.text = Utils.getTextForAppLanguage(it.name, it.name_ar, it.name_fr)
        }

        medicineType.find { it.id == order.medicine_type_id }?.let {
            holder.type.text = Utils.getTextForAppLanguage(it.name, it.name_ar, it.name_fr)
        }

        holder.quantity.text = order.amount.toString()
    }

    inner class  MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
    {
        val name = view.tv_name
        val quantity = view.tv_quantity
        val type = view.tv_type
        val close = view.iv_close

        init {
            view.iv_close.visibility = View.GONE
        }

    }
}