package com.example.color_match3

import retrofit2.http.Body
import retrofit2.http.POST

data class ImageRequest(
    val prompt: String,
    val n: Int = 1,
    val size: String = "1024x1024",
    val model: String = "dall-e-2"
)

data class ImageResponse(
    val created: Long,
    val data: List<ImageData>
)

data class ImageData(
    val url: String
)

interface ImageGenerationApi {
    @POST("images/generations")
    suspend fun generateImage(@Body request: ImageRequest): ImageResponse
}