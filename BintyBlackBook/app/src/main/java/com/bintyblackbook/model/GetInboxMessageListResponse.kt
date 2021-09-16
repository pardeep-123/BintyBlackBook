package com.bintyblackbook.model

class GetInboxMessageListResponse {

    var chatListing:ArrayList<MessageData> = ArrayList<MessageData>()
    var swapListing:ArrayList<MessageData> = ArrayList<MessageData>()
}

class MessageData{
    var id: Int = 0
    var senderId: Int = 0
    var receiverId: Int = 0
    var groupId:Int=0
    var type:Int=0
    var lastMessageId: Int = 0
    var deletedId: Int = 0
    var created: Int = 0
    var updated : Int = 0
    var user_id: Int = 0
    var name:String=""
    var lastMessage: String=""
    var userName: String=""
    var userImage: String=""
    var userType:String=""
    var created_at : Int=0
    var messageType: Int=0
    var isOnline : Int=0
    var unreadcount: Int=0
    var uuid:String=""
    var pushKitToken:String=""
    var isGroup:Int=0
    var image: String=""
    var totalUsers:Int=0
    var lastMessageCreated:Int=0
    var adminId:String=""
    var deleteMessagesCount:Int=0
    var groupMessagesCount:Int=0
    var userId:String=""
}

