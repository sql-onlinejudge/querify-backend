package me.suhyun.soj.global.infrastructure.notification.model.enums

enum class NotificationType(
    val title: String,
    val color: Int
) {
    SUBMISSION("📝 쿼리 제출", 3447003),
    ERROR("🚨 Error Occurred", 16711680),
}
