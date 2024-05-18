package com.cokiri.coinkiri.data

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesManager @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {

    /**
     * 동반 객체(Companion Object)
     * SharedPreferences에 저장할 토큰 키
     * 액세스 토큰과 리프레시 토큰을 저장할 때 사용
     * @property KEY_ACCESS_TOKEN  액세스 토큰 키
     * @property KEY_REFRESH_TOKEN 리프레시 토큰 키
     */
    companion object {
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_ACCESS_TOKEN = "access_token"
        private const val KEY_REFRESH_TOKEN = "refresh_token"
    }


    /**
     * 액세스 토큰 저장
     * @param token 저장할 액세스 토큰
     */
    fun saveAccessToken(token: String) {
        sharedPreferences
            .edit()                                 // SharedPreferences에 저장할 데이터를 수정하기 위한 Editor 객체 반환
            .putString(KEY_ACCESS_TOKEN, token)     // 액세스 토큰 저장
            .apply()                                // 변경사항 저장
    }


    /**
     * 리프레시 토큰 저장
     * @param token 저장할 리프레시 토큰
     */
    fun saveRefreshToken(token: String) {
        sharedPreferences
            .edit()                                 // SharedPreferences에 저장할 데이터를 수정하기 위한 Editor 객체 반환
            .putString(KEY_REFRESH_TOKEN, token)    // 리프레시 토큰 저장
            .apply()                                // 변경사항 저장
    }


    /**
     * 액세스 토큰 가져오기
     * @return 액세스 토큰
     */
    fun getAccessToken(): String? {
        return sharedPreferences
            .getString(KEY_ACCESS_TOKEN, null)
    }


    /**
     * 리프레시 토큰 가져오기
     * @return 리프레시 토큰
     */
    fun getRefreshToken(): String? {
        return sharedPreferences
            .getString(KEY_REFRESH_TOKEN, null)
    }


    /**
     * 토큰 삭제
     * 액세스 토큰과 리프레시 토큰을 삭제
     */
    fun clearTokens() {
        sharedPreferences
            .edit()                     // SharedPreferences에 저장된 데이터 삭제
            .remove(KEY_ACCESS_TOKEN)   // 액세스 토큰 삭제
            .remove(KEY_REFRESH_TOKEN)  // 리프레시 토큰 삭제
            .apply()                    // 변경사항 저장
    }

    var isLoggedIn: Boolean
        get() = sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
        set(value) { sharedPreferences.edit().putBoolean(KEY_IS_LOGGED_IN, value).apply() }
}