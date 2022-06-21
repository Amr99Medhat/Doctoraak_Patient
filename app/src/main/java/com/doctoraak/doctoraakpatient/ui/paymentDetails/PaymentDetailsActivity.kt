package com.doctoraak.doctoraakpatient.ui.paymentDetails

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.doctoraak.doctoraakpatient.R
import com.doctoraak.doctoraakpatient.adapters.PaymentDetailsSliderAdapter
import com.doctoraak.doctoraakpatient.databinding.ActivityPaymentDetailsBinding
import com.doctoraak.doctoraakpatient.repository.local.SessionManager
import com.doctoraak.doctoraakpatient.ui.BaseActivity
import com.doctoraak.doctoraakpatient.ui.payment.PaymentActivity

class PaymentDetailsActivity : BaseActivity() {

    lateinit var motion : MotionLayout
    lateinit var binding : ActivityPaymentDetailsBinding

    private val viewModel: PaymentDetailsViewModel by lazy {
        ViewModelProviders.of(this).get(
            PaymentDetailsViewModel::class.java
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this , R.layout.activity_payment_details)
        motion = findViewById(R.id.motion_layout)

        observeData()
        viewModel.getPaymentDetails()

        binding.clickHandler = PaymentDetailsClickHander()
        binding.lifecycleOwner = this


    }

    private fun observeData() {
        viewModel.paymentDetailsResponse.observe(this , Observer {
            binding.viewPager.adapter  = PaymentDetailsSliderAdapter(this@PaymentDetailsActivity
            ,ArrayList<Int>().apply {
                    add(R.drawable.payment_detail_image1)
                    add(R.drawable.payment_detail_image2)
                    add(R.drawable.payment_detail_image1)
                    add(R.drawable.payment_detail_image2)
                } , it.data)
            binding.indicator.setupWithViewPager(binding.viewPager)

            disableViewPagerSwipe()
        })

        viewModel.isLoading.observe(this , Observer {
            if (it){
                binding.loading.alpha = 1f
            }else{
                binding.loading.alpha = 0f
            }
        })
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun disableViewPagerSwipe() {
        val viewgroup = binding.indicator.getChildAt(0) as LinearLayout

        for ( i in 0 until viewgroup.childCount){
            viewgroup.getChildAt(i).setOnTouchListener(View.OnTouchListener { v, event ->
                true
            })
        }

        binding.viewPager.setOnTouchListener(View.OnTouchListener { v, event ->
            true
        })
    }

    inner class PaymentDetailsClickHander{
        fun onForwardClick(){
            if (binding.viewPager.adapter != null) {
                if (binding.viewPager.currentItem == binding.viewPager.adapter!!.count - 2 ) {
                    binding.viewPager.currentItem += 1
                    motion.setTransition(R.id.start, R.id.end)
                    motion.transitionToEnd()
                    binding.btnForward.alpha = 0f
                    binding.tvSkip.alpha = 0f
                }else{
                    binding.viewPager.currentItem += 1
                }
            }
        }

        fun onBackClick(){
            if (binding.viewPager.adapter != null) {
                if (binding.viewPager.currentItem == binding.viewPager.adapter!!.count - 1) {
                    binding.viewPager.currentItem -= 1
                    motion.setTransition(R.id.end, R.id.start)
                    motion.transitionToEnd()
                    binding.btnForward.alpha = 1f
                    binding.tvSkip.alpha = 1f
                }else{
                    binding.viewPager.currentItem -= 1
                }
            }
        }

        fun onSkipClcick(){
            if (SessionManager.isLogIn()) {
                startActivity(Intent(this@PaymentDetailsActivity  , PaymentActivity::class.java))
            }else {
                showLoginFirstDialog(getString(R.string.login_first))
            }
        }

        fun onPaymentTextClick(){
            if (SessionManager.isLogIn()) {
                startActivity(Intent(this@PaymentDetailsActivity  , PaymentActivity::class.java))
            }else {
                showLoginFirstDialog(getString(R.string.login_first))
            }
        }
    }
}
