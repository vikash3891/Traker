package com.home.traker.utils

class Constants {

    companion object {
        //https://freshkart.live/limpid/index.php/api
        const val BASE_URL =
            "https://freshkart.live/limpid/index.php/api/"//"http://www.multitechnosys.com/allprojects/afa/index.php/api/v1/auth/"
        const val REQUEST_TIMEOUT_DURATION = 10
        const val DEBUG = true
        const val USER_NAME = "user_id"
        const val ADMIN_ID = "admin_id"
        const val PASSWORD = "pasword"
        const val IMAGE = "image"
        const val DRIVER_NAME = "drive_name"
        const val VENDOR_NAME = "vendor_name"
        const val PHONE = "phone"
        const val DETAILS = "details"
        const val DRIVER_DETAILS = "driverDetails"
        const val DRIVER_REG = "registerdrv"
        const val DRIVER_LOGIN = "loginuser"
        const val ADMIN_LOGIN = "loginadmin"
        const val DRIVER_ATTENDANCE = "attn"
        const val DRIVER_ATTENDANCE_VIEW = "attnvw"
        const val DRIVER_ID = "driver_id"

        const val DRIVER_BASE_URL =
            "https://freshkart.live/limpid/index.php/"
        const val DRIVER_LIST = "registration/user"
        const val DRIVER_ATTENDANCE_LIST = "attendance/user"
        const val DRIVER_VENDOR_LIST = "vendor/user"
        const val CURRENT_TIME = "current_time"
        const val CURRENT_LOCATION = "current_location"
        const val LAT = "lat"
        const val LONG = "long"
        const val IN_TIME = "in_time"
        const val CURRENT_DATE = "current_date"
        const val OUT_TIME = "out_time"
        const val IS_ROUTE_MAP_VIEW = "is_route_map_view"
        var isAdmin = true
    }
}