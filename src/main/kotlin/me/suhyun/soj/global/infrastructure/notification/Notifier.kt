package me.suhyun.soj.global.infrastructure.notification

import me.suhyun.soj.global.infrastructure.notification.model.enums.NotificationType

interface Notifier {
    fun notify(type: NotificationType, vararg bodies: Any)
}
