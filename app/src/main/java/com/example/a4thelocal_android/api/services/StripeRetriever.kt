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

import com.example.a4thelocal_android.api.data.StripePaymentResponse
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class StripeRetriever {
  private val service: ApiService
  init {

      val httpClient = OkHttpClient.Builder()

      httpClient.addInterceptor { chain ->
          val token = "sk_test_51GvrVzKFV5GDVcmXabLephK8SBQ1Os2fyQi6dygoOt3XeDPqxTwm8sv5jIR2gffquSEHa4oBT2kvcumuR8dKrVup00Tk3ZZ9XX"
          token.let {
              val request: Request = chain.request().newBuilder().addHeader("Authorization",
                  "Bearer $it").build()
              chain.proceed(request)
          }

      }

      val retrofit = Retrofit.Builder()
          //1
          .baseUrl("https://api.stripe.com/")
          .addConverterFactory(GsonConverterFactory.create())
          //2
          .client(httpClient.build())
          .build()
      //3
      service = retrofit.create(ApiService::class.java)
  }

    suspend fun stripePayment(params: Map<String, String>): StripePaymentResponse {
        return service.stripePayment(params)
    }

}