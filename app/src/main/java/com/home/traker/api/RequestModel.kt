package com.home.traker.api

import android.util.Log
import com.google.gson.Gson
import com.home.traker.utils.Constants

object RequestModel {

    /*-------Registration-------*/
    fun getDriverRegistrationRequestModel(
        user_id: String,
        drive_name: String,
        phone: String,
        driverDetails: String,
        driverPassword: String,
        image: String
    ): Map<String, String> {
        var map = HashMap<String, String>()
        map.put(Constants.USER_NAME, user_id)
        map.put(Constants.DRIVER_NAME, drive_name)
        map.put(Constants.PHONE, phone)
        map.put(Constants.DETAILS, driverDetails)
        map.put("pasword", driverPassword)
        map.put(Constants.IMAGE, image)
        map.put("address", "")
        map.put(Constants.LAT, "0.0")
        map.put(Constants.LONG, "0.0")
        Log.d("Driver Reg Request: ", "" + Gson().toJson(map))
        return map;
    }

    /*-------Login-------*/
    fun getLoginRequestModel(
        isAdmin: Boolean,
        userid: String,
        password: String
    ): Map<String, String> {
        var map = HashMap<String, String>()
        if (isAdmin)
            map.put(Constants.ADMIN_ID, userid)
        else
            map.put(Constants.USER_NAME, userid)
        map.put(Constants.PASSWORD, password)
        Log.d("Login Request: ", "" + Gson().toJson(map))
        return map;
    }

    /*-------PUSH VENDOR-------*/
    fun setVendorDetailsRequestModel(
        user_id: String,
        vendor_name: String,
        phone: String,
        vendor_details: String,
        current_time: String,
        current_location: String,
        current_lat: String,
        current_long: String
    ): Map<String, String> {
        var map = HashMap<String, String>()
        map.put(Constants.USER_NAME, user_id)
        map.put(Constants.VENDOR_NAME, vendor_name)
        map.put(Constants.PHONE, phone)
        map.put(Constants.DETAILS, vendor_details)
        map.put(Constants.CURRENT_TIME, current_time)
        map.put(Constants.CURRENT_LOCATION, current_location)
        map.put(Constants.LAT, current_lat)
        map.put("lang", current_long)
        Log.d("Vendor Reg Request: ", "" + Gson().toJson(map))
        return map;
    }

    /*-------Registration-------*/
    fun PunchInOuRequestModel(
        user_id: String,
        current_date: String,
        in_time: String,
        out_time: String
    ): Map<String, String> {
        var map = HashMap<String, String>()
        map.put(Constants.USER_NAME, user_id)
        map.put(Constants.CURRENT_DATE, current_date)
        map.put(Constants.IN_TIME, in_time)
        map.put(Constants.OUT_TIME, out_time)

        Log.d("Vendor Reg Request: ", "" + Gson().toJson(map))
        return map;
    }

}