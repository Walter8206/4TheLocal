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
import com.example.a4thelocal_android.ui.data.LocalSubscriptionResponse
import com.example.a4thelocal_android.utills.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class Retriever {
  private val service: ApiService
  init {

    val retrofit = Retrofit.Builder()
            //1
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
            //2
        .build()
    //3
    service = retrofit.create(ApiService::class.java)
  }

    suspend fun getProducts(): AllProductResponse {
        return service.getProducts()
    }

    suspend fun loginAdmin(user: String, pass: String): AdminLoginResponse {
        return service.loginAdmin(user, pass)
    }

    suspend fun sclSubscription(params: Map<String, String>): SchoolSubscriptionResponse {
        return service.sclSubscription(params)
    }

    suspend fun localSubscription(params: Map<String, String>): LocalSubscriptionResponse {
        return service.localSubscription(params)
    }
}