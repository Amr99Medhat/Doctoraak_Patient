package com.doctoraak.doctoraakpatient.ui.radiology

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.doctoraak.doctoraakpatient.R
import com.doctoraak.doctoraakpatient.adapters.RadiologyAdapter
import com.doctoraak.doctoraakpatient.databinding.ActivityRadiologyListBinding
import com.doctoraak.doctoraakpatient.model.*
import com.doctoraak.doctoraakpatient.repository.local.SessionManager
import com.doctoraak.doctoraakpatient.ui.BaseActivity
import com.doctoraak.doctoraakpatient.ui.RadiologyItemActivity
import com.doctoraak.doctoraakpatient.ui.lap.LabListActivity
import com.doctoraak.doctoraakpatient.utils.Utils

class RadiologyListActivity : BaseActivity()
{
    private val viewModel by lazy { ViewModelProvider(this).get(RadiologyViewModel::class.java) }
    private lateinit var binding : ActivityRadiologyListBinding
    private lateinit var adapter : RadiologyAdapter
    private lateinit var filter: RadiologyFilter
    companion object {
        val FILTER_RADIOLOGY_KEY = "FILTER_RADIOLOGY_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this , R.layout.activity_radiology_list)

        filter = Utils.convertJsonToObject(intent.getStringExtra(FILTER_RADIOLOGY_KEY) ?: "")
        val radios: ArrayList<Radiology> = Utils.getCachedRadiologyList()
        viewModel.pageNum.postValue(1)

        setUpRecyclerView(radios)

        val logo = findViewById<ImageView>(R.id.iv_oncare_logo)
        val user = SessionManager.returnUserInfo()
        if (user != null) {
            if (user.insurance!!.id == 1) {
                logo.visibility = View.VISIBLE
            }
        }
    }

    private fun setUpRecyclerView(radio: ArrayList<Radiology>)
    {
        setRecyclerviewLinearLayout(binding.rvRadiology)
        binding.rvRadiology.onStartLoadingAction = {
            if (::filter.isInitialized)
            {
                viewModel.filterRadiologies(filter)
                true
            }

            false
        }
        adapter = RadiologyAdapter(this)
        binding.rvRadiology.adapter = adapter

        onItemClicks()
        adapter.setData(radio)
        observeData()
    }

    private fun onItemClicks()
    {
        adapter.setOnItemClickListener(object : RadiologyAdapter.ClickListener {
            override fun onClick(model: Radiology, aView: View) {
                val intent = Intent(this@RadiologyListActivity , RadiologyItemActivity::class.java)
                intent.putExtra(getString(R.string.radiology_item_key) , Utils.convertObjectToJson(model))
                startActivity(intent)
            }
        })
    }

    private fun observeData()
    {
        viewModel.radiologyReponse.observe(this, {
            adapter.setData(it.data)
        })
    }

}
