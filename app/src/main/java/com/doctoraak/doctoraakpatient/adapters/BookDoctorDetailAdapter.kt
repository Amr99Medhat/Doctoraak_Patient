package com.doctoraak.doctoraakpatient.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.doctoraak.doctoraakpatient.R
import com.doctoraak.doctoraakpatient.model.WorkingHour

class BookDoctorDetailAdapter(val workingHours: ArrayList<WorkingHour>, val context: Context) :
    RecyclerView.Adapter<BookDoctorDetailAdapter.MyViewHolder>() {


    lateinit var mClickListener: ClickListener

    fun setOnItemClickListener(aClickListener: ClickListener) {
        mClickListener = aClickListener
    }

    interface ClickListener {
        fun onClick(model: WorkingHour, aView: View)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.clinic_item, parent, false)
        )
    }

    fun clearData(){
        if (workingHours.size > 0){
            workingHours.clear()
        }
    }

    fun setData(data : ArrayList<WorkingHour>){
        clearData()
        workingHours.addAll(data)
        this.notifyDataSetChanged()
    }


    override fun getItemCount() = workingHours.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val workingHour = workingHours[position]

    }

    inner class  MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
        , View.OnClickListener {
        override fun onClick(v: View?) {
            mClickListener.onClick(workingHours[adapterPosition] , v!!)
        }


        init {
            itemView.setOnClickListener(this)
        }

    }
}