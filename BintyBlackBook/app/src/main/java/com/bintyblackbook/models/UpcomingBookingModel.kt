package com.bintyblackbook.models

class UpcomingBookingModel {
    var image: Int? = null
    var name: String? = null
    var date: String? = null
    var time: String? = null
    var status: String? = null

    constructor()

    constructor(image: Int, name: String, date: String, time: String, status: String) {
        this.image = image
        this.name = name
        this.date = date
        this.time = time
        this.status = status
    }
}