package com.cokiri.coinkiri.data.remote.api

import com.cokiri.coinkiri.data.remote.model.LoginRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface SignUpApi {

    @POST("/api/v1/auth/signup")
    suspend fun signUpUser(@Body loginRequest: LoginRequest)

}