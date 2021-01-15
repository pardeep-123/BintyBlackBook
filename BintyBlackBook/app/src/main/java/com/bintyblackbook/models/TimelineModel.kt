package com.bintyblackbook.models

class TimelineModel {
    var profileImage:Int? = null
    var name:String? = null
    var time:String? = null
    var postText:String? = null
    var likes:String? = null
    var comments:String? = null
    var postImage:Int? = null
    var heartFilled:Boolean = false
    var myPost:Boolean = false

    constructor()

    constructor(profileImage:Int,name:String,time:String,postText:String,likes:String,comments:String?,postImage:Int?,heartFilled:Boolean,myPost:Boolean){
        this.profileImage = profileImage
        this.name = name
        this.time = time
        this.postText = postText
        this.likes = likes
        this.comments = comments
        this.postImage = postImage
        this.heartFilled = heartFilled
        this.myPost = myPost
    }
}