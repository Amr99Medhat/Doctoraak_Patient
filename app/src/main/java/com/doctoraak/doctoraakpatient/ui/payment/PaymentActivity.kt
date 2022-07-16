package com.doctoraak.doctoraakpatient.ui.payment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.doctoraak.doctoraakpatient.R
import com.doctoraak.doctoraakpatient.adapters.PaymentSliderAdapter
import com.doctoraak.doctoraakpatient.databinding.ActivityPaymentBinding
import com.doctoraak.doctoraakpatient.model.*
import com.doctoraak.doctoraakpatient.repository.local.SessionManager
import com.doctoraak.doctoraakpatient.ui.BaseActivity
import com.doctoraak.doctoraakpatient.ui.WalletWebviewActivity
import com.doctoraak.doctoraakpatient.utils.Constants
import com.doctoraak.doctoraakpatient.utils.Utils
import com.doctoraak.doctoraakpatient.utils.hide
import com.doctoraak.doctoraakpatient.utils.show
import com.example.androidsdk.IntentKeys
import com.example.androidsdk.Pay
import kotlin.random.Random

class PaymentActivity : BaseActivity() {

    private val PAYMENT_REQUEST_CODE = 1
    private lateinit var binding: ActivityPaymentBinding
    lateinit var motion: MotionLayout
    var authToken = ""
    val PAYMENT_AMOUNT = 1000

    //payment variables
    var Endpoint = "https://accept.paymobsolutions.com/api/acceptance/post_pay"

    // transaction status
    var success = ""

    // transaction ID
    var Id = ""

    // transaction amount
    var amount_cents = ""

    // The Integration ID used
    var integration_id = ""
    
    var has_parent_transaction = ""
    var txn_response_code = ""
    var acq_response_code = ""
    var IframeID = 36278

    private val viewModel: PaymentViewModel by lazy {
        ViewModelProviders.of(this).get(PaymentViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_payment)
        motion = findViewById<MotionLayout>(R.id.motion_layout)
        binding.clickhander = PaymentClickHandler()
        binding.lifecycleOwner = this

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

        observeData()
        binding.viewPager.apply {
            adapter = PaymentSliderAdapter(this@PaymentActivity, ArrayList<Int>().apply {
                add(R.drawable.im_vodaphone)
                add(R.drawable.im_fawry)
                add(R.drawable.im_visa)
            })
            clipToPadding = false
            setPadding(60, 0, 60, 0)
            pageMargin = 25
        }
        disableViewPagerSwipe()

        val logo = findViewById<ImageView>(R.id.iv_oncare_logo)
        val user = SessionManager.returnUserInfo()
        if (user != null) {
            if (user.insurance!!.id == 1) {
                logo.visibility = View.VISIBLE
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun disableViewPagerSwipe() {

        binding.viewPager.setOnTouchListener(View.OnTouchListener { v, event ->
            true
        })
    }

    private fun observeData() {
        viewModel.paymentResponse.observe(this, Observer {
            SessionManager.logIn(it.user!!)
            Toast.makeText(this, getString(R.string.process_completed), Toast.LENGTH_LONG).show()
            finish()
        })

        viewModel.paymentAuthResponse.observe(this, Observer {
            authToken = it.token
            val request =
                OrderRegisterationRequest(authToken = it.token, merchantId = "${it.profile.id}"
                    , amountCents = PAYMENT_AMOUNT.toString(), deliveryNeeded = "false")
            val currentItem = binding.viewPager.currentItem
            val random = Random.nextInt(215, 51615183)
            if (currentItem == 0 || currentItem == 1) {
                request.merchantOrderId = random
            }
            viewModel.orderRegisteration(request)
        })

        viewModel.orderRegisterationResponse.observe(this, Observer {
            val user = Utils.getUser()

            val billingData = BillingData(apartment = "NA",
                email = user.email,
                floor = "NA",
                firstName = user.name.split(" ")[0],
                street = "NA",
                building = "NA",
                phoneNumber = user.phone /*, shippingMethod = "NA"*/,
                postalCode = "NA",
                city = "NA",
                country = "NA",
                lastName = user.name.split(" ")[1],
                state = "NA")
            val request = CardPaymentKeyRequest(authToken = authToken,
                integrationId = 27701,
                billingData = billingData,
                amountCents = PAYMENT_AMOUNT.toString(),
                expiration = 3600,
                orderId = it.id,
                lockOrderWhenPaid = "false")
            val currentItem = binding.viewPager.currentItem
            if (currentItem == 0) {
                request.integrationId = 30538
            } else if (currentItem == 1) {
                request.integrationId = 26508
            } else {
                request.integrationId = 27701
            }
            viewModel.getCardPaymentToken(request)
        })

        viewModel.paymentTokenResponse.observe(this, Observer {
            val currentItem = binding.viewPager.currentItem
                if (currentItem == 0) {
                    val request = PayOrderRequest()
                    val source =
                        Source(identifier = "${binding.etPhoneNum.text.toString()}", subtype = "WALLET")
                    request.payment_token = it.token
                    request.source = source

                    viewModel.makePayOrder(request)
                } else if (currentItem == 1) {
                    val request = PayOrderRequest()
                    val source = Source(identifier = "AGGREGATOR", subtype = "AGGREGATOR")
                request.payment_token = it.token
                request.source = source

                viewModel.makePayOrder(request)
            } else {
                openPayment(it.token)
            }

        })

        viewModel.payOrderResponse.observe(this, Observer {
            val currentItem = binding.viewPager.currentItem
            if (currentItem == 0) {

                val intent = Intent(this@PaymentActivity, WalletWebviewActivity::class.java)
                intent.putExtra("url", it.redirectUrl)
                startActivity(intent)
            } else {
                showSuscessDialog("Take this code to complete payment process ${it.data.billReference}")
            }

        })

        viewModel.isLoading.observe(this, object : Observer<Boolean> {
            override fun onChanged(t: Boolean?) {
                if (t!!) {
                    binding.loading!!.visibility = View.VISIBLE
                    //binding.icPharmOrderMedicine.btn_send_order.disable()
                } else {
                    binding.loading!!.visibility = View.GONE
                    //binding.icPharmOrderMedicine.btn_send_order.enable()
                }

            }
        })

        viewModel.errorMsg.observe(this, object : Observer<String> {
            override fun onChanged(t: String?) {
                if (!t.isNullOrEmpty()) {
                    showSnackbar(binding.clPayment, t)
                    binding.loading!!.visibility = View.GONE
                    //binding.icPharmOrderMedicine.btn_send_order.enable()
                }
            }
        })

        viewModel.errorInt.observe(this, object : Observer<Int> {
            override fun onChanged(t: Int?) {
                if (t!! != 0) {
                    showSnackbar(binding.clPayment, getString(t))
                    binding.loading!!.visibility = View.INVISIBLE
                    //binding.icPharmOrderMedicine.btn_send_order.enable()
                }
            }
        })
    }


    private fun openPayment(token: String) {
        val payIntent = Intent(this, Pay::class.java)
        payIntent.putExtra(IntentKeys.PAYMENT_KEY, token)
        payIntent.putExtra(IntentKeys.ENDPOINT_URL, Endpoint)
        payIntent.putExtra(IntentKeys.IFRAMEID.toString(), IframeID)
        startActivityForResult(payIntent, PAYMENT_REQUEST_CODE)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PAYMENT_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    success = data.getStringExtra("success") ?:""
                    Id = data.getStringExtra("ID")?:""
                    amount_cents = data.getStringExtra("amount_cents")?:""
                    integration_id = data.getStringExtra("integration_id")?:""
                    has_parent_transaction = data.getStringExtra("has_parent_transaction")?:""
                    //txn_response_code = data.getStringExtra("txn_response_code")
                    //acq_response_code = data.getStringExtra("acq_response_code")
                    checkPaymentStatus()
                }
            }
        }
    }

