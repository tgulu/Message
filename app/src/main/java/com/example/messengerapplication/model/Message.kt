package com.example.messengerapplication.model

class Message {
    var message: String? = null
    var senderID: String? = null
    var time: String? = null

    constructor(){}

    constructor(message: String?, senderID: String?, messageTime: String?){
        this.message = message
        this.senderID = senderID
        this.time = messageTime
    }

}