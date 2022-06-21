package com.doctoraak.doctoraakpatient.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.doctoraak.doctoraakpatient.R
import com.doctoraak.doctoraakpatient.model.Clinic
import com.doctoraak.doctoraakpatient.repository.local.SessionManager
import com.doctoraak.doctoraakpatient.utils.Constants
import com.doctoraak.doctoraakpatient.utils.Utils
import com.doctoraak.doctoraakpatient.utils.hide
import com.doctoraak.doctoraakpatient.utils.show
import kotlinx.android.synthetic.main.clinic_item.view.*


class FavouriteDoctorAdapter(val favDoctors: ArrayList<Clinic>, val context: Context) :
    RecyclerView.Adapter<FavouriteDoctorAdapter.MyViewHolder>() {

    lateinit var mClickListenerView: ClickListenerView

    fun setOnItemClickListener(aClickListener: ClickListenerView) {
        mClickListenerView = aClickListener
    }

    interface ClickListenerView {
        fun onClick(model: Clinic, aView: View)
    }

    //fav
    lateinit var mClickListener: ClickUnfavDoctor

    fun setOnItemClickUnfavListener(aClickListener: ClickUnfavDoctor) {
        mClickListener = aClickListener
    }

    interface ClickUnfavDoctor {
        fun onClick(model: Clinic, position: Int, aView: View)
    }

    fun deleteItem(pos : Int){
        favDoctors.removeAt(pos)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.clinic_item, parent, false)
        )
    }

    fun clearData(){
        if (favDoctors.size > 0){
            favDoctors.clear()
        }
    }

    fun setData(data : ArrayList<Clinic>){
        clearData()
        favDoctors.addAll(data)
        this.notifyDataSetChanged()
    }



    override fun getItemCount() = favDoctors.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val doctor = favDoctors[position]

        InializeUI(holder , doctor , position)
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

    private fun InializeUI(holder: MyViewHolder, favDoctor: Clinic,
        position: Int)
    {
       holder.name.text = Utils.getTextForAppLanguage(favDoctor.doctor.name,favDoctor.doctor.name_ar,
           favDoctor.doctor.name_fr)
        if (favDoctor.degree != null) {
            holder.degree.text = Utils.getTextForAppLanguage(favDoctor.degree.name,favDoctor.degree.name_ar,
                favDoctor.degree.name_fr)
        }

        if (favDoctor.specialization != null) {
            holder.spec.text = Utils.getTextForAppLanguage(favDoctor.specialization.name,favDoctor.specialization.name_ar,
                favDoctor.specialization.name_fr)
        }

        Glide.with(context).load(favDoctor.doctor.photo).placeholder(R.drawable.ic_doctor_placeholder)
            .error(R.drawable.ic_doctor_placeholder)
            .into(holder.image)

        try {
            holder.rate.rating = favDoctor.doctor.reservation_rate.toFloat()
        } catch (e: Exception) {
        }

        val address = "${Utils.getAreaName((favDoctor.area).toInt())}, ${Utils.getCityName((favDoctor.city).toInt())} \n ${favDoctor.address}"
        holder.address.text = address

        if (SessionManager.isLogIn())
        {
            if (Utils.getUser().insuranceCode.startsWith(Constants.INSURANCE_CODE_START_WITH_FOR_NOTES))
            {
                holder.notes.text = favDoctor.notes
                holder.notes.show()
            }
            else
                holder.notes.hide()

            val list = Utils.getFavDoctorsIds()
            if (list.contains(favDoctor.doctor_id)) {
                holder.fav.setImageResource(R.drawable.ic_favorite)
                holder.fav.tag = Constants.FAVED
            } else {
                holder.fav.setImageResource(R.drawable.ic_unfav)
                holder.fav.tag = Constants.Not_FAVED
            }
        }
        else
        {
            holder.notes.hide()
        }

        holder.fav.setOnClickListener {
            mClickListener.onClick(favDoctor  , position , it)
        }

    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view),View.OnClickListener
    {
        override fun onClick(v: View?) {
            mClickListenerView.onClick(favDoctors[adapterPosition] , v!!)
        }
        val name = view.tv_doctor_name
        val degree = view.tv_degree_name
        val spec = view.tv_specialization
        val image = view.iv_doctor_image
        val rate = view.rb_doctor
        val fav = view.iv_favorite
        val address = view.tv_address
        val notes = view.tv_notes

        init {
            itemView.setOnClickListener(this)
        }
    }
}