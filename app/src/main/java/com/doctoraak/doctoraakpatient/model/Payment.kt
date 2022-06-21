package com.doctoraak.doctoraakpatient.model

import com.google.gson.annotations.SerializedName

data class PaymentDetailResponse(
    @SerializedName("data")
    var `data`: ArrayList<ArrayList<String>> = ArrayList<ArrayList<String>>()
) : BaseResponse()

//payment error
data class PaymentErrorDetails( val detail : String)

//Payment auth
data class PaymentAuthRequest( val api_key : String)

data class PaymentAuthResponse(
    val profile: Profile,
    val token: String
)

data class Profile(
    val acq_partner: Any,
    val active: Boolean,
    val address: Any,
    val allow_terminal_order_id: Boolean,
    val awb_banner: Any,
    val bank_deactivation_reason: Any,
    val bank_merchant_status: Int,
    val bank_related: Any,
    val city: Any,
    val commercial_registration: Any,
    val commercial_registration_area: Any,
    val company_emails: List<String>,
    val company_name: String,
    val country: String,
    val created_at: String,
    val custom_export_columns: List<Any>,
    val deactivated_by_bank: Boolean,
    val delivery_status_callback: String,
    val delivery_update_endpoint: Any,
    val distributor_branch_code: Any,
    val distributor_code: Any,
    val email_banner: Any,
    val email_notification: Boolean,
    val failed_attempts: Int,
    val id: Int,
    val identification_number: Any,
    val logo_url: Any,
    val merchant_external_link: Any,
    val national_id: Any,
    val order_retrieval_endpoint: Any,
    val password: Any,
    val permissions: List<Any>,
    val phones: List<String>,
    val postal_code: String,
    val profile_type: String,
    val server_IP: List<Any>,
    val state: String,
    val street: String,
    val super_agent: Any,
    val user: UserPayment,
    val username: Any,
    val wallet_limit_profile: Any
)

data class UserPayment(
    val date_joined: String,
    val email: String,
    val first_name: String,
    val groups: List<Any>,
    val id: Int,
    val is_active: Boolean,
    val is_staff: Boolean,
    val is_superuser: Boolean,
    val last_login: Any,
    val last_name: String,
    val user_permissions: List<Int>,
    val username: String
)

//order registeration
data class OrderRegisterationRequest(
    @SerializedName("amount_cents")
    var amountCents: String = "",
    @SerializedName("auth_token")
    var authToken: String = "",
    @SerializedName("currency")
    var currency: String = "EGP",
    @SerializedName("delivery_needed")
    var deliveryNeeded: String = "",
    @SerializedName("items")
    var items: List<Any> = listOf(),
    @SerializedName("merchant_id")
    var merchantId: String = "",
    @SerializedName("merchant_order_id")
    var merchantOrderId : Int? = null
)

