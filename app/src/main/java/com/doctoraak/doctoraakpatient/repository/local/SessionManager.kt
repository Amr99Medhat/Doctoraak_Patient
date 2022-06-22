package com.doctoraak.doctoraakpatient.repository.local

import android.content.Context
import android.content.SharedPreferences
import com.doctoraak.doctoraakpatient.model.Lab
import com.doctoraak.doctoraakpatient.model.User
import com.doctoraak.doctoraakpatient.utils.DoctorType
import com.doctoraak.doctoraakpatient.utils.Utils
import com.google.gson.Gson

class SessionManager(context: Context) {
    init {
        pref = context.getSharedPreferences("Doctoraak", Context.MODE_PRIVATE)
    }

    companion object {
        private const val USER_DATA_KEY: String = "USER_DATA"
        private const val USER_FORGET_PASSWORD_MODE: String = "USER_FORGET_PASSWORD_MODE"
        private lateinit var pref: SharedPreferences

        private const val CONTACTINFO_KEY = "CONTACTINFO_KEY"
        private const val CITIES_KEY = "CITIES_KEY"
        private const val AREAS_KEY = "AREAS_KEY"
        private const val MEDI_KEY = "MEDI_KEY"
        private const val MEDIType_KEY = "MEDIType_KEY"
        private const val RAYS_KEY = "RAYS_KEY"
        private const val ANAYSIS_KEY = "ANAYSIS_KEY"
        private const val ALLLabs_KEY = "ALLLabs_KEY"
        private const val ALLRADIOLOGY_KEY = "ALLRADIOLOGY_KEY"
        private const val LANG_KEY = "LANG_KEY"
        private const val INSSURACNE_KEY = "INSSURACNE_KEY"
        private const val FAVDOCTORS_IDS_KEY = "FAVDOCTORS_KEY"
        private const val CACHE_LIST_KEY = "CACHE_LIST_KEY"
        private const val DOCTOR_TYPE_KEY = "DOCTOR_TYPE_KEY"

        private const val USER_ID_MOBILE_Verfi_KEY = "USER_ID_MOBILE_Verfi_KEY"

        fun saveUserIDMobileVerfi(id: String) =
            pref.edit().putString(USER_ID_MOBILE_Verfi_KEY, id).apply()

        fun getUserIDMobileVerfi() = pref.getString(USER_ID_MOBILE_Verfi_KEY, "1")

        fun isInMobileVerfi() = pref.contains(USER_ID_MOBILE_Verfi_KEY)

        fun removeMobileVerfiMode() = pref.edit().remove(USER_ID_MOBILE_Verfi_KEY).apply()

        fun isLogIn() = pref.contains(
            USER_DATA_KEY
        )

        fun logIn(model: User) = pref.edit().putString(
            USER_DATA_KEY, Gson().toJson(model)
        ).apply()

        fun getUser() = pref.getString(USER_DATA_KEY, "")

        fun returnUserInfo(): User? {
            val user = getUser()
            val m = Gson().fromJson(user, User::class.java)
            return m;
        }

        fun signOut() = pref.edit()
            .remove(USER_DATA_KEY)
            .remove(DOCTOR_TYPE_KEY)
            .apply()


        fun setPhoneForgetPasswordMode(phone: String) = pref.edit().putString(
            USER_FORGET_PASSWORD_MODE, phone
        ).apply()

        fun getPhoneForgetPasswordMode() = pref.getString(
            USER_FORGET_PASSWORD_MODE, ""
        )

        fun clearPhoneForgetPasswordMode() = pref.edit().remove(
            USER_FORGET_PASSWORD_MODE
        ).apply()

        fun saveCities(cities: String) =
            pref.edit().putString(
                CITIES_KEY, cities
            ).apply()

        fun getCities() = pref.getString(
            CITIES_KEY, ""
        )

        fun saveAreas(cities: String) =
            pref.edit().putString(
                AREAS_KEY, cities
            ).apply()

        fun getAreas() = pref.getString(
            AREAS_KEY, ""
        )

        fun saveMedicines(medicines: String) =
            pref.edit().putString(
                MEDI_KEY, medicines
            ).apply()

        fun getMedicines() = pref.getString(
            MEDI_KEY, ""
        )

        fun saveMedicinesType(medicinesType: String) =
            pref.edit().putString(
                MEDIType_KEY, medicinesType
            ).apply()

        fun getMedicinesType() = pref.getString(
            MEDIType_KEY, ""
        )

        fun saveRays(rays: String) =
            pref.edit().putString(
                RAYS_KEY, rays
            ).apply()

        fun getRays() = pref.getString(
            RAYS_KEY, ""
        )

        fun saveAnaysis(anaylsis: String) =
            pref.edit().putString(
                ANAYSIS_KEY, anaylsis
            ).apply()

        fun getAnaysis() = pref.getString(
            ANAYSIS_KEY, ""
        )

        fun saveAllLabs(allLabs: String) =

            pref.edit().putString(
                ALLLabs_KEY, allLabs).apply()

        fun getAllLabs() = pref.getString(
            ALLLabs_KEY, ""
        )

        fun saveAllRadiologies(allRadiologies: String) =
            pref.edit().putString(
                ALLRADIOLOGY_KEY, allRadiologies
            ).apply()

        fun getAllRadiologies() = pref.getString(
            ALLRADIOLOGY_KEY, ""
        )

        fun saveLang(lang: String) =
            pref.edit().putString(
                LANG_KEY, lang
            ).apply()

        fun getLang() = pref.getString(
            LANG_KEY, null
        )


        fun saveContactInfo(info: String) =
            pref.edit().putString(
                CONTACTINFO_KEY, info
            ).apply()

        fun getContactInfo() = pref.getString(
            CONTACTINFO_KEY, null
        )

        fun saveInsurrance(insuurance: String) =
            pref.edit().putString(
                INSSURACNE_KEY, insuurance
            ).apply()

        fun getInsurrance() = pref.getString(
            INSSURACNE_KEY, ""
        )

        fun saveFavDoctorsID(favIDs: String) =
            pref.edit().putString(
                FAVDOCTORS_IDS_KEY, favIDs
            ).apply()

        fun getFavDoctorsID() = pref.getString(
            FAVDOCTORS_IDS_KEY, ""
        )

        fun <T> cacheList(obj: T) = pref.edit()
            .putString(CACHE_LIST_KEY, Gson().toJson(obj))
            .apply()

        fun getCachedList(): String {
            val list = pref.getString(CACHE_LIST_KEY, "")!!
            pref.edit()
                .remove(CACHE_LIST_KEY)
                .apply()

            return list
        }

        fun getDoctorType(): DoctorType? = DoctorType.getType(pref.getInt(DOCTOR_TYPE_KEY, -1))

        fun setDoctorType(type: DoctorType) {
            pref.edit()
                .putInt(DOCTOR_TYPE_KEY, type.value)
                .apply()
        }

    }

}