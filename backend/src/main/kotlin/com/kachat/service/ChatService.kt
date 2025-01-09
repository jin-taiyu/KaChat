package com.kachat.service

import com.kachat.db.MessageTable
import com.kachat.db.UserTable
import com.kachat.dto.MessageDto
import com.kachat.models.Message
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class ChatService {

    fun saveMessage(dto: MessageDto) {
        val sender = transaction {
            UserTable.select { UserTable.username eq dto.senderName }.singleOrNull()
        } ?: throw RuntimeException("发送者用户不存在")

        val recipient = dto.recipientName?.let { rName ->
            transaction {
                UserTable.select { UserTable.username eq rName }.singleOrNull()
            } ?: throw RuntimeException("接收者用户不存在")
        }

        transaction {
            MessageTable.insert {
                it[senderId] = sender[UserTable.id]
                it[recipientId] = recipient?.get(UserTable.id)
                it[content] = dto.content
                it[roomType] = dto.roomType
                it[createTime] = java.time.LocalDateTime.now()
            }
        }
    }

    fun getMessages(roomType: String, sender: String?, recipient: String?): List<Message> {
        return transaction {
            val query = if (roomType == "PRIVATE" && sender != null && recipient != null) {
                val senderUser = UserTable.select { UserTable.username eq sender }.singleOrNull()
                    ?: throw RuntimeException("用户 $sender 不存在")
                val recipientUser = UserTable.select { UserTable.username eq recipient }.singleOrNull()
                    ?: throw RuntimeException("用户 $recipient 不存在")

                val senderId = senderUser[UserTable.id]
                val recipientId = recipientUser[UserTable.id]

                MessageTable.select {
                    (
                            ((MessageTable.senderId eq senderId) and (MessageTable.recipientId eq recipientId)) or
                                    ((MessageTable.senderId eq recipientId) and (MessageTable.recipientId eq senderId))
                            ) and
                            (MessageTable.roomType eq roomType)
                }
            } else {
                MessageTable.select { MessageTable.roomType eq roomType }
            }

            query.orderBy(MessageTable.createTime to SortOrder.ASC)
                .map {
                    Message(
                        id = it[MessageTable.id].value,
                        senderId = it[MessageTable.senderId].value,
                        recipientId = it[MessageTable.recipientId]?.value,
                        content = it[MessageTable.content],
                        roomType = it[MessageTable.roomType],
                        createTime = it[MessageTable.createTime].toString()
                    )
                }
        }
    }
}