data class OrderRegisterationResponse(
    @SerializedName("amount_cents")
    var amountCents: Int = 0,
    @SerializedName("api_source")
    var apiSource: String = "",
    @SerializedName("collector")
    var collector: Any = Any(),
    @SerializedName("commission_fees")
    var commissionFees: Int = 0,
    @SerializedName("created_at")
    var createdAt: String = "",
    @SerializedName("currency")
    var currency: String = "",
    @SerializedName("delivery_fees_cents")
    var deliveryFeesCents: Int = 0,
    @SerializedName("delivery_needed")
    var deliveryNeeded: Boolean = false,
    @SerializedName("delivery_status")
    var deliveryStatus: List<Any> = listOf(),
    @SerializedName("delivery_vat_cents")
    var deliveryVatCents: Int = 0,
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("is_cancel")
    var isCancel: Boolean = false,
    @SerializedName("is_canceled")
    var isCanceled: Boolean = false,
    @SerializedName("is_payment_locked")
    var isPaymentLocked: Boolean = false,
    @SerializedName("is_return")
    var isReturn: Boolean = false,
    @SerializedName("is_returned")
    var isReturned: Boolean = false,
    @SerializedName("items")
    var items: List<Any> = listOf(),
    @SerializedName("merchant")
    var merchant: Merchant = Merchant(),
    @SerializedName("merchant_order_id")
    var merchantOrderId: Any = Any(),
    @SerializedName("merchant_staff_tag")
    var merchantStaffTag: Any = Any(),
    @SerializedName("notify_user_with_email")
    var notifyUserWithEmail: Boolean = false,
    @SerializedName("order_url")
    var orderUrl: String = "",
    @SerializedName("paid_amount_cents")
    var paidAmountCents: Int = 0,
    @SerializedName("payment_method")
    var paymentMethod: String = "",
    @SerializedName("pickup_data")
    var pickupData: Any = Any(),
    @SerializedName("shipping_data")
    var shippingData: Any = Any(),
    @SerializedName("shipping_details")
    var shippingDetails: Any = Any(),
    @SerializedName("token")
    var token: String = "",
    @SerializedName("url")
    var url: String = "",
    @SerializedName("wallet_notification")
    var walletNotification: Any = Any()
)
data class Merchant(
    @SerializedName("city")
    var city: String = "",
    @SerializedName("company_emails")
    var companyEmails: List<String> = listOf(),
    @SerializedName("company_name")
    var companyName: String = "",
    @SerializedName("country")
    var country: String = "",
    @SerializedName("created_at")
    var createdAt: String = "",
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("phones")
    var phones: List<String> = listOf(),
    @SerializedName("postal_code")
    var postalCode: String = "",
    @SerializedName("state")
    var state: String = "",
    @SerializedName("street")
    var street: String = ""
)
//card payment key
data class CardPaymentKeyRequest(
    @SerializedName("amount_cents")
    var amountCents: String = "",
    @SerializedName("auth_token")
    var authToken: String = "",
    @SerializedName("billing_data")
    var billingData: BillingData = BillingData(),
    @SerializedName("currency")
    var currency: String = "EGP",
    @SerializedName("expiration")
    var expiration: Int = 0,
    @SerializedName("integration_id")
    var integrationId: Int = 0,
    @SerializedName("lock_order_when_paid")
    var lockOrderWhenPaid: String = "",
    @SerializedName("order_id")
    var orderId: Int = 0
)

data class BillingData(
    @SerializedName("apartment")
    var apartment: String = "",
    @SerializedName("building")
    var building: String = "",
    @SerializedName("city")
    var city: String = "",
    @SerializedName("country")
    var country: String = "",
    @SerializedName("email")
    var email: String = "",
    @SerializedName("first_name")
    var firstName: String = "",
    @SerializedName("floor")
    var floor: String = "",
    @SerializedName("last_name")
    var lastName: String = "",
    @SerializedName("phone_number")
    var phoneNumber: String = "",
    @SerializedName("postal_code")
    var postalCode: String = "",
    /*@SerializedName("shipping_method")
    var shippingMethod: String = "",*/
    @SerializedName("state")
    var state: String = "",
    @SerializedName("street")
    var street: String = ""
)

data class CardPaymentKeyResposne(
    @SerializedName("token")
    var token: String = ""
)
//pay request
data class PayOrderRequest(
    var payment_token: String = "",
    var source: Source = Source()
)
data class Source(
    var identifier: String = "",
    var subtype: String = ""
)

