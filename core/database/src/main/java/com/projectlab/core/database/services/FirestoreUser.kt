package com.projectlab.core.database.services

import com.projectlab.core.domain.entity.UserEntity
import com.projectlab.core.domain.model.EntityId
import kotlinx.coroutines.flow.Flow

/**
 * FirestoreUser interface for user-related operations.
 *
 * This interface defines the contract for user-related data operations, including creating a user
 * and retrieving a user by their ID.
 *
 * @author ricardoceadev
 */


interface FirestoreUser {
    suspend fun createUser(user: UserEntity): Result<EntityId>
    suspend fun getUserById(id: String): Result<UserEntity?>
    suspend fun getAllUsers(): Result<List<UserEntity>>
    suspend fun updateUser(user: UserEntity): Result<Unit>
    suspend fun deleteUser(id: String): Result<Unit>
}