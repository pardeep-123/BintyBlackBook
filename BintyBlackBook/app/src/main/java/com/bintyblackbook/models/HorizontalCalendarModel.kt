package com.bintyblackbook.models

class HorizontalCalendarModel {
    var weekName: String? = null
    var monthAndDate: String? = null
    var isSelected: Boolean = false


    constructor()
    constructor(weekName: String, monthAndDate: String, isSelected: Boolean) {
        this.weekName = weekName
        this.monthAndDate = monthAndDate
        this.isSelected = isSelected
    }
}