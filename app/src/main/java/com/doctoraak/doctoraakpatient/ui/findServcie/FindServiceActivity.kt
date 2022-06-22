package com.doctoraak.doctoraakpatient.ui.findServcie

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import com.doctoraak.doctoraakpatient.R
import com.doctoraak.doctoraakpatient.databinding.ActivityFindServiceBinding
import com.doctoraak.doctoraakpatient.repository.local.SessionManager
import com.doctoraak.doctoraakpatient.ui.BaseActivity
import com.doctoraak.doctoraakpatient.ui.doctorCategory.DoctorCategoryActivity
import com.doctoraak.doctoraakpatient.ui.icu.IcuActivity
import com.doctoraak.doctoraakpatient.ui.incubation.IncubationActivity
import com.doctoraak.doctoraakpatient.ui.lap.LapActivity
import com.doctoraak.doctoraakpatient.ui.pharmacy.PharmacyActivity
import com.doctoraak.doctoraakpatient.ui.radiology.RadiologyActivity
import com.doctoraak.doctoraakpatient.ui.searchDoctor.SearchDoctorActivity
import com.doctoraak.doctoraakpatient.utils.DoctorType

class FindServiceActivity : BaseActivity() {

    private lateinit var binding: ActivityFindServiceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_find_service)

        applyAnimation(R.anim.slide_in_end, binding.itCvPharmacy)
        applyAnimation(R.anim.slide_in_end, binding.itCvRadiology)
        applyAnimation(R.anim.slide_in_end, binding.itCvICU)
        applyAnimation(R.anim.slide_in_start, binding.itCvDoctor)
        applyAnimation(R.anim.slide_in_end, binding.itCvHospital)
        applyAnimation(R.anim.slide_in_start, binding.itCvMedicalCenter)
        applyAnimation(R.anim.slide_in_end, binding.itCvOpticalCenter)
        applyAnimation(R.anim.slide_in_start, binding.itCvLab)
        applyAnimation(R.anim.slide_in_start, binding.itCvIncubation)
        applyAnimation(R.anim.slide_in_start, binding.itCvIsolationCenter)

        binding.itCvDoctor.setOnClickListener {
            SessionManager.setDoctorType(DoctorType.DOCTOR)
            val intent = Intent(this , DoctorCategoryActivity::class.java)
            startActivity(intent)
        }

        binding.itCvHospital.setOnClickListener {
            SessionManager.setDoctorType(DoctorType.HOSPITAL)

            val intent = Intent(this , DoctorCategoryActivity::class.java)
            startActivity(intent)
        }

        binding.itCvMedicalCenter.setOnClickListener {
            SessionManager.setDoctorType(DoctorType.MEDICAL_CENTER)

            val intent = Intent(this , DoctorCategoryActivity::class.java)
            startActivity(intent)
        }

        binding.itCvOpticalCenter.setOnClickListener {
            SessionManager.setDoctorType(DoctorType.OPTICAL_CENTER)
            val intent = Intent(this , SearchDoctorActivity::class.java)
            startActivity(intent)
        }

        binding.itCvPharmacy.setOnClickListener {
            launchIntent(this, PharmacyActivity::class.java)
        }

        binding.itCvLab.setOnClickListener {
            launchIntent(this, LapActivity::class.java)
        }

        binding.itCvRadiology.setOnClickListener {
            launchIntent(this, RadiologyActivity::class.java)
        }

        binding.itCvIncubation.setOnClickListener {
            launchIntent(this, IncubationActivity::class.java)
        }

        binding.itCvICU.setOnClickListener {
            IcuActivity.launch(this, false)
        }

        binding.itCvIsolationCenter.setOnClickListener {
            IcuActivity.launch(this, true)
        }

        val logo = findViewById<ImageView>(R.id.iv_oncare_logo)
        val user = SessionManager.returnUserInfo()
        if (user != null) {
            if (user.insurance!!.id == 1) {
                logo.visibility = View.VISIBLE
            }
        }

    }
}
