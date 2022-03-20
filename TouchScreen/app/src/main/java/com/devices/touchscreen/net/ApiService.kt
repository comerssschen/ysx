package com.devices.touchscreen.net

import com.devices.touchscreen.bean.*
import okhttp3.MultipartBody
import okhttp3.ResponseBody
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

    @GET("v2/evaluation/record/evaluator")
    suspend fun evaluator(): ApiResult<ArrayList<DropDownBean>>

    @GET("v2/evaluation/record/evaluationGrade")
    suspend fun evaluationGrade(): ApiResult<ArrayList<DropDownBean>>

    @GET("v2/evaluation/record/type")
    suspend fun evaluateType(): ApiResult<ArrayList<DropDownBean>>

    @GET("public/service/home/getRestDirectionDropDown")
    suspend fun getRestDirectionDropDown(@Query("restId") restId: String?): ApiResult<ArrayList<DropDownBean>>

    @GET("public/service/home/getPublicPagePhoto")
    suspend fun getPublicPagePhoto(@Query("restId") restId: String?, @Query("restDirection") restDirection: String?): ApiResult<BannerBean>

    @GET("public/service/evaluation/getRestInfo")
    suspend fun getRestInfo(@Query("restId") restId: String?, @Query("restDirection") restDirection: String?): ApiResult<RestInfoBean>

    @GET("restQecode/downloadCode")
    suspend fun downloadCode(@Query("restId") restId: String?, @Query("restDirection") restDirection: String?): ResponseBody

    @POST("public/service/home/publicFrontPage")
    suspend fun submitConfig(@Body map: Map<String, Any?>): ApiResult<String?>

    @POST("public/service/evaluation/addPublicComplain")
    suspend fun addPublicComplain(@Body map: AddPublicComplainBean): ApiResult<String?>

    @POST("v2/evaluation/record/save")
    suspend fun addPublicEvaluation(@Body map: EvaluateDetailBean): ApiResult<String?>
}