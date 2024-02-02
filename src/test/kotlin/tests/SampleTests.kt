package tests

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import io.restassured.RestAssured.*
import org.hamcrest.CoreMatchers.*
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test
//import kotlinx.serialization.Serializable
//import kotlinx.serialization.json.Json
//import kotlinx.serialization.encodeToString

class SampleTests {

    private var id: Int? = null

    @BeforeMethod
    fun setUp() {
        baseURI = "https://restful-booker.herokuapp.com/booking"
    }

    @Test
    fun get_booking_ids() {
        get().then().statusCode(200).body(notNullValue())

    }


    @Test
    fun get_a_booking_with_id() {
        id = get().body.path("[0].bookingid")

        given()
            .get("/$id")
            .then()
            .statusCode(200)
    }

    @Test
    fun create_a_booking() {

        val bookingDates = JsonObject().apply {
            addProperty("checkin", 20200202)
            addProperty("checkout", 20200202)
        }

        val req = JsonObject().apply {
            addProperty("firstname", "Azzum")
            addProperty("lastname", "waqar")
            addProperty("totalprice", 187)
            addProperty("depositpaid", true)
            add("bookingdates", bookingDates.asJsonObject)
            addProperty("additionalneeds", "breakie")
        }


        given()
            .header("Content-Type", "application/json")
            .body(req.asJsonObject)
            .`when`()
            .post()
            .then().statusCode(200)
            .log().all()

    }

}