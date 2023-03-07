package com.example.messengerapplication

class User {
    var name: String? = null
    var email: String? = null
    var uniqueIdentifier: String? = null

    constructor(){}

    constructor(name:String?, email:String?, uid:String?){
        this.name = name
        this.email = email
        this.uniqueIdentifier = uid
    }
}