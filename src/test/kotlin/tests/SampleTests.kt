package tests

import io.restassured.RestAssured.*
import org.hamcrest.CoreMatchers.*
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

class SampleTests {

    companion object {
        private const val GET_BOOKINGS = "https://restful-booker.herokuapp.com/booking"
    }

    private  var id:Int? = null

    @BeforeMethod
    fun setUp() {
        baseURI = GET_BOOKINGS
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
            .body("firstname", `is`("Josh"))
    }


}