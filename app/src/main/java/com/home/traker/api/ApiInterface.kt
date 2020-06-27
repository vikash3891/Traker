package com.home.traker.api

import com.home.traker.utils.Constants
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {

    @FormUrlEncoded
    @POST(Constants.DRIVER_LOGIN)
    fun getDriverLogin(@FieldMap fieldMap: Map<String, String>): Call<ResponseModelClasses.LoginResponseModel>

    @FormUrlEncoded
    @POST(Constants.ADMIN_LOGIN)
    fun getAdminLogin(@FieldMap fieldMap: Map<String, String>): Call<ResponseModelClasses.LoginResponseModel>

    @FormUrlEncoded
    @POST(Constants.DRIVER_LIST)
    fun registerUser(@FieldMap fieldMap: Map<String, String>): Call<ResponseModelClasses.RegistrationResponse>

    @FormUrlEncoded
    @POST(Constants.DRIVER_ATTENDANCE)
    fun setDriverAttendance(@FieldMap fieldMap: Map<String, String>): Call<ResponseModelClasses.DriverAttendanceResponseModel>

    @FormUrlEncoded
    @POST(Constants.DRIVER_ATTENDANCE_VIEW)
    fun setDriverAttendanceView(@FieldMap fieldMap: Map<String, String>): Call<ResponseModelClasses.DriverAttendanceResponseModel>

    @GET(Constants.DRIVER_LIST)
    fun getDriverList(): Call<ResponseModelClasses.DriverListResponseModel>

    @GET(Constants.DRIVER_ATTENDANCE_LIST)
    fun getDriverAttendanceList(): Call<ResponseModelClasses.DriverAttendanceResponseModel>

    //+ "/{DriverID}"
    //@Path("DriverID") id: String
    @GET(Constants.DRIVER_VENDOR_LIST)
    fun getVendorList(): Call<ResponseModelClasses.VendorListResponseModel>

    @GET(Constants.DRIVER_LIST + "/{DriverID}")
    fun getDriverProfile(@Path("DriverID") id: String): Call<ResponseModelClasses.DriverListResponseModel>

    @FormUrlEncoded
    @POST(Constants.DRIVER_VENDOR_LIST)
    fun shareLiveLocation(@FieldMap fieldMap: Map<String, String>): Call<ResponseModelClasses.RegistrationResponse>

    @FormUrlEncoded
    @POST(Constants.DRIVER_ATTENDANCE_LIST)
    fun setPunchInOut(@FieldMap fieldMap: Map<String, String>): Call<ResponseModelClasses.LoginResponseModel>

}