package com.home.traker.api

import com.home.traker.model.DriverVendorListModel
import com.home.traker.model.ListAttendanceModel
import com.home.traker.model.ListItemModel
import com.home.traker.model.TrackDriverModel

object ResponseModelClasses {

    /*-------Registration-------*/
    data class RegistrationResponse(
        val status: String,
        val driver_id: String,
        val message: String
    )

    /*-------Login-------*/
    data class LoginResponseModel(
        val status: String,
        val message: String,
        val data: ArrayList<TableData1>
    ) {
        data class TableData1(
            val id: String,
            val user_id: String,
            val drive_name: String,
            val phone: String,
            val details: String,
            val pasword: String,
            val image: String
        )
    }

    /*-------Login-------*/
    data class PunchModel(
        val status: String,
        val message: String,
        val data: ArrayList<TableData1>
    ) {
        data class TableData1(
            val attendance_id: String
        )
    }

    /*-------Driver-------*/
    data class DriverAttendanceResponseModel(
        val status: String,
        val message: String,
        val data: ArrayList<ListAttendanceModel>
    )

    /*-------Vendor List-------*/
    data class VendorListResponseModel(
        val status: String,
        val message: String,
        val data: ArrayList<DriverVendorListModel>
    )

    /*-------Login-------*/
    data class DriverListResponseModel(
        val status: String,
        val message: String,
        val data: ArrayList<ListItemModel>
    )

    /*-------Login-------*/
    data class TrackDriverResponseModel(
        val status: String,
        val message: String,
        val data: ArrayList<TrackDriverModel>
    )

}



