package com.example


import com.example.plugins.Item
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.junit.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class OrderRouteTest {

    @Test
    fun `given orders route, it returns an empty list`() = testApplication{

        client.get("orders").apply {
            assertEquals(expected = "[]", actual= bodyAsText())
        }
    }
    @Test
    fun `given orders route, when user creates an item, it saves successfully`() = testApplication {
       // add this to dependencies  testImplementation("io.ktor:ktor-client-content-negotiation:2.1.1")
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val response = client.post("orders") {
            contentType(ContentType.Application.Json) // accept json
            setBody(Item("burger", 1)) // this is the json
        }
        assertEquals(expected = HttpStatusCode.OK, response.status)
        assertEquals(expected = "Saved Successfully", response.bodyAsText())
    }

    @Test
    fun `given orders route, when user creates an item, it exists in the list`() = testApplication {
        val item = Item("burger", 1)
        val client = createClient {
            install(ContentNegotiation) { // understand json
                json() // language client understands
            }
        }
        client.post("orders") {
            contentType(ContentType.Application.Json)
            setBody(item)
        }
        client.get("orders").apply {
            val response = bodyAsText()
            val items: List<Item> = Json.decodeFromString(response)
            assertContains(items, item)
        }

    }

}



