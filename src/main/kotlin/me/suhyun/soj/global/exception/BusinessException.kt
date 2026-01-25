package me.suhyun.soj.global.exception

class BusinessException(
    val errorCode: ErrorCode
) : RuntimeException(errorCode.message)
