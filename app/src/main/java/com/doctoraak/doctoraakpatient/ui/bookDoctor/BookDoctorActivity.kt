package com.doctoraak.doctoraakpatient.ui.bookDoctor

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.doctoraak.doctoraakpatient.R
import com.doctoraak.doctoraakpatient.adapters.DoctorFreeDaysAdapter
import com.doctoraak.doctoraakpatient.adapters.WorkingHoursAdapter
import com.doctoraak.doctoraakpatient.databinding.ActivityBookDoctorBinding
import com.doctoraak.doctoraakpatient.model.Clinic
import com.doctoraak.doctoraakpatient.model.WorkingHour
import com.doctoraak.doctoraakpatient.repository.local.SessionManager
import com.doctoraak.doctoraakpatient.ui.BaseActivity
import com.doctoraak.doctoraakpatient.ui.bookDoctorDetail.BookDoctorDetailsActivity
import com.doctoraak.doctoraakpatient.utils.*
import kotlinx.android.synthetic.main.activity_book_doctor.view.*
import java.util.*

class BookDoctorActivity : BaseActivity()
{
    private lateinit var binding: ActivityBookDoctorBinding
    lateinit var clincic: Clinic

    private lateinit var adapter : WorkingHoursAdapter
    private lateinit var doctorFreeDays : DoctorFreeDaysAdapter
    private var isButtonVisible = true

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_book_doctor)

        setRecyclerviewLayout()

        val json = intent.getStringExtra(getString(R.string.clinic_item_key))

        clincic = Utils.convertJsonToObject(json!!) as Clinic
        setAdapter()

        binding.btnBook.setOnClickListener {
            onBookClick()
        }
        InitializeUI()

        adapter.setOnItemClickListener(object : WorkingHoursAdapter.ClickListener {
            override fun onClick(model: WorkingHour, aView: View) {

            }
        })

        binding.tvPhone.setOnClickListener {
            startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:${clincic.phone}")))
        }

        binding.tvAddress.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("geo:${clincic.latt},${clincic.lang}")))
        }

        setupFreeDaysAdapter()
        binding.scrollView.post(Runnable {
            binding.scrollView.scrollTo(0,0)
        })
    }

    private fun onBookClick(date: String? = null, shift: Int? = null)
    {
        val intent = Intent(this, BookDoctorDetailsActivity::class.java)
        intent.putExtra(getString(R.string.clinic_id_key) , clincic.id)
        date?.let {
            intent.putExtra(getString(R.string.book_date_key) , date)
        }
        shift?.let {
            intent.putExtra(getString(R.string.book_shift_key) , shift)
        }
        startActivity(intent)
    }

    private fun setupFreeDaysAdapter()
    {
        val list = clincic.free_days
        val layout = LinearLayoutManager(this , LinearLayoutManager.HORIZONTAL , false)
        binding.rvFreeDays.layoutManager = layout
        doctorFreeDays = DoctorFreeDaysAdapter(list , this) {
            if (SessionManager.getDoctorType() != DoctorType.OPTICAL_CENTER)
                onBookClick(it.date, if (it.part_id == 1 || it.part_id == 2) it.part_id else null)
        }
        binding.rvFreeDays.adapter = doctorFreeDays
    }

    private fun setAdapter() {
        val list = getActiveWorkingHours(clincic.working_hours)
        adapter = WorkingHoursAdapter(list , this)
        addListDivider(binding.rvWorkingDate)
        binding.rvWorkingDate.adapter = adapter
    }

    private fun getActiveWorkingHours(workingHours: ArrayList<WorkingHour>) : ArrayList<WorkingHour> {
        val list = ArrayList<WorkingHour>()
        for (item in workingHours)
            if (item.active.toBoolean())
                list.add(item)
        return list
    }

    private fun setRecyclerviewLayout() {
        val layout = LinearLayoutManager(this  )
        binding.rvWorkingDate.rv_working_date
            .layoutManager = layout

    }

    private fun InitializeUI()
    {
        if (SessionManager.getDoctorType() == DoctorType.OPTICAL_CENTER)
            binding.btnBook.hide()

        Glide.with(this).load(clincic.doctor.photo)
            .placeholder(R.drawable.ic_doctor_placeholder)
            .error(R.drawable.ic_doctor_placeholder)
            .into(binding.ivDoctorImage)

        binding.tvDoctorName.text = Utils.getTextForAppLanguage(clincic.doctor.name,clincic.doctor.name_ar,
            clincic.doctor.name_fr)
        try {
            binding.rbDoctor.rating = (clincic.doctor.degree_rate).toFloat()
        } catch (e: Exception) {}

        binding.tvDegreeName.text = clincic.doctor.title
        binding.tvPhone.text = clincic.doctor.phone

        if (clincic.specialization != null) {
            binding.tvSpecialization.text = Utils.getTextForAppLanguage(clincic.specialization!!.name,clincic.specialization!!.name_ar,
                clincic.specialization!!.name_fr)
        }
        Utils.getUser()?.insuranceCode?.let {
            if (it.startsWith(Constants.INSURANCE_CODE_START_WITH_FOR_NOTES))
            {
                binding.tvFees.text = clincic.notes
                binding.tvFees.visibility = View.VISIBLE
            } else
                binding.tvFees.visibility = View.GONE
        }


        binding.tvAddress.text = "${Utils.getAreaName((clincic.area).toInt())}, ${Utils.getCityName((clincic.city).toInt())} \n ${clincic.address}"

    }
}
