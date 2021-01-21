package com.bintyblackbook.models

class NotificationModel {
    var notificationMessage: String? = null
    var notificationType: String? = null

    constructor()

    constructor(notificationMessage: String, notificationType: String) {
        this.notificationMessage = notificationMessage
        this.notificationType = notificationType
    }
}