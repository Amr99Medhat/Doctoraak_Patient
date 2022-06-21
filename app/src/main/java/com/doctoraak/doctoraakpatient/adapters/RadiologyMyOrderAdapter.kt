package com.doctoraak.doctoraakpatient.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.doctoraak.doctoraakpatient.R
import com.doctoraak.doctoraakpatient.model.RayInfo
import com.doctoraak.doctoraakpatient.model.userOrdersModels.RadiologyDetail
import com.doctoraak.doctoraakpatient.utils.Utils
import kotlinx.android.synthetic.main.lap_order_laps_item.view.*

class RadiologyMyOrderAdapter(val orders: ArrayList<RadiologyDetail>, val context: Context, val raysNames: ArrayList<RayInfo>) :
    RecyclerView.Adapter<RadiologyMyOrderAdapter.MyViewHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.lap_order_laps_item, parent, false)
        )
    }

    fun clearData(){
        if (orders.size > 0){
            orders.clear()
        }
    }

    fun setData(data : ArrayList<RadiologyDetail>){
        clearData()
        orders.addAll(data)
        this.notifyDataSetChanged()
    }

    override fun getItemCount() = orders.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int)
    {
        val order = orders[position]

        raysNames.find { it.id == order.rays_id }?.let {
            holder.name.text = Utils.getTextForAppLanguage(it.name, it.name_ar, it.name_fr)
        }
    }

    inner class  MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
    {
        val name = view.tv_name
        val close = view.iv_close

        init {
            view.iv_close.visibility = View.GONE
        }

    }
}