package com.example.a4thelocal_android.api.services

import com.example.a4thelocal_android.api.data.*
import com.example.a4thelocal_android.ui.data.LocalSubscriptionResponse
import retrofit2.http.*


interface ApiService {

    @FormUrlEncoded
    @POST("/wp-json/jwt-auth/v1/token")
    suspend fun loginAdmin(
        @Field("username") user: String,
        @Field("password") pass: String
    ): AdminLoginResponse

    @GET("/wp-json/wc/v3/products")
    suspend fun getProducts(): AllProductResponse

    @GET("/wp-content/api/states.php")
    suspend fun getAllStates(): StatesResponse

    //rashed rifat
    @GET("/wp-json/wp/v2/charities")
    suspend fun getCharities(): Charity

    @GET("/wp-content/api/counties.php")
    suspend fun getAllCounty(@Query("state_code") stateCode: String?): CountiesResponse

    @GET("/wp-content/api/schools.php")
    suspend fun getAllSchool(
        @Query("state_code") stateCode: String?,
        @Query("county") county: String?
    ): SchoolsResponse

    @GET("/wp-content/api/boosters.php")
    suspend fun getAllBooster(
        @Query("state_code") stateCode: String?,
        @Query("county") county: String?,
        @Query("school_name") schoolName: String?
    ): BoostersResponse

    @GET("/wp-content/api/students.php")
    suspend fun getAllStudent(
        @Query("state_code") stateCode: String?,
        @Query("county") county: String?,
        @Query("school_name") schoolName: String?,
        @Query("booster") booster: String?
    ): StudentsResponse

    @PUT("/wp-json/wc/v3/customers/{id}")
    suspend fun changePassword(
        @Path("id") id: Int?,
        @QueryMap params: Map<String, String?>
    ): ChangePasswordResponse

    @GET("/wp-json/wp/v2/businesses")
    suspend fun getLocation(
        @Query("per_page") perPage: Int,
        @Query("page") pageNo: Int): LocationResponse

    @GET("/wp-json/wc/v3/customers/{userId}")
    suspend fun getUserInfo(@Path("userId") id: Int?): UserInfoResponse

    @GET("/wp-json/wc/v3/coupons")
    suspend fun applyCoupon(@Query("code") code: String?): CouponCodeResponse

    @GET("/wc-api/v3/customers/{userId}/orders")
    suspend fun getOrderList(
        @Path("userId") id: Int?,
        @Query("consumer_key") key: String?,
        @Query("consumer_secret") secret: String?
    ): OrderListResponse

    @GET("/wc-api/v3/subscriptions/{sid}")
    suspend fun getUserSubscription(
        @Path("sid") id: Int?,
        @Query("consumer_key") key: String?,
        @Query("consumer_secret") secret: String?
    ): SubscriptionResponse

    @POST("/wp-content/api/subscription.php")
    suspend fun sclSubscription(@Body params: Map<String, String>): SchoolSubscriptionResponse

    @POST("/wp-content/api/subscription.php")
    suspend fun localSubscription(@Body params: Map<String, String>): LocalSubscriptionResponse

    @POST("/wp-json/wp/v2/users/lostpassword")
    suspend fun forgotPassword(@Body param: Map<String, String>): CommonResponse

    @FormUrlEncoded
    @POST("/v1/payment_methods")
    suspend fun stripePayment(@FieldMap param: Map<String, String>): StripePaymentResponse

}