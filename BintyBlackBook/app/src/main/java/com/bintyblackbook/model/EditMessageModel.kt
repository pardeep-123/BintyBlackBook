package com.bintyblackbook.model

class EditMessageModel {

    var image: Int? = null
    var name: String? = null
    var time: String? = null
    var selected: Boolean = false

    constructor()

    constructor(image: Int, name: String, time: String, selected: Boolean) {
        this.image = image
        this.name = name
        this.time = time
        this.selected = selected
    }
}