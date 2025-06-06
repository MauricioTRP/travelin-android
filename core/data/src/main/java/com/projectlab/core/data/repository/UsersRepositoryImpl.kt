package com.projectlab.core.data.repository

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import com.projectlab.core.database.services.FirestoreUser
import com.projectlab.core.domain.model.Response
import com.projectlab.core.domain.entity.UserEntity
import com.projectlab.core.domain.model.User
import com.projectlab.core.domain.repository.UsersRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/** * UsersRepositoryImpl is a concrete implementation of the UsersRepository interface.
 * It performs operations on user data.
 *
 * @param usersRef The Firestore collection reference for users.
 * @param firestoreUser The FirestoreUserImpl instance used for user-related operations.
 */

class UsersRepositoryImpl @Inject constructor(
    private val usersRef: CollectionReference,
    private val firestoreUser: FirestoreUser,
) : UsersRepository {

    override suspend fun createUser(user: User): Response<Boolean> {
        return try {
            usersRef.document(user.id).set(user).await()
            Response.Success(true)
        } catch (e: Exception) {
            e.printStackTrace()
            Response.Failure(e)
        }
    }

    override fun getUserById(id: String): Flow<User> = callbackFlow {
        val snapshotListener = usersRef.document(id).addSnapshotListener { snapshot, e ->
            val user = snapshot?.toObject(User::class.java) ?: User(
                id = "",
                email = "",
                age = "",
                firstName = "",
                lastName = "",
                countryCode = "",
                phoneNumber = "",
            )
            trySend(user)
            println(user)
        }

        awaitClose {
            snapshotListener.remove()
        }
    }

    override suspend fun getAllUsers(): Result<List<UserEntity>> {
        return firestoreUser.getAllUsers()
    }

    override suspend fun updateUser(user: UserEntity): Result<Unit> {
        return firestoreUser.updateUser(user)
    }

    override suspend fun deleteUser(id: String): Result<Unit> {
        return firestoreUser.deleteUser(id)
    }


}