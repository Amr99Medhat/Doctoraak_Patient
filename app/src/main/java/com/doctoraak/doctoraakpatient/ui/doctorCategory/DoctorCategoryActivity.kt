package com.doctoraak.doctoraakpatient.ui.doctorCategory

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.doctoraak.doctoraakpatient.R
import com.doctoraak.doctoraakpatient.adapters.DoctorCategoryAdapter
import com.doctoraak.doctoraakpatient.databinding.ActivityDoctorCategoryBinding
import com.doctoraak.doctoraakpatient.model.Category
import com.doctoraak.doctoraakpatient.model.DoctorCategoryResponse
import com.doctoraak.doctoraakpatient.repository.local.SessionManager
import com.doctoraak.doctoraakpatient.ui.BaseActivity
import com.doctoraak.doctoraakpatient.ui.searchDoctor.SearchDoctorActivity
import com.doctoraak.doctoraakpatient.utils.DoctorType
import com.doctoraak.doctoraakpatient.utils.Utils

// todo handle Medical centers and hospitals and Optical Centers.
class DoctorCategoryActivity : BaseActivity() {
    private lateinit var binding: ActivityDoctorCategoryBinding
    private val viewModel by lazy { ViewModelProvider(this).get(DoctorCategoryViewModel::class.java) }
    private lateinit var adapter: DoctorCategoryAdapter
    private var isMedicalCenter = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this, R.layout.activity_doctor_category)
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
        when (SessionManager.getDoctorType()) {
            DoctorType.HOSPITAL -> {
                binding.tvTitle.text = getString(R.string.hospital)
                binding.btnGeneral.visibility = View.VISIBLE
            }
            DoctorType.MEDICAL_CENTER -> {
                binding.tvTitle.text = getString(R.string.medical_center)
                binding.btnGeneral.visibility = View.VISIBLE
            }
            DoctorType.DOCTOR -> {
                binding.tvTitle.text = getString(R.string.doctor)
                binding.btnGeneral.visibility = View.GONE
            }
        }

        setRecyclerviewLayout()
        ObserveData()

        if (Utils.checkInternetConnection(this, binding.clDoctorCategory)) {
            viewModel.getCategories()
        }
        adapter = DoctorCategoryAdapter(ArrayList(), this)
        binding.rvList.adapter = adapter

        adapter.setOnItemClickListener(object : DoctorCategoryAdapter.ClickListener {
            override fun onClick(model: Category, aView: View) {
                categorySelected(model.id)
            }
        })

        binding.btnGeneral.setOnClickListener {
            categorySelected(-1)
        }

        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(txt: CharSequence?, p1: Int, p2: Int, p3: Int) {
                adapter.filter.filter(txt)
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
        val logo = findViewById<ImageView>(R.id.iv_oncare_logo)
        val user = SessionManager.returnUserInfo()
        if (user != null) {
            if (user.insurance!!.id == 1) {
                logo.visibility = View.VISIBLE
            }
        }
    }

    private fun categorySelected(id: Int) {
        val intent = Intent(this@DoctorCategoryActivity, SearchDoctorActivity::class.java)
        intent.putExtra(getString(R.string.specialization_Id_Key), id)
        startActivity(intent)
    }

    private fun setRecyclerviewLayout() {
        val layout = GridLayoutManager(this, 3)
        binding.rvList.layoutManager = layout

    }

    private fun ObserveData() {

        viewModel.cate.observe(this, object : Observer<DoctorCategoryResponse> {
            @SuppressLint("SuspiciousIndentation")
            override fun onChanged(t: DoctorCategoryResponse?) {
                binding.loading.visibility = View.GONE
                if (t!!.data.size > 0) {
                    binding.rvList.visibility = View.VISIBLE
                    adapter.setData(t.data)
                }

            }
        })
        viewModel.isLoading.observe(this, object : Observer<Boolean> {
            override fun onChanged(t: Boolean?) {
                if (t!!)
                    binding.loading.visibility = View.VISIBLE
                else
                    binding.loading.visibility = View.GONE
            }
        })

        viewModel.errorMsg.observe(this, object : Observer<String> {
            override fun onChanged(t: String?) {
                if (!t!!.equals("")) {
                    showSnackbar(binding.clDoctorCategory, t)
                    binding.loading.visibility = View.GONE
                }
            }
        })

        viewModel.errorInt.observe(this, object : Observer<Int> {
            override fun onChanged(t: Int?) {
                if (t!! != 0) {
                    showSnackbar(binding.clDoctorCategory, getString(t))
                    binding.loading.visibility = View.GONE
                }
            }
        })

    }

}
