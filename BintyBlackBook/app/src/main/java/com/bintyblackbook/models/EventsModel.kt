package com.bintyblackbook.models

class EventsModel {
    var image:Int? = null
    var name:String? = null
    var address:String? = null
    var ivFav:Boolean? = false

    constructor()

    constructor(image:Int,name:String?,address:String?, ivFav:Boolean?){
        this.image = image
        this.name = name
        this.address = address
        this.ivFav = ivFav
    }
}