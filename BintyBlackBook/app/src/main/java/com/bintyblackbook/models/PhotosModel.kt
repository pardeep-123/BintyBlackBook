package com.bintyblackbook.models

class PhotosModel {
    var image:Int? = null
    var name:String? = null
    var address:String? = null

    constructor()

    constructor(image:Int,name:String?,address:String?){
        this.image = image
        this.name = name
        this.address = address
    }
}