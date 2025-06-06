package com.projectlab.core.domain.repository

import com.projectlab.core.domain.entity.UserEntity
import com.projectlab.core.domain.model.Response
import com.projectlab.core.domain.model.User
import kotlinx.coroutines.flow.Flow

/**
 * UsersRepository interface for user-related operations.
 *
 * This interface defines the contract for user-related data operations, including creating a user,
 * retrieving a user by their ID, and managing user data.
 *
 */

interface UsersRepository {
    suspend fun createUser(user: User): Response<Boolean>
    fun getUserById(id: String): Flow<User>
    suspend fun getAllUsers(): Result<List<UserEntity>>
    suspend fun updateUser(user: UserEntity): Result<Unit>
    suspend fun deleteUser(id: String): Result<Unit>
}