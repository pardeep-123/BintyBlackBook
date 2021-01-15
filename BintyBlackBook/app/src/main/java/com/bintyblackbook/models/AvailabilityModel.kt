package com.bintyblackbook.models

class AvailabilityModel {
    var time: String? = null
    var selected: Boolean = false

    constructor(time: String, selected: Boolean) {
        this.time = time
        this.selected = selected
    }

    constructor()
}