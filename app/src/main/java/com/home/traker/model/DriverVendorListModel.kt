package com.home.traker.model

class DriverVendorListModel {

    var id: String? = null
    var user_id: String? = null
    var vendor_name: String? = null
    var current_time: String? = null
    var phone: String? = null
    var details: String? = null
    var current_Location: String? = null
    var address: String? = null
    var lat: String? = null
    var lang: String? = null

    constructor(
        id: String,
        user_id: String,
        vendor_name: String,
        current_time: String,
        phone: String,
        details: String,
        current_Location: String,
        address: String,
        lat: String,
        lang: String

    ) {
        this.id = id
        this.user_id = user_id
        this.vendor_name = vendor_name
        this.current_time = current_time
        this.phone = phone
        this.details = details
        this.current_Location = current_Location
        this.address = address
        this.lat = lat
        this.lang = lang

    }
}