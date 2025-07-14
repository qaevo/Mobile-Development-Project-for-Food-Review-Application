package com.mobdeve.s11.mc04.evora.santiago

import java.util.*

data class ReviewModel(
    var id: Int = getAutoId(),
    var title: String = "",
    var body: String = ""
){
    companion object{
        fun getAutoId(): Int{
            val random = Random()
            return random.nextInt(100)
        }
    }
}
