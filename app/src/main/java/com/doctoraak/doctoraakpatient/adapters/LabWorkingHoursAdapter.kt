package com.doctoraak.doctoraakpatient.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.doctoraak.doctoraakpatient.R
import com.doctoraak.doctoraakpatient.model.LabWorkingHour
import com.doctoraak.doctoraakpatient.utils.Utils
import kotlinx.android.synthetic.main.show_working_dates_item.view.*

class LabWorkingHoursAdapter(val workingHours: ArrayList<LabWorkingHour>, val context: Context) :
    RecyclerView.Adapter<LabWorkingHoursAdapter.MyViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.show_working_dates_item, parent, false)
        )
    }

    fun clearData(){
        if (workingHours.size > 0){
            workingHours.clear()
        }
    }

    fun setData(data : ArrayList<LabWorkingHour>){
        clearData()
        workingHours.addAll(data)
        this.notifyDataSetChanged()
    }


    override fun getItemCount() = workingHours.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val workingHour = workingHours[position]

        holder.day.text = Utils.getDayOfWeek(workingHour.day , context)

        holder.shift1From.text = Utils.convertTimeto12Format(workingHour.part_from)
        holder.shift1To.text = Utils.convertTimeto12Format(workingHour.part_to)


    }

    inner class  MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
        {

        val day = view.tv_day
        val shift1From = view.tv_shift_1_from
        val shift1To = view.tv_shift_1_to
        val shift2From = view.tv_shift_2_from
        val shift2To = view.tv_shift_2_to

    }
}