    private fun checkPaymentStatus() {
        val Succeful = "true"
        val Declined = "false"

        if (success.equals(Succeful)) {
            viewModel.applyBuyCareience(Utils.getUserId())
        } else {
            showSnackbar(binding.clPayment, getString(R.string.some_went_wrong))
        }
    }

    private fun showSuscessDialog(msg: String) {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle(getString(R.string.done))
        dialog.setMessage(msg)
        dialog.setCancelable(false)
        dialog.setNegativeButton(getString(R.string.ok)) { d, which ->
            d.dismiss()
        }
        dialog.show()
    }

    inner class PaymentClickHandler {
        fun onVodaphonecashClick() {
            binding.etlPhoneNum.hint =
                getString(com.doctoraak.doctoraakpatient.R.string.phone_number)
            binding.tvEnterPhone.text = getString(R.string.enter_your_phone_number)
            binding.tvCountryCode.show()
            binding.etlPhoneNum.show()
            val index = binding.viewPager.currentItem
            if (index != 0) {
                if (index == 2) {
                    motion.setTransition(R.id.start3, R.id.end3)
                    motion.transitionToEnd()
                } else {
                    motion.setTransition(R.id.end1, R.id.start1)
                    motion.transitionToEnd()
                }
            }
            binding.viewPager.currentItem = 0

        }

        fun onFaweryClick() {
            binding.etlPhoneNum.hint = getString(com.doctoraak.doctoraakpatient.R.string.code)
            binding.tvEnterPhone.text =
                getString(com.doctoraak.doctoraakpatient.R.string.enter_the_code)
            binding.tvCountryCode.hide()
            binding.etlPhoneNum.hide()
            val index = binding.viewPager.currentItem

            if (index != 1) {
                if (index == 0) {
                    motion.setTransition(com.doctoraak.doctoraakpatient.R.id.start1,
                        com.doctoraak.doctoraakpatient.R.id.end1)
                    motion.transitionToEnd()
                } else {
                    motion.setTransition(R.id.end2, R.id.start2)
                    motion.transitionToEnd()
                }
            }

            binding.viewPager.currentItem = 1
        }

        fun onVisaClick() {
            binding.etlPhoneNum.hint =
                getString(com.doctoraak.doctoraakpatient.R.string.visa_number_)
            binding.tvEnterPhone.text =
                getString(com.doctoraak.doctoraakpatient.R.string.visa_number)
            binding.tvCountryCode.hide()
            binding.etlPhoneNum.hide()
            val index = binding.viewPager.currentItem
            if (index != 2) {
                if (index == 1) {
                    motion.setTransition(R.id.start2, com.doctoraak.doctoraakpatient.R.id.end2)
                    motion.transitionToEnd()
                } else {
                    motion.setTransition(com.doctoraak.doctoraakpatient.R.id.end3,
                        com.doctoraak.doctoraakpatient.R.id.start3)
                    motion.transitionToEnd()
                }
            }

            binding.viewPager.currentItem = 2
        }

        fun onDoneClick() {
            viewModel.paymentAuth(Constants.paymentKey)
        }

    }
}
