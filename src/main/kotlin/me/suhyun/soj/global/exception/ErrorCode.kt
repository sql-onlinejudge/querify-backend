package me.suhyun.soj.global.exception

import org.springframework.http.HttpStatus

interface ErrorCode {
    val status: HttpStatus
    val message: String
}
