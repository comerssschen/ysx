package com.devices.touchscreen.net

import com.devices.touchscreen.bean.*
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.http.*

@JvmSuppressWildcards
interface ApiService {

    companion object {
        const val BASE_URL = "http://125.124.10.5:81"
    }

    @Streaming
    @GET
    suspend fun downloadFile(@Url url: String): ResponseBody

    @Multipart
    @POST("api/pc-server/common/uploadFile")
    suspend fun uploadImage(@Part file: MultipartBody.Part): ApiResult<UploadFileBean?>


    @GET("api/pc-server/public/service/home/dropdown")
    suspend fun getDropdown(): ApiResult<ArrayList<DropDownBean>>
}