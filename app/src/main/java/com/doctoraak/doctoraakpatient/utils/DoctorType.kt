package com.doctoraak.doctoraakpatient.utils

enum class DoctorType(val value: Int) {
    DOCTOR(0), HOSPITAL(1), MEDICAL_CENTER(2), OPTICAL_CENTER(3);

    companion object {
        fun getType(value: Int?) = when(value) {
            DOCTOR.value -> DOCTOR
            HOSPITAL.value -> HOSPITAL
            MEDICAL_CENTER.value -> MEDICAL_CENTER
            OPTICAL_CENTER.value -> OPTICAL_CENTER
            else -> null
        }
    }


}

