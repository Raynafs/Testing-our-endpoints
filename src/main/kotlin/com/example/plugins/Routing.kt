package com.example.plugins

import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import kotlinx.serialization.Serializable

@Serializable
data class Item (val name : String, val quantity : Int)

fun Application.configureRouting() {
    val database = mutableListOf<Item>()
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        route("orders")
        {
            get{
                call.respond(database)
            }
            post{
                val body = call.receive<Item>()
                database.add(body)
                call.respond("Saved Successfully")
            }
        }
    }
}
