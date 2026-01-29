package me.suhyun.soj.global.exception

data class ErrorResponse(
    val code: String,
    val message: String
) {
    companion object {
        fun of(errorCode: ErrorCode) = ErrorResponse(
            code = (errorCode as Enum<*>).name,
            message = errorCode.message
        )
    }
}
