package com.devices.touchscreen.net

import com.devices.touchscreen.BuildConfig
import com.devices.touchscreen.common.token
import okhttp3.ConnectionPool
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private const val CONNECT_TIME_OUT = 120L * 1000L

    private const val READ_TIME_OUT = 60L * 1000L
    private const val WRITE_TIME_OUT = 60L * 1000L

    private val mTokenInterceptor = Interceptor { chain ->
        val builder = chain.request().newBuilder()
        token.let {
            if (it.isNotEmpty()) builder.addHeader("token", it)
        }
        return@Interceptor chain.proceed(builder.build())
    }
    private val mLogInterceptor: Interceptor by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        val level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
        HttpLoggingInterceptor().setLevel(level)
    }

    private val mOkHttpClient: OkHttpClient by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        OkHttpClient.Builder()
            .connectTimeout(CONNECT_TIME_OUT, TimeUnit.MILLISECONDS)    // 连接超时
            .readTimeout(READ_TIME_OUT, TimeUnit.MILLISECONDS)          // 读取超时
            .writeTimeout(WRITE_TIME_OUT, TimeUnit.MILLISECONDS)          // 读取超时
            .addInterceptor(mLogInterceptor)                            // 日志拦截器
            .addInterceptor(mTokenInterceptor)
            .connectionPool(ConnectionPool(32, 5L, TimeUnit.MINUTES))
            .retryOnConnectionFailure(true)      // 失败重连
            .build()
    }

    private val mRetrofit: Retrofit by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        Retrofit.Builder()
            .baseUrl(ApiService.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(mOkHttpClient)
            .build()
    }
    val apiService: ApiService = mRetrofit.create(ApiService::class.java)
}