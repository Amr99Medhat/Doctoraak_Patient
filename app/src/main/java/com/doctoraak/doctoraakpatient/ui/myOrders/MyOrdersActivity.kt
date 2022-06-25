package com.doctoraak.doctoraakpatient.ui.myOrders

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.ViewPager
import com.doctoraak.doctoraakpatient.R
import com.doctoraak.doctoraakpatient.adapters.MyOrdersPagerAdapter
import com.doctoraak.doctoraakpatient.databinding.ActivityMyOrdersBinding
import com.doctoraak.doctoraakpatient.repository.local.SessionManager
import com.doctoraak.doctoraakpatient.ui.BaseActivity

class MyOrdersActivity : BaseActivity()
{
    private lateinit var binding: ActivityMyOrdersBinding

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_orders)

        binding.vpOrders.adapter = MyOrdersPagerAdapter(supportFragmentManager)

        handleBottomNavigation()
        handleViewPagerSwipe()

        val logo = findViewById<ImageView>(R.id.iv_oncare_logo)
        val user = SessionManager.returnUserInfo()
        if (user != null) {
            if (user.insurance!!.id == 1) {
                logo.visibility = View.VISIBLE
            }
        }
    }


    private fun handleViewPagerSwipe()
    {
        binding.vpOrders.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int)
            {
                binding.bottomNavigation.selectedItemId = when (position)
                {
                    0 -> R.id.nav_doctor
                    1 -> R.id.nav_pharmacy
                    2 -> R.id.nav_lab
                    3 -> R.id.nav_radiology
                    else -> 0
                }
            }
        })
    }

    private fun handleBottomNavigation()
    {
        binding.bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_doctor-> {
                    binding.vpOrders.currentItem = 0
                }
                R.id.nav_pharmacy -> {
                    binding.vpOrders.currentItem = 1
                }
                R.id.nav_lab -> {
                    binding.vpOrders.currentItem = 2
                }
                R.id.nav_radiology -> {
                    binding.vpOrders.currentItem = 3
                }
                else -> {}
            }

            true
        }
    }

}
