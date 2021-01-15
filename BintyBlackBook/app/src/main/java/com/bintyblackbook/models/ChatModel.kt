package com.bintyblackbook.models

class ChatModel {
    var image:Int? = null
    var message: String? = null
    var rightMessage:Boolean = false
    var profileVisible:Boolean = false

    constructor()

    constructor(image:Int,message: String,rightMessage:Boolean,profileVisible:Boolean ){
        this.image = image
        this.message = message
        this.rightMessage = rightMessage
        this.profileVisible = profileVisible
    }
}