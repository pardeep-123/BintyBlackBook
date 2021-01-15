package com.bintyblackbook.models

class PaymentModel {

    var image: Int? = null
    var name: String? = null
    var cardNo: String? = null
    var selected: Boolean = false

    constructor()

    constructor(image: Int, name: String, cardNo: String, selected: Boolean) {
        this.image = image
        this.name = name
        this.cardNo = cardNo
        this.selected = selected
    }
}