data class PayOrderResponse(
    @SerializedName("amount_cents")
    var amountCents: Int = 0,
    @SerializedName("api_source")
    var apiSource: String = "",
    @SerializedName("captured_amount")
    var capturedAmount: Int = 0,
    @SerializedName("created_at")
    var createdAt: String = "",
    @SerializedName("currency")
    var currency: String = "",
    @SerializedName("data")
    var `data`: Data = Data(),
    @SerializedName("error_occured")
    var errorOccured: Boolean = false,
    @SerializedName("has_parent_transaction")
    var hasParentTransaction: Boolean = false,
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("integration_id")
    var integrationId: Int = 0,
    @SerializedName("is_3d_secure")
    var is3dSecure: Boolean = false,
    @SerializedName("is_auth")
    var isAuth: Boolean = false,
    @SerializedName("is_capture")
    var isCapture: Boolean = false,
    @SerializedName("is_captured")
    var isCaptured: Boolean = false,
    @SerializedName("is_hidden")
    var isHidden: Boolean = false,
    @SerializedName("is_live")
    var isLive: Boolean = false,
    @SerializedName("is_refund")
    var isRefund: Boolean = false,
    @SerializedName("is_refunded")
    var isRefunded: Boolean = false,
    @SerializedName("is_standalone_payment")
    var isStandalonePayment: Boolean = false,
    @SerializedName("is_void")
    var isVoid: Boolean = false,
    @SerializedName("is_voided")
    var isVoided: Boolean = false,
    @SerializedName("merchant_staff_tag")
    var merchantStaffTag: Any = Any(),
    @SerializedName("order")
    var order: Order = Order(),
    @SerializedName("other_endpoint_reference")
    var otherEndpointReference: Any = Any(),
    @SerializedName("owner")
    var owner: Int = 0,
    @SerializedName("parent_transaction")
    var parentTransaction: Any = Any(),
    @SerializedName("payment_key_claims")
    var paymentKeyClaims: PaymentKeyClaims = PaymentKeyClaims(),
    @SerializedName("pending")
    var pending: Boolean = false,
    @SerializedName("profile_id")
    var profileId: Int = 0,
    @SerializedName("refunded_amount_cents")
    var refundedAmountCents: Int = 0,
    @SerializedName("source_data")
    var sourceData: SourceData = SourceData(),
    @SerializedName("source_id")
    var sourceId: Int = 0,
    @SerializedName("success")
    var success: Boolean = false,
    @SerializedName("terminal_id")
    var terminalId: Any = Any(),
    @SerializedName("transaction_processed_callback_responses")
    var transactionProcessedCallbackResponses: List<Any> = listOf(),
    @SerializedName("iframe_redirection_url")
    var iframeRedirectionUrl: String = "",

    @SerializedName("redirect_url")
    var redirectUrl: String = ""
)

data class Order(
    @SerializedName("amount_cents")
    var amountCents: Int = 0,
    @SerializedName("api_source")
    var apiSource: String = "",
    @SerializedName("collector")
    var collector: Any = Any(),
    @SerializedName("commission_fees")
    var commissionFees: Int = 0,
    @SerializedName("created_at")
    var createdAt: String = "",
    @SerializedName("currency")
    var currency: String = "",
    @SerializedName("delivery_fees_cents")
    var deliveryFeesCents: Int = 0,
    @SerializedName("delivery_needed")
    var deliveryNeeded: Boolean = false,
    @SerializedName("delivery_status")
    var deliveryStatus: List<Any> = listOf(),
    @SerializedName("delivery_vat_cents")
    var deliveryVatCents: Int = 0,
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("is_cancel")
    var isCancel: Boolean = false,
    @SerializedName("is_canceled")
    var isCanceled: Boolean = false,
    @SerializedName("is_payment_locked")
    var isPaymentLocked: Boolean = false,
    @SerializedName("is_return")
    var isReturn: Boolean = false,
    @SerializedName("is_returned")
    var isReturned: Boolean = false,
    @SerializedName("items")
    var items: List<Any> = listOf(),
    @SerializedName("merchant")
    var merchant: Merchant = Merchant(),
    @SerializedName("merchant_order_id")
    var merchantOrderId: String = "",
    @SerializedName("merchant_staff_tag")
    var merchantStaffTag: Any = Any(),
    @SerializedName("notify_user_with_email")
    var notifyUserWithEmail: Boolean = false,
    @SerializedName("order_url")
    var orderUrl: String = "",
    @SerializedName("paid_amount_cents")
    var paidAmountCents: Int = 0,
    @SerializedName("payment_method")
    var paymentMethod: String = "",
    @SerializedName("pickup_data")
    var pickupData: Any = Any(),
    @SerializedName("shipping_data")
    var shippingData: ShippingData = ShippingData(),
    @SerializedName("shipping_details")
    var shippingDetails: Any = Any(),
    @SerializedName("wallet_notification")
    var walletNotification: Any = Any()
)

