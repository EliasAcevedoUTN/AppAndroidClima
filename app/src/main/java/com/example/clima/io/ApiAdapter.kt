package com.example.clima.io

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

//se encargara de instanciar un  objeto Retrofit
open class ApiAdapter {

    private lateinit var API_SERVICE: ApiService

    private var retrofit: Retrofit? = null

    //creamos el interceptor y le indicamos el log level  a usar
    private val interceptor = run {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.apply {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        }
    }
    //Asociamos el interceptor a las peticiones
    private val okHttpClient = OkHttpClient.Builder().readTimeout(30, TimeUnit.SECONDS)
        .connectTimeout(30, TimeUnit.SECONDS)
        .addInterceptor(interceptor)//usamos el log level
        .build()




    fun getApiService(): ApiService {

        if (!::API_SERVICE.isInitialized) {
            retrofit = Retrofit.Builder()
                .baseUrl("http://dataservice.accuweather.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()

            API_SERVICE = retrofit!!.create(ApiService::class.java)
        }

        return API_SERVICE
    }

}