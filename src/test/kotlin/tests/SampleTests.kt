package tests

import com.google.gson.JsonObject
import io.restassured.RestAssured.*
import io.restassured.http.ContentType
import org.hamcrest.CoreMatchers.*
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test


class SampleTests {

    companion object{
        private const val BASE_URL = "https://restful-booker.herokuapp.com"
        private const val AUTH_REQUEST = "https://restful-booker.herokuapp.com/auth"
    }

    private var id: Int? = null
    private var bookingId:Int? = null

    @BeforeMethod
    fun setUp() {
        baseURI = "$BASE_URL/booking"
    }

    @Test
    fun get_booking_ids() {
        get().then().statusCode(200).body(notNullValue())

    }


    @Test(priority = 2)
    fun get_a_booking_with_id() {

        bookingId = bookingId ?: get().body.path("[0].bookingid")

        given()
            .get("/$bookingId")
            .then()
            .statusCode(200)
    }

    @Test(priority = 1)
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


        val result = given()
            .header("Content-Type", "application/json")
            .body(req.asJsonObject)
            .`when`()
            .post()

        result.then().statusCode(200).log().all()

        bookingId = result.body.jsonPath().getString("bookingid").toInt()

    }

    @Test(priority = 4)
    fun delete_a_booking_with_id() {

        val req = JsonObject().apply {
            addProperty("username", "admin")
            addProperty("password", "password123")
        }

        val token = given()
            .header("Content-Type", "application/json")
            .body(req.asJsonObject)
            .`when`()
            .post(AUTH_REQUEST).body().`as`(JsonObject::class.java).get("token")


        given()
            .header("Content-Type","application/json")
            .header("Authorization","Basic YWRtaW46cGFzc3dvcmQxMjM=")
//            .cookie("Cookie: token=",token)
            . `when`()
            .delete("/110")
            .then().statusCode(201).log().all()
    }

    @Test(priority = 3)
    fun update_a_booking_with_id() {

        val req = JsonObject().apply {
            addProperty("username", "admin")
            addProperty("password", "password123")
        }

        val token = given()
            .header("Content-Type", "application/json")
            .body(req.asJsonObject)
            .`when`()
            .post(AUTH_REQUEST).body().`as`(JsonObject::class.java).get("token").asString

        println("Cookie: token=${token}")

        val bookingDates = JsonObject().apply {
            addProperty("checkin", 20200202)
            addProperty("checkout", 20200202)
        }

        val updateRequest = JsonObject().apply {
            addProperty("firstname", "Thomas")
            addProperty("lastname", "Paine")
            addProperty("totalprice", 176)
            addProperty("depositpaid", false)
            add("bookingdates", bookingDates.asJsonObject)
            addProperty("additionalneeds", "dinner")
        }

        println("$AUTH_REQUEST/booking/444")

        given()
            .auth().preemptive().basic("admin","password123")
            .header("Content-Type","application/json")
            .header("Accept","application/json")
            .header("Authorization","Basic YWRtaW46cGFzc3dvcmQxMjM=")
            .cookie(token)
            .body(updateRequest.asJsonObject)
        `when`().put("/444")
            .then().statusCode(200)
    }
}