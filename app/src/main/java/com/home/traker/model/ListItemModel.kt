package com.home.traker.model

class ListItemModel {

    var id: String? = null
    var user_id: String? = null
    var drive_name: String? = null
    var phone: String? = null
    var details: String? = null
    var pasword: String? = null
    var image: String? = null
    var address: String? = null
    var lat: String? = null
    var lang: String? = null

    constructor(
        id: String,
        user_id: String,
        drive_name: String,
        phone: String,
        details: String,
        pasword: String,
        image: String,
        address: String,
        lat: String,
        lang: String

    ) {
        this.id = id
        this.user_id = user_id
        this.drive_name = drive_name
        this.phone = phone
        this.details = details
        this.pasword = pasword
        this.image = image
        this.address = address
        this.lat = lat
        this.lang = lang
    }
}