package com.example.chatapp.models

class MyMessage {
    var text:String = ""
    var image:String = ""
    var fromUid:String = ""
    var toUid:String = ""
    var type:String = ""

    constructor(text: String, fromUid: String, toUid: String,type:String) {
        this.text = text
        this.fromUid = fromUid
        this.toUid = toUid
        this.type = type
    }




    constructor()
    constructor(text: String, image: String, fromUid: String, toUid: String,type:String) {
        this.text = text
        this.image = image
        this.fromUid = fromUid
        this.toUid = toUid
        this.type = type
    }
}