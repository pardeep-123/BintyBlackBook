package com.bintyblackbook.models

class DateModel {
    var weekName: String? = null
    var date: String? = null
    var selected: Boolean = false

    constructor(weekName: String, date: String, selected: Boolean) {
        this.weekName = weekName
        this.date = date
        this.selected = selected
    }

    constructor()
}