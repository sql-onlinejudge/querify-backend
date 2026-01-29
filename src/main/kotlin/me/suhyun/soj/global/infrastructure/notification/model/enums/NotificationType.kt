package me.suhyun.soj.global.infrastructure.notification.model.enums

enum class NotificationType(
    val title: String,
    val template: String
) {
    SUBMISSION("📝 쿼리 제출", "제출 아이디: %s\n쿼리: ```%s```"),
}
