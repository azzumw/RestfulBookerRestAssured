package tests

import com.google.gson.JsonObject
import io.restassured.RestAssured.*
import org.hamcrest.CoreMatchers.*
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test


class SampleTests {

    companion object{
        private const val AUTH_REQUEST = "https://restful-booker.herokuapp.com/auth"
    }

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

    @Test
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
            .cookie("Cookie: token=",token)
            . `when`()
            .delete("/109")
            .then().statusCode(201).log().all()
    }
}