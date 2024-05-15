package com.cokiri.coinkiri.data.remote.api

import retrofit2.http.Body
import retrofit2.http.POST

interface SignUpApi {

    @POST("/api/v1/auth/signup")
    suspend fun signUpUser(@Body accessToken: String)  // Body에 accessToken을 담아 서버로 전송

}