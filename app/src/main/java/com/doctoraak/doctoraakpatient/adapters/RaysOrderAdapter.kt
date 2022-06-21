package com.doctoraak.doctoraakpatient.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.doctoraak.doctoraakpatient.R
import kotlinx.android.synthetic.main.lap_order_laps_item.view.*


class RaysOrderAdapter(val names: ArrayList<String>, val context: Context) :
    RecyclerView.Adapter<RaysOrderAdapter.MyViewHolder>() {


    lateinit var mClickListener: ClickListener

    fun setOnItemClickDeleteListener(aClickListener: ClickListener) {
        mClickListener = aClickListener
    }

    interface ClickListener {
        fun onClick(model: String, position: Int,aView: View)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.radiology_order_radio_item, parent, false)
        )
    }

    fun clearData(){
        if (names.size > 0){
            names.clear()
        }
    }

    fun setData(data : ArrayList<String>){
        clearData()
        names.addAll(data)
        this.notifyDataSetChanged()
    }


    override fun getItemCount() = names.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val name = names[position]
        holder.name.text = name
        holder.close.setOnClickListener {
            mClickListener.onClick(name ,position, it)
        }
    }

    inner class  MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
    {
        val name = view.tv_name
        val close = view.iv_close
    }
}