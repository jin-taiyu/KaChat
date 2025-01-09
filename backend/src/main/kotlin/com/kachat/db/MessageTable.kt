package com.kachat.db

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime

object MessageTable : IntIdTable("messages") {
    val senderId = reference("sender_id", UserTable)
    val recipientId = optReference("recipient_id", UserTable)
    val content = text("content")
    val roomType = varchar("room_type", 20)
    val createTime = datetime("create_time").clientDefault { java.time.LocalDateTime.now() }
}
