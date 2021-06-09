package com.example.svrmaps.system.message

data class SystemMessage(
    val text: String,
    val type: SystemMessageType = SystemMessageType.ALERT
)