package com.example.chatapp.models

class User : java.io.Serializable{
    var name:String = ""
    var image:String = ""
    var email:String = ""
    var password:String = ""
    var uid:String = ""
    var online:String = ""

    constructor()

    constructor(
        name: String,
        image: String,
        email: String,
        password: String,
        uid: String,
        online: String
    ) {
        this.name = name
        this.image = image
        this.email = email
        this.password = password
        this.uid = uid
        this.online = online
    }

    constructor(name: String, image: String, email: String, password: String, uid: String) {
        this.name = name
        this.image = image
        this.email = email
        this.password = password
        this.uid = uid
    }

}