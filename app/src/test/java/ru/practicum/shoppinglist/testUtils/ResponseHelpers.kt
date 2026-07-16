package ru.practicum.shoppinglist.testUtils

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import retrofit2.Response

fun <T> successResponse(body: T, code: Int = 200): Response<T> =
    Response.success(code, body)

fun <T> errorResponse(errorBody: String, code: Int = 400): Response<T> =
    Response.error(code, ResponseBody.create("application/json".toMediaTypeOrNull(), errorBody))