data class PaymentKeyClaims(
    @SerializedName("amount_cents")
    var amountCents: Int = 0,
    @SerializedName("billing_data")
    var billingData: BillingData = BillingData(),
    @SerializedName("currency")
    var currency: String = "",
    @SerializedName("exp")
    var exp: Int = 0,
    @SerializedName("integration_id")
    var integrationId: Int = 0,
    @SerializedName("lock_order_when_paid")
    var lockOrderWhenPaid: Boolean = false,
    @SerializedName("order_id")
    var orderId: Int = 0,
    @SerializedName("pmk_ip")
    var pmkIp: String = "",
    @SerializedName("user_id")
    var userId: Int = 0
)

data class ShippingData(
    @SerializedName("apartment")
    var apartment: String = "",
    @SerializedName("building")
    var building: String = "",
    @SerializedName("city")
    var city: String = "",
    @SerializedName("country")
    var country: String = "",
    @SerializedName("email")
    var email: String = "",
    @SerializedName("extra_description")
    var extraDescription: String = "",
    @SerializedName("first_name")
    var firstName: String = "",
    @SerializedName("floor")
    var floor: String = "",
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("last_name")
    var lastName: String = "",
    @SerializedName("order")
    var order: Int = 0,
    @SerializedName("order_id")
    var orderId: Int = 0,
    @SerializedName("phone_number")
    var phoneNumber: String = "",
    @SerializedName("postal_code")
    var postalCode: String = "",
    @SerializedName("shipping_method")
    var shippingMethod: String = "",
    @SerializedName("state")
    var state: String = "",
    @SerializedName("street")
    var street: String = ""
)

data class SourceData(
    @SerializedName("owner_name")
    var ownerName: Any = Any(),
    @SerializedName("pan")
    var pan: String = "",
    @SerializedName("phone_number")
    var phoneNumber: String = "",
    @SerializedName("sub_type")
    var subType: String = "",
    @SerializedName("type")
    var type: String = ""
)

data class Data(
    @SerializedName("amount")
    var amount: Int = 0,
    @SerializedName("created_at")
    var createdAt: String = "",
    @SerializedName("currency")
    var currency: String = "",
    @SerializedName("mer_txn_ref")
    var merTxnRef: String = "",
    @SerializedName("mpg_txn_id")
    var mpgTxnId: String = "",
    @SerializedName("order_info")
    var orderInfo: String = "",
    @SerializedName("redirect_url")
    var redirectUrl: String = "",
    @SerializedName("token")
    var token: String = "",
    @SerializedName("uig_txn_id")
    var uigTxnId: String = "",
    @SerializedName("wallet_issuer")
    var walletIssuer: String = "",
    @SerializedName("wallet_msisdn")
    var walletMsisdn: String = "",
    @SerializedName("agg_terminal")
    var aggTerminal: Any = Any(),
    @SerializedName("bill_reference")
    var billReference: Int = 0,
    @SerializedName("biller")
    var biller: Any = Any(),
    @SerializedName("cashout_amount")
    var cashoutAmount: Any = Any(),
    @SerializedName("due_amount")
    var dueAmount: Int = 0,
    @SerializedName("from_user")
    var fromUser: Any = Any(),
    @SerializedName("gateway_integration_pk")
    var gatewayIntegrationPk: Int = 0,
    @SerializedName("klass")
    var klass: String = "",
    @SerializedName("message")
    var message: String = "",
    @SerializedName("otp")
    var otp: String = "",
    @SerializedName("paid_through")
    var paidThrough: String = "",
    @SerializedName("ref")
    var ref: Any = Any(),
    @SerializedName("rrn")
    var rrn: Any = Any(),
    @SerializedName("txn_response_code")
    var txnResponseCode: String = ""
)
