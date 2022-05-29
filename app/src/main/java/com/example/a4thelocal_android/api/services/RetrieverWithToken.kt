/*
 * Copyright (c) 2018 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 * distribute, sublicense, create a derivative work, and/or sell copies of the
 * Software in any work that is designed, intended, or marketed for pedagogical or
 * instructional purposes related to programming, coding, application development,
 * or information technology.  Permission for such use, copying, modification,
 * merger, publication, distribution, sublicensing, creation of derivative works,
 * or sale is expressly withheld.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 */

package com.example.a4thelocal_android.api.services

import com.example.a4thelocal_android.api.data.*
import com.example.a4thelocal_android.utills.Constants
import com.example.a4thelocal_android.utills.MyPreference
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.Request


class RetrieverWithToken {
  private val service: ApiService
  init {

    val httpClient = OkHttpClient.Builder()

    httpClient.addInterceptor { chain ->
        val token = MyPreference.shared.getString(MyPreference.apiToken)
        token.let {
            val request: Request = chain.request().newBuilder().addHeader("Authorization",
                "Bearer $it").build()
            chain.proceed(request)
        }

    }

    val retrofit = Retrofit.Builder()
            //1
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
            //2
        .client(httpClient.build())
        .build()
    //3
    service = retrofit.create(ApiService::class.java)
  }

    suspend fun getProducts(): AllProductResponse {
        return service.getProducts()
    }

    suspend fun getAllStates(): StatesResponse {
        return service.getAllStates()
    }

    suspend fun getCharities(): Charity {
        return service.getCharities()
    }

    suspend fun getAllCounty(stateCode: String?): CountiesResponse {
        return service.getAllCounty(stateCode)
    }

    suspend fun getAllSchool(stateCode: String?, county: String?): SchoolsResponse {
        return service.getAllSchool(stateCode, county)
    }

    suspend fun getAllBooster(stateCode: String?, county: String?, schoolName: String?): BoostersResponse {
        return service.getAllBooster(stateCode, county, schoolName)
    }

    suspend fun getAllStudent(stateCode: String?, county: String?, schoolName: String?, booster: String?): StudentsResponse {
        return service.getAllStudent(stateCode, county, schoolName, booster)
    }

    suspend fun getAllLocations(pageNo: Int): LocationResponse {
        return service.getLocation(80, pageNo)
    }

    suspend fun getUserInfo(): UserInfoResponse {
        val userId = MyPreference.shared.getInt(MyPreference.userId)
        return service.getUserInfo(userId)
    }

    suspend fun applyCoupon(couponCode: String): CouponCodeResponse {
        return service.applyCoupon(couponCode)
    }

    suspend fun getOrderList(): OrderListResponse {
        val userId = MyPreference.shared.getInt(MyPreference.userId)
        return service.getOrderList(userId,
            Constants.customer_key,
            Constants.customer_secret)
    }

    suspend fun getUserSubscription(sId: Int): SubscriptionResponse {
        return service.getUserSubscription(sId,
            Constants.customer_key,
            Constants.customer_secret)
    }

    suspend fun changePassword(id: Int, name: String?, email: String?, password: String?): ChangePasswordResponse {
        val fName = name?.split(" ")!![0]
        val lName = name?.split(" ")!![1]

        val params = mapOf(
            "email" to email,
            "first_name" to fName,
            "last_name" to lName,
            "password" to password)

        return service.changePassword(id, params)
    }

    suspend fun forgotPassword(email: String): CommonResponse {
        val param = mapOf(
            "user_login" to email)
        return service.forgotPassword(param)
    }

}