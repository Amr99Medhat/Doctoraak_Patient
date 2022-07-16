package com.doctoraak.doctoraakpatient.ui.lap

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.doctoraak.doctoraakpatient.R
import com.doctoraak.doctoraakpatient.adapters.LabAdapter
import com.doctoraak.doctoraakpatient.databinding.ActivityLabListBinding
import com.doctoraak.doctoraakpatient.model.Lab
import com.doctoraak.doctoraakpatient.model.LabFilter
import com.doctoraak.doctoraakpatient.repository.local.SessionManager
import com.doctoraak.doctoraakpatient.ui.BaseActivity
import com.doctoraak.doctoraakpatient.ui.LapItemActivity
import com.doctoraak.doctoraakpatient.utils.Utils

class LabListActivity : BaseActivity()
{
    private val viewModel by lazy { ViewModelProvider(this).get(LapViewModel::class.java) }
    private lateinit var binding : ActivityLabListBinding
    private lateinit var adapter : LabAdapter
    private lateinit var filter: LabFilter
    companion object {
        val FILTER_LAB_KEY = "FILTER_LAB_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView( this, R.layout.activity_lab_list)
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
        filter = Utils.convertJsonToObject(intent.getStringExtra(FILTER_LAB_KEY) ?: "")
        val labs: ArrayList<Lab> = Utils.getCachedLabList()
        viewModel.pageNum.postValue(1)

        setUpRecyclerView(labs)

        val logo = findViewById<ImageView>(R.id.iv_oncare_logo)
        val user = SessionManager.returnUserInfo()
        if (user != null) {
            if (user.insurance!!.id == 1) {
                logo.visibility = View.VISIBLE
            }
        }
    }

    private fun setUpRecyclerView(labs: ArrayList<Lab>)
    {
        setRecyclerviewLinearLayout(binding.rvLaps)
        binding.rvLaps.onStartLoadingAction = {
            if (::filter.isInitialized)
            {
                viewModel.filterLabs(filter)
                true
            }

            false
        }
        adapter = LabAdapter(this)
        binding.rvLaps.adapter = adapter

        onItemClicks()

        adapter.setData(labs)
        observeData()
    }

    private fun onItemClicks()
    {
        adapter.setOnItemClickListener(object : LabAdapter.ClickListener {
            override fun onClick(model: Lab, aView: View) {
                val intent = Intent(this@LabListActivity , LapItemActivity::class.java)
                intent.putExtra(getString(R.string.lab_item) , Utils.convertObjectToJson(model))
                startActivity(intent)
            }
        })
    }

    private fun observeData()
    {
        viewModel.labsReponse.observe(this, {
            adapter.setData(it.data)
        })
    }


}
