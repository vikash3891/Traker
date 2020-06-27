package com.home.traker.model

class ListAttendanceModel{

    var id: String? = null
    var user_id: String? = null
    var current_date: String? = null
    var in_time: String? = null
    var out_time: String? = null

    var currentdate: String? = null
    var INTIME: String? = null
    var OUTTIME: String? = null

    constructor(
        id: String,
        user_id: String,
        current_date: String,
        in_time: String,
        out_time: String,
        currentdate: String,
        INTIME: String,
        OUTTIME: String

    ) {
        this.id = id
        this.user_id = user_id
        this.current_date = current_date
        this.in_time = in_time
        this.out_time = out_time

        this.currentdate = currentdate
        this.INTIME = INTIME
        this.OUTTIME = OUTTIME
    }
}