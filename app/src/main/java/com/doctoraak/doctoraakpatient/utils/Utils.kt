package com.doctoraak.doctoraakpatient.utils

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.location.Address
import android.location.Geocoder
import android.net.ConnectivityManager
import android.net.Uri
import android.provider.MediaStore
import android.telephony.PhoneNumberUtils
import android.util.DisplayMetrics
import android.view.ViewGroup
import android.widget.Toast
import androidx.loader.content.CursorLoader
import com.doctoraak.doctoraakpatient.R
import com.doctoraak.doctoraakpatient.model.*
import com.doctoraak.doctoraakpatient.repository.local.SessionManager
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class Utils {
    companion object {

        const val PAGING_STARTING_PAGE_INDEX = 1
        private const val MAX_IMAGE_SIZE = 3000

        //check if image size less than 3 MB
        fun isSuitableImageSize(context: Context, file: File, view: ViewGroup): Boolean =
            if ((file.length() / 1024) < MAX_IMAGE_SIZE) {
                true
            } else {
                showSnackbar(view, context.getString(R.string.imgagesize_must_lessthan_2MB))
                false
            }

        //choose the suitable name based on app langauge
        //in case of fr currently the app will be english and arabic maybe later  french will be added
        fun getTextForAppLanguage(enText: String?, arText: String?, frText: String?): String =
            when (getAppLanguage()) {
                "ar" -> arText ?: enText
                "fr" -> enText ?: ""
                else -> enText ?: ""
            }.toString()


        fun getAppLanguage() = SessionManager.getLang() ?: "ar"

        fun getUserName() = getUser().name
        fun getFirstName() = getUserName().split(" ")[0]
        fun getUser() = convertJsonToObject<User>(SessionManager.getUser() ?: "")
        fun getUserId() = convertJsonToObject<User>(SessionManager.getUser()!!).id
        fun getApiToken() = convertJsonToObject<User>(SessionManager.getUser()!!).apiToken

        fun getDayOfWeek(dayOfWeek: Int, context: Context): String {
            when (dayOfWeek) {
                1 -> return context.getString(com.doctoraak.doctoraakpatient.R.string.saturday)
                2 -> return context.getString(com.doctoraak.doctoraakpatient.R.string.sunday)
                3 -> return context.getString(com.doctoraak.doctoraakpatient.R.string.monday)
                4 -> return context.getString(com.doctoraak.doctoraakpatient.R.string.tuesday)
                5 -> return context.getString(com.doctoraak.doctoraakpatient.R.string.wednesday)
                6 -> return context.getString(R.string.thursday)
                7 -> return context.getString(com.doctoraak.doctoraakpatient.R.string.friday)
                else -> return ""
            }
        }

        fun getArea(): AreaResponse {
            val areasStr = SessionManager.getAreas()
            if (areasStr != "")
            return convertJsonToObject<AreaResponse>(areasStr!!)
            else return AreaResponse(ArrayList())
        }

        fun getAreasNames(): ArrayList<String> {
            try {
                val data = getArea().data
                val areaStr = ArrayList<String>()
                for (item in data) {
                    areaStr.add(getTextForAppLanguage(item.name, item.name_ar, item.name_fr))
                }

                return areaStr
            } catch (e: Exception) {
                return ArrayList<String>()
            }
        }

        fun getAreasNamesForCityId(cityId : Int) : ArrayList<String>{
            val list = ArrayList<String>()
            val data = getArea().data
            for (item in data){
                if (item.city_id == cityId)
                    list.add(getTextForAppLanguage(item.name,item.name_ar,item.name_fr))
            }
            return list
        }

        fun getAreaId(s: String): Int {
            val data = getArea().data
            for (item in data)
                if (getTextForAppLanguage(item.name, item.name_ar, item.name_fr) == s)
                    return item.id
            return -1
        }

        fun getCityId(s: String): Int {
            val data = getCities().data
            for (item in data)
                if (getTextForAppLanguage(item.name, item.name_ar, item.name_fr).equals(s))
                    return item.id
            return -1
        }


        fun getAreaName(id: Int): String {
            val data = getArea().data
            for (item in data)
                if (item.id == id)
                    return getTextForAppLanguage(item.name, item.name_ar, item.name_fr)
            return ""
        }

        fun getCityName(id: Int): String {
            val data = getCities().data
            for (item in data)
                if (item.id == id)
                    return getTextForAppLanguage(item.name, item.name_ar, item.name_fr)
            return ""
        }


        fun getCitiessNames(): ArrayList<String> {
            try {
                val data = getCities().data
                val citiesStr = ArrayList<String>()
                for (item in data) {
                    citiesStr.add(getTextForAppLanguage(item.name, item.name_ar, item.name_fr))
                }

                return citiesStr
            } catch (e: Exception) {
                return ArrayList<String>()
            }
        }

        //----------------------rays and analsis-----------------------//
        fun getMedicines(): MedicinesResponse {
            return convertJsonToObject<MedicinesResponse>(SessionManager.getMedicines()!!)
        }

        fun getMedicinesNames(): ArrayList<String> {
            try {
                val data = getMedicines().data
                val medicinesStr = ArrayList<String>()
                for (item in data) {
                    medicinesStr.add(getTextForAppLanguage(item.name, item.name_ar, item.name_fr))
                }

                return medicinesStr
            } catch (e: Exception) {
                return ArrayList<String>()
            }
        }

        //------------------------------------allLabs and AllRadiologies-------//
        fun getAllLabs(): AllLabsResponse {
            return convertJsonToObject<AllLabsResponse>(SessionManager.getAllLabs()!!)

        }

        fun getAllLabsNames(): ArrayList<String> {
            try {
                val data = getAllLabs().data
                val labsStr = ArrayList<String>()
                for (item in data) {
                    labsStr.add(getTextForAppLanguage(item.name, item.nameAr, item.nameFr)
                    +"\n"+"("+getAddress(item.area , item.city)+")")
                }
                return labsStr
            } catch (e: Exception) {
                return ArrayList<String>()
            }
        }

        fun getLabId(s: String): Int {
            val data = getAllLabs().data
            for (item in data)
                if ((getTextForAppLanguage(item.name, item.nameAr, item.nameFr)
                    +"\n" +"("+getAddress(item.area , item.city)+")")
                        .equals(s))
                    return item.id
            return -1
        }

        fun getAllRadiologies(): AllRadiologiesResponse {
            return convertJsonToObject<AllRadiologiesResponse>(SessionManager.getAllRadiologies()!!)
        }

        fun getAllRadiologiesNames(): ArrayList<String> {
            try {
                val data = getAllRadiologies().data
                val radiologStr = ArrayList<String>()
                for (item in data) {
                    radiologStr.add(getTextForAppLanguage(item.name, item.nameAr, item.nameFr)
                            +"\n"+"("+getAddress(item.area , item.city)+")")
                }
                return radiologStr
            } catch (e: Exception) { return ArrayList<String>() }
        }

        fun getRadiologyId(s: String): Int {
            val data = getAllRadiologies().data
            for (item in data)
                if ((getTextForAppLanguage(item.name, item.nameAr, item.nameFr)
                            +"\n" +"("+getAddress(item.area , item.city)+")")
                        .equals(s))
                    return item.id
            return -1
        }
        //---------------------------------------------------------------------//

        fun getRays(): RaysResponse {
            return convertJsonToObject<RaysResponse>(SessionManager.getRays()!!)
        }

        fun getRaysNames(): ArrayList<String> {
            try {
                val data = getRays().data
                val raysStr = ArrayList<String>()
                for (item in data) {
                    raysStr.add(getTextForAppLanguage(item.name, item.name_ar, item.name_fr))
                }

                return raysStr
            } catch (e: Exception) {
                return ArrayList<String>()
            }
        }

        fun getRayId(s: String): Int {
            val data = getRays().data
            for (item in data)
                if (getTextForAppLanguage(item.name, item.name_ar, item.name_fr).equals(s))
                    return item.id
            return -1
        }

        fun getAnaysis(): AnalysisResponse {
            return convertJsonToObject<AnalysisResponse>(SessionManager.getAnaysis()!!)

        }

        fun getAnaysisNames(): ArrayList<String> {
            try {
                val data = getAnaysis().data
                val raysStr = ArrayList<String>()
                for (item in data) {
                    raysStr.add(getTextForAppLanguage(item.name, item.name_ar, item.name_fr))
                }

                return raysStr
            } catch (e: Exception) {
                return ArrayList<String>()
            }
        }

        fun getAnaysisId(s: String): Int {
            val data = getAnaysis().data
            for (item in data)
                if (getTextForAppLanguage(item.name, item.name_ar, item.name_fr).equals(s))
                    return item.id
            return -1
        }

        //--------------------------------------------------------------------//

        fun getMedicinesType(): MedicinesTypeResponse {
            return convertJsonToObject<MedicinesTypeResponse>(SessionManager.getMedicinesType()!!)

        }

        fun getMedicinesTypeNames(): ArrayList<String> {
            try {
                val data = getMedicinesType().data
                val medicinesStr = ArrayList<String>()
                for (item in data) {
                    medicinesStr.add(getTextForAppLanguage(item.name, item.name_ar, item.name_fr))
                }

                return medicinesStr
            } catch (e: Exception) {
                return ArrayList<String>()
            }
        }

        fun getMedicineId(s: String): Int {
            val data = getMedicines().data
            for (item in data)
                if (getTextForAppLanguage(item.name, item.name_ar, item.name_fr).equals(s))
                    return item.id
            return -1
        }

        fun getMedicineTypeId(s: String): Int {
            val data = getMedicinesType().data
            for (item in data)
                if (getTextForAppLanguage(item.name, item.name_ar, item.name_fr).equals(s))
                    return item.id
            return -1
        }

        //-----------------------------------------------------------------//

        fun getCities(): CityResponse {
            val citiesStr = SessionManager.getCities()
            if (citiesStr != "")
            return convertJsonToObject<CityResponse>(citiesStr!!)
            else return  CityResponse(ArrayList())
        }

        fun getContactInfo(): ContactUsResponse {
            return convertJsonToObject<ContactUsResponse>(SessionManager.getContactInfo()!!)

        }
    //---------------------------get Insurrance----------------------------------------//
    fun getInsurrance(): InsuranceResponse {
        return convertJsonToObject<InsuranceResponse>(SessionManager.getInsurrance()!!)

    }

    fun getInsurranceNames(): ArrayList<String> {
        try {
            val data = getInsurrance().data
            val insuuranceList = ArrayList<String>()
            for (item in data!!) {
                insuuranceList.add(getTextForAppLanguage(item.name, item.name_ar, item.name_fr))
            }
            return insuuranceList
        } catch (e: Exception) {
            return ArrayList<String>()
        }
    }

    fun getInsurranceId(s: String): Int {
        val data = getInsurrance().data
        for (item in data!!)
            if (getTextForAppLanguage(item.name, item.name_ar, item.name_fr).equals(s))
                return item.id
        return -1
        }
     //------------------------------------------------------------------------------------//
        fun convertDpToPixel(context: Context, dp: Float): Float =
            dp * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)

        fun convertPixelsToDp(context: Context, px: Float): Float =
            px / (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)

        //convert object to json due to parcable problem
        fun <T> convertObjectToJson(t: T): String {
            val gson = Gson()
            val json = gson.toJson(t)
            return json
        }

        inline fun <reified T> convertJsonToObject(json: String): T {
            val gson = Gson()
            return gson.fromJson<T>(json, T::class.java)
        }

        //convert json to array of objects ex:ArrayList<Person> ,hence T = ArrayList<Person>
        inline fun <reified T> convertJsonToArrayOfObjects(json: String): T {
            val listType = object : TypeToken<T>(){}.type
            val gson = Gson()
            return gson.fromJson(json, listType)
        }

        fun checkInternetConnection(activity: Context, view: ViewGroup): Boolean {
            val connectivityManager = activity.getSystemService(Context.CONNECTIVITY_SERVICE)
                    as ConnectivityManager
            val networkInfo = connectivityManager.activeNetworkInfo
            val isConnected = (networkInfo != null && networkInfo.isConnected)
            if (isConnected) return true
            else {
                showSnackbar(view, activity.getString(com.doctoraak.doctoraakpatient.R.string.no_internet_connection))
                return false
            }
        }

        fun showSnackbar(view: ViewGroup, msg: String) {
            Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show()
        }

        fun getAddress(area: Int, city: Int): String {
            return "${getAreaName(area)} " + ", ${getCityName(city)}"
            //return "${getAreaName(area)} " attach  ", ${getCityName(city)}"
        }


        fun changeLanguage(context: Context, lang: String) {
            val language = lang
            val locale = Locale(language)
            val configEn = Configuration()
            configEn.locale = locale
            context.resources.updateConfiguration(configEn, null)
        }


        //get difference between two dates in days
        fun getDateDiff(startDate: Date, endDate: Date): Long {

            var different = endDate.time - startDate.time

            val secondsInMilli: Long = 1000
            val minutesInMilli = secondsInMilli * 60
            val hoursInMilli = minutesInMilli * 60
            val daysInMilli = hoursInMilli * 24

            val elapsedDays = different / daysInMilli
            different = different % daysInMilli

            val elapsedHours = different / hoursInMilli
            different = different % hoursInMilli

            val elapsedMinutes = different / minutesInMilli
            different = different % minutesInMilli

            val elapsedSeconds = different / secondsInMilli

            return elapsedDays
        }

        //convert string date to Date object in "yyyy-MM-dd" format
        fun getDateFormString(date: String): Date? {
            val spf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            return spf.parse(date)

        }

        fun getCurrentDate(): Date? {
            val sdfDate = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            val now = Date()
            val format = sdfDate.format(now)
            return sdfDate.parse(format)
        }

        fun getAddressFromLatLong(context: Context, latit: Double, longit: Double)
                : String {
            val geocoder: Geocoder
            var addresses: List<Address>? = null
            geocoder = Geocoder(context, Locale.getDefault())

            try {
                addresses = geocoder.getFromLocation(latit, longit, 1)
            } catch (e: IOException) {
                e.printStackTrace()
            }

            val city = addresses!![0].getLocality()
            val country = addresses[0].getCountryName()
            val locality = addresses[0].getLocality()
            val addressLine = addresses[0].getAddressLine(0) ?: ""
            val subLocality = addresses[0].getSubLocality()

            return addressLine
        }

        fun getRealPathFromURI(context: Context, contentUri: Uri): String {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            val loader = CursorLoader(
                context, contentUri, proj,
                null, null, null
            )
            val cursor = loader.loadInBackground()
            val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            val result = cursor.getString(column_index)
            cursor.close()
            return result
        }

        fun getBitmapUri(context: Context, bitmap: Bitmap): Uri {
            val bytes = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
            val path = MediaStore.Images.Media.insertImage(context.contentResolver, bitmap,
                "tempbsh", null)
            return Uri.parse(path)
        }

        fun openMessanger(context: Context, id: String) {
            val uri = Uri.parse("fb-messenger://user/$id")

            val toMessenger = Intent(Intent.ACTION_VIEW, uri)
            try {
                context.startActivity(toMessenger)
            } catch (ex: Exception) {
                Toast.makeText(
                    context,
                    context.getString(com.doctoraak.doctoraakpatient.R.string.install_messanger),
                    Toast.LENGTH_LONG
                ).show()
            }

        }

        fun openWhatsApp(context: Context, number: String) {
            var number = number
            try {
                number = number.replace(" ", "").replace("+", "")

                val sendIntent = Intent("android.intent.action.MAIN")
                sendIntent.component = ComponentName("com.whatsapp", "com.whatsapp.Conversation")
                sendIntent.putExtra(
                    "jid",
                    PhoneNumberUtils.stripSeparators(number) + "@s.whatsapp.net"
                )
                context.startActivity(sendIntent)

            } catch (e: Exception) {
                Toast.makeText(
                    context,
                    context.getString(R.string.install_whatsapp),
                    Toast.LENGTH_SHORT
                ).show()
            }

        }

        fun openDialer(context: Context, phone: String) {
            val intent = Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:$phone"));
            try {
                context.startActivity(intent);
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun openEmail(context: Context, mail: String) {

            val emailIntent = Intent(Intent.ACTION_SEND);

            emailIntent.data = Uri.parse("mailto:")
            emailIntent.type = "text/plain"
            emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(mail.trim()))
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "")

            context.startActivity(
                Intent.createChooser(
                    emailIntent,
                    context.getString(R.string.send_email)))
        }

        fun openFacebookPage(context: Context, url: String, id: String) {
            try {

                context.getPackageManager()
                    .getPackageInfo("com.facebook.katana", 0)
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("fb://page/$id")
                )
                context.startActivity(intent)
            } catch (e: Exception) {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(url)
                )
                context.startActivity(intent)
            }
        }

        fun openWebsite(context: Context,url : String) {
            var urltemp = ""
            if (url.startsWith("http://")){
                urltemp = url.replace("http://" , "")
                urltemp ="https://$urltemp"
            }
            else if (!url.startsWith("https://")){
                urltemp ="https://$urltemp"
            }
            try {
                val intent = Intent(Intent.ACTION_VIEW , Uri.parse(urltemp))
                context.startActivity(intent)
            } catch (e: Exception) {
            }
        }

        fun gotoMapDirection(context: Context , latt : Double , lang: Double){
            val strUri = "http://maps.google.com/maps?q=loc:" + latt + "," + lang + " (" + "location" + ")";
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(strUri))
            intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
            context.startActivity(intent);
        }
        //-------------------save fav doctors id -------------------//

        fun saveFavDoctorsIds(response : ClinicsResponse){
            val list = response.data
            val listOfIds = ArrayList<Int>()
            for (doc in list){
                listOfIds.add(doc.doctor_id)
            }
            SessionManager.saveFavDoctorsID(Gson().toJson(listOfIds))
        }

        fun getFavDoctorsIds() : ArrayList<Int>{
            val listType = object : TypeToken<ArrayList<Int>>() { }.type
            val gson = Gson()
            val list : ArrayList<Int> =  gson.fromJson(SessionManager.getFavDoctorsID(), listType)
            return list
        }

        fun addIdToFavList(id : Int){
            val list = getFavDoctorsIds()
            list.add(id)
            SessionManager.saveFavDoctorsID(Gson().toJson(list))
        }

        fun removeIdToFavList(id : Int){
            val list = getFavDoctorsIds()
            list.remove(id)
            SessionManager.saveFavDoctorsID(Gson().toJson(list))
        }

        fun convertTimeto12Format(s : String) : String {
            try {
                val f1 = SimpleDateFormat("HH:mm:ss")
                val d = f1.parse(s)
                val f2 = SimpleDateFormat("h:mm a")
                return f2.format(d).toLowerCase()
            } catch (e: Exception) {
                return ""
            }
        }

        fun getNiceTimeFormat(context: Context, date: String): String {
            try {
                val format = SimpleDateFormat("yyyy-M-dd HH:mm:ss")
                val past = format.parse(date)
                val now = Date()

                val seconds = TimeUnit.MILLISECONDS.toSeconds(now.time - past!!.time)
                val minutes = TimeUnit.MILLISECONDS.toMinutes(now.time - past.time)
                val hours = TimeUnit.MILLISECONDS.toHours(now.time - past.time)
                val days = TimeUnit.MILLISECONDS.toDays(now.time - past.time)

                if (seconds == 0.toLong())
                    return context.getString(R.string.now)
                else if (seconds < 60) {
                    return if (getAppLanguage() == "en")
                        "$seconds ${context.getString(R.string.seconds)} ${context.getString(R.string.ago)}"
                    else
                        "${context.getString(R.string.ago)} $seconds ${context.getString(R.string.seconds)}"
                } else if (minutes < 60) {
                    return if (getAppLanguage() == "en")
                        "$minutes ${context.getString(R.string.minutes)} ${context.getString(R.string.ago)}"
                    else
                        "${context.getString(R.string.ago)} $minutes ${context.getString(R.string.minutes)}"
                } else if (hours < 24) {
                    return if (getAppLanguage() == "en")
                        "$hours ${context.getString(R.string.hours)} ${context.getString(R.string.ago)}"
                    else
                        "${context.getString(R.string.ago)} $hours ${context.getString(R.string.hours)}"
                } else {
                    return if (getAppLanguage() == "en")
                        "$days ${context.getString(R.string.days)} ${context.getString(R.string.ago)}"
                    else
                        "${context.getString(R.string.ago)} $days ${context.getString(R.string.days)}"
                }
            } catch (e: Exception) {
                return ""
            }
        }

        fun getCachedLabList(): ArrayList<Lab>
        {
            val listType = object : TypeToken<ArrayList<Lab>>(){}.type
            return Gson().fromJson(SessionManager.getCachedList(), listType)
        }

        fun getCachedRadiologyList(): ArrayList<Radiology>
        {
            val listType = object : TypeToken<ArrayList<Radiology>>(){}.type
            return Gson().fromJson(SessionManager.getCachedList(), listType)
        }


    }

}
