package com.doctoraak.doctoraakpatient.adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
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
import com.doctoraak.doctoraakpatient.utils.PageStatus
import com.doctoraak.doctoraakpatient.utils.Utils
import com.doctoraak.doctoraakpatient.utils.hide
import kotlinx.android.synthetic.main.clinic_item.view.*

class AvailableClinicsAdapter(val context: Context)
    : PagingAdapter<Clinic>()
{
    lateinit var mClickListener: ClickListener

    fun setOnItemClickListener(aClickListener: ClickListener) {
        mClickListener = aClickListener
    }

    /////////fav click
    lateinit var mClickFavListener: ClickFavListener

    fun setOnItemClickFavListener(aClickListener: ClickFavListener) {
        mClickFavListener = aClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        when(viewType) {
            PageStatus.NEW_PAGE.value -> MyViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.clinic_item, parent, false))
            else -> super.onCreateViewHolder(parent, viewType)
        }

    private fun InializeUI(holder: MyViewHolder, clinic: Clinic) {

        try {
            holder.rating.rating = (clinic.doctor.rate).toFloatOrNull() ?: 1.0f
        } catch (e: Exception) {
        }

        holder.doctorName.text = Utils.getTextForAppLanguage(
            clinic.doctor.name,
            clinic.doctor.name_ar, clinic.doctor.name_fr
        )

        holder.doctorDegree.text = clinic.doctor.title
        holder.phone.text = clinic.phone

        Glide.with(context).load(clinic.doctor.photo).into(holder.doctorImage)

        holder.address.text = "${Utils.getAreaName((clinic.area).toInt())}, ${Utils.getCityName((clinic.city).toInt())} \n ${clinic.address}"

        if (clinic.specialization != null)
        {
            holder.specialization.text = Utils.getTextForAppLanguage(
                clinic.specialization.name,
                clinic.specialization.name_ar, clinic.specialization.name_fr
            )
        }

        Glide.with(context).load(clinic.doctor.photo).placeholder(R.drawable.ic_doctor_placeholder)
            .error(R.drawable.ic_doctor_placeholder)
            .into(holder.doctorImage)
    }

    fun setAnimation(position: Int, holder: MyViewHolder) {
        val slideLeft = AnimationUtils.loadAnimation(context, R.anim.slide_right)
        val slideright = AnimationUtils.loadAnimation(context, R.anim.slide_left)
        if (position % 2 == 0)
            holder.itemView.startAnimation(slideLeft)
        else
            holder.itemView.startAnimation(slideright)

    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
        , View.OnClickListener, PagingHolder<Clinic>
    {
        override fun onClick(v: View?) {
            mClickListener.onClick(list[adapterPosition], v!!)
        }

        val rating = view.rb_doctor
        val doctorName = view.tv_doctor_name
        val doctorImage = view.iv_doctor_image
        val doctorDegree = view.tv_degree_name
        val specialization = view.tv_specialization
        val address = view.tv_address
        val phone = view.tv_phone
        val notes = view.tv_notes
        val fav = view.iv_favorite
        val progressBar = view.progress_bar

        init {
            itemView.setOnClickListener(this)
        }

        override fun bind(item: Clinic)
        {
            InializeUI(this, item)
            fav.setOnClickListener {
                if (SessionManager.isLogIn()) {
                    mClickFavListener.onClick(position, item, it)
                }
            }

            phone.setOnClickListener {
                context.startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:${item.phone}")))
            }

            address.setOnClickListener {
                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("geo:${item.latt},${item.lang}")))
            }

            fav.visibility = View.VISIBLE
            progressBar.visibility = View.INVISIBLE
            if (!SessionManager.isLogIn()){
                fav.visibility = View.INVISIBLE
            }
            if (SessionManager.isLogIn())
            {
                if (Utils.getUser().insuranceCode.startsWith(Constants.INSURANCE_CODE_START_WITH_FOR_NOTES))
                {
                    notes.text = item.notes
                    notes.visibility = View.VISIBLE
                }
                else
                    notes.visibility = View.GONE

                val list = Utils.getFavDoctorsIds()
                if (list.contains(item.doctor_id)) {
                    fav.setImageResource(R.drawable.ic_favorite)
                    fav.tag = Constants.FAVED
                } else {
                    fav.setImageResource(R.drawable.ic_unfav)
                    fav.tag = Constants.Not_FAVED
                }
            }
            else
            {
                notes.hide()
            }

            setAnimation(position, this)
        }

    }

    interface ClickListener {
        fun onClick(model: Clinic, aView: View)
    }
    interface ClickFavListener {
        fun onClick(position: Int, model: Clinic, aView: View)
    }
}