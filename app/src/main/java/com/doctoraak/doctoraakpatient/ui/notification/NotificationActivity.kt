package com.doctoraak.doctoraakpatient.ui.notification

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.doctoraak.doctoraakpatient.R
import com.doctoraak.doctoraakpatient.adapters.NotificationsAdapter
import com.doctoraak.doctoraakpatient.databinding.ActivityNotificationBinding
import com.doctoraak.doctoraakpatient.model.BaseResponse
import com.doctoraak.doctoraakpatient.model.NotificationInfo
import com.doctoraak.doctoraakpatient.model.NotificationsResponse
import com.doctoraak.doctoraakpatient.repository.local.SessionManager
import com.doctoraak.doctoraakpatient.ui.BaseActivity
import com.doctoraak.doctoraakpatient.ui.myOrders.MyOrdersActivity
import com.doctoraak.doctoraakpatient.utils.Utils
import com.doctoraak.doctoraakpatient.utils.getMsg


class NotificationActivity : BaseActivity() {

    private lateinit var binding : ActivityNotificationBinding
    private lateinit var adapter: NotificationsAdapter
    private var notificationPosition = -1

    private val viewModel: NotificationViewModel by lazy {
        ViewModelProviders.of(this).get(
            NotificationViewModel::class.java
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this , R.layout.activity_notification)

        setRecyclerviewLinearLayout(binding.rvNotifications)
        adapter = NotificationsAdapter(ArrayList<NotificationInfo>() ,this)
        binding.rvNotifications.adapter = adapter
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
        observeData()
        getUserNotifications()

        binding.srlRefresh.setOnRefreshListener {
            getUserNotifications()
            binding.srlRefresh.isRefreshing = false
        }

        adapter.setOnItemClickListener(object : NotificationsAdapter.ClickListener {
            override fun onClick(model: NotificationInfo, aView: View) {
                model.order?.let {
                    launchIntent(this@NotificationActivity, MyOrdersActivity::class.java)
                }
            }
        })

        enableSwipeItem()

        val logo = findViewById<ImageView>(R.id.iv_oncare_logo)
        val user = SessionManager.returnUserInfo()
        if (user != null) {
            if (user.insurance!!.id == 1) {
                logo.visibility = View.VISIBLE
            }
        }
    }

    private fun enableSwipeItem() {
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                var tag = viewHolder.itemView.tag as String
                val split = tag.split("&")
                val id = split[0]
                val position = split[1]
                notificationPosition = position.toInt()
                if (Utils.checkInternetConnection(this@NotificationActivity ,binding.clNotifications)) {
                    viewModel.removeNotifications(id.toInt())
                }
            }
        }).attachToRecyclerView(binding.rvNotifications)
    }

    private fun getUserNotifications() {
        if (Utils.checkInternetConnection(this , binding.clNotifications)) {
            viewModel.getNotifications(Utils.getUserId() , "PATIENT" , Utils.getApiToken())
        }else{
            binding.emptyList.visibility = View.VISIBLE
        }
    }

    private fun observeData() {

        viewModel.notificationsResponse.observe(this, object : Observer<NotificationsResponse> {
            override fun onChanged(t: NotificationsResponse?) {

                if (t!!.data.size == 0){
                    binding.emptyList.visibility = View.VISIBLE
                }
                binding.loading!!.visibility = View.INVISIBLE
                adapter.setData(t.data)

            }
        })

        viewModel.removeNotification.observe(this, object : Observer<BaseResponse> {
            override fun onChanged(t: BaseResponse?) {
                if (notificationPosition != -1){
                    adapter.deleteItem(notificationPosition)
                    notificationPosition = -1
                }
                showSnackbar(binding.clNotifications,t!!.getMsg())
            }
        })

        viewModel.isLoading.observe(this, object : Observer<Boolean> {
            override fun onChanged(t: Boolean?) {

                if (t!!) {
                    binding.loading!!.visibility = View.VISIBLE

                } else {
                    binding.loading!!.visibility = View.INVISIBLE

                }
            }
        })

        viewModel.errorMsg.observe(this, object : Observer<String> {
            override fun onChanged(t: String?) {
                if (!t.isNullOrEmpty()) {
                    showSnackbar(binding.clNotifications , t)
                    binding.loading!!.visibility = View.INVISIBLE
                }

            }
        })

        viewModel.errorInt.observe(this, object : Observer<Int> {
            override fun onChanged(t: Int?) {

                if (t!! !=0) {
                    showSnackbar(binding.clNotifications , getString(t))
                    binding.loading!!.visibility = View.INVISIBLE
                }
            }
        })

    }
}
