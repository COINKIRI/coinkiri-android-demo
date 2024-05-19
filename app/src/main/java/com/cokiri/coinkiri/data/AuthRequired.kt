package com.cokiri.coinkiri.data

/**
 * 인증이 필요한 API에 사용하는 어노테이션
 * 사용시 해당 API는 인증이 필요하다는 것을 의미
 */

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class AuthRequired
