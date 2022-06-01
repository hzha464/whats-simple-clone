package com.example.familychat.model

class UserModel(
    var userName: String = "",
    var mail: String = "",
    val password: String = "",
    var profilepic: String ="",
    var userId: String = "",
    var lastMessage: String = "",
    var status: String = "",
    var friendListEmail: MutableList<String> = arrayListOf()
)
//){
//    lateinit var profilepic: String
//    lateinit var userId: String
//    lateinit var lastMessage: String
//    lateinit var status: String
//}
