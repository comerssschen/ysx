package com.devices.touchscreen.net

import com.devices.touchscreen.bean.*
import okhttp3.MultipartBody
import retrofit2.http.*

@JvmSuppressWildcards
interface ApiService {

    companion object {
        const val BASE_URL = "http://125.124.10.5:81/api/pc-server/"
    }

    @Multipart
    @POST("common/uploadFile")
    suspend fun uploadImage(@Part file: MultipartBody.Part): ApiResult<UploadFileBean?>

    @GET("public/service/home/dropdown")
    suspend fun getDropdown(): ApiResult<ArrayList<DropDownBean>>

    @GET("public/service/home/getRestDirectionDropDown")
    suspend fun getRestDirectionDropDown(@Query("restId") restId: String?): ApiResult<ArrayList<DropDownBean>>

    @GET("public/service/home/getPublicPagePhoto")
    suspend fun getPublicPagePhoto(@Query("restId") restId: String?, @Query("restDirection") restDirection: String?): ApiResult<BannerBean>

    @POST("public/service/home/publicFrontPage")
    suspend fun submitConfig(@Body map: Map<String, Any?>): ApiResult<String?>
}