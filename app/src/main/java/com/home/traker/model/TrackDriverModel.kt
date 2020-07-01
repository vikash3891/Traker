package com.home.traker.model

class TrackDriverModel {

    var id: String? = null
    var attendance_id: String? = null
    var user_id: String? = null
    var lat: String? = null
    var lang: String? = null
    var track_date: String? = null

    constructor(
        id: String,
        user_id: String,
        attendance_id: String,
        lat: String,
        lang: String,
        track_date: String
    ) {
        this.id = id
        this.attendance_id = attendance_id
        this.user_id = user_id
        this.lat = lat
        this.lang = lang
        this.track_date = track_date
    }
}