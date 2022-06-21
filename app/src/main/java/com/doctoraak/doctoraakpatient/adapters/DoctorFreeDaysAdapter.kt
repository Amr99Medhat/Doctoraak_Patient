package com.doctoraak.doctoraakpatient.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.doctoraak.doctoraakpatient.R
import com.doctoraak.doctoraakpatient.model.FreeDays
import com.doctoraak.doctoraakpatient.utils.show
import kotlinx.android.synthetic.main.doctor_free_days_items.view.*

class DoctorFreeDaysAdapter(val freedays: ArrayList<FreeDays>, val context: Context, val onItemClick: (FreeDays)->Unit) :
    RecyclerView.Adapter<DoctorFreeDaysAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.doctor_free_days_items, parent, false)
        )
    }


    fun clearData() {
        if (freedays.size > 0) {
            freedays.clear()
        }
    }

    fun setData(data: ArrayList<FreeDays>) {
        clearData()
        freedays.addAll(data)
        this.notifyDataSetChanged()
    }

    override fun getItemCount() = freedays.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val freeday = freedays[position]
        holder.date.text = freeday.date
        val shift = freeday.part_id
        if (shift == 1) {
            holder.viewShift2.visibility = View.GONE
            changeTextViewColor(holder.shift1, R.color.white)
            changeTextViewColor(holder.shift2, R.color.black)
        } else if (shift == 2) {
            holder.viewShift1.visibility = View.GONE
            changeTextViewColor(holder.shift2, R.color.white)
            changeTextViewColor(holder.shift1, R.color.black)
        } else {
            holder.viewShift1.visibility = View.GONE
            holder.viewShift2.visibility = View.GONE
            changeTextViewColor(holder.shift2, R.color.white)
            changeTextViewColor(holder.shift1, R.color.white)
            holder.allAvailbleshifts.show()
        }
    }

    protected fun changeTextViewColor(view: TextView, color: Int) {
        view.setTextColor(ContextCompat.getColor(context, color))
    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val date = view.tv_free_day_date
        val shift1 = view.shift1
        val shift2 = view.shift2
        val viewShift1 = view.view_shift1
        val viewShift2 = view.view_shift2
        val allAvailbleshifts = view.view_shifts_availble

        init {
            itemView.setOnClickListener {
                onItemClick(freedays[adapterPosition])
            }
        }
    }
}