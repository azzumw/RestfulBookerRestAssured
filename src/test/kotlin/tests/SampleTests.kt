package tests

import io.restassured.RestAssured.*
import org.hamcrest.CoreMatchers.*
import org.testng.annotations.Test

class SampleTests {

    companion object {
        private const val GET_BOOKINGS = "https://restful-booker.herokuapp.com/booking"
        private const val GET_A_BOOKING = GET_BOOKINGS
    }

    @Test
    fun get_booking_ids() {
        get(GET_BOOKINGS).then().statusCode(200).body(notNullValue())
    }



    @Test
    fun get_a_booking_with_id(){
         get("${GET_BOOKINGS}/1000").then().statusCode(200)

    }


}