package com.bintyblackbook.models

class HomeModel {
    var title:String? = null
    var imageArrayList:ArrayList<Int>? = null

    constructor()

    constructor(title:String?,imageArrayList:ArrayList<Int>?){
        this.title = title
        this.imageArrayList = imageArrayList
    }
}