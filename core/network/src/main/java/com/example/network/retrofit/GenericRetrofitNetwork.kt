package com.example.network.retrofit

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Inject

class GenericRetrofitNetwork <T : Any> @Inject constructor(
    okhttpCallFactory: Call.Factory,
    baseUrl: String,
    apiClass: Class<T>
) {
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    val networkApi: T = Retrofit.Builder()
        .baseUrl(baseUrl)
        .callFactory(okhttpCallFactory)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
        .create(apiClass)
}