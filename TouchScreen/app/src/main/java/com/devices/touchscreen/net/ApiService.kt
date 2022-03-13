package com.devices.touchscreen.net

import com.devices.touchscreen.net.ApiResult
import com.devices.touchscreen.bean.*
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.*

@JvmSuppressWildcards
interface ApiService {

    companion object {
        const val BASE_URL = "http://47.101.165.93:8012"
    }

    @Streaming
    @GET
    suspend fun downloadFile(@Url url: String): ResponseBody

    @POST("tskWorkorderContent/imgUpload")
    suspend fun uploadImage(@Body map: RequestBody): ApiResult<String>

}