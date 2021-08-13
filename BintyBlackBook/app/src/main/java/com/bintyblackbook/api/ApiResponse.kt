package com.bintyblackbook.api

import androidx.annotation.NonNull
import androidx.annotation.Nullable
import com.google.gson.JsonElement

class ApiResponse(status: Status, @Nullable data: JsonElement?, @Nullable error: Throwable?) {
    lateinit var status: Status
    @Nullable
    lateinit var data: JsonElement
    @Nullable
    lateinit var error: Throwable

    companion object {

        fun loading(): ApiResponse {
            return ApiResponse(Status.LOADING, null, null)
        }

        fun success(@NonNull data: JsonElement): ApiResponse {
            return ApiResponse(Status.SUCCESS, data, null)
        }

        fun error(@NonNull error: Throwable): ApiResponse {
            return ApiResponse(Status.ERROR, null, error)
        }
    }
}