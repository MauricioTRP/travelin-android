package com.projectlab.core.database.services



import kotlinx.coroutines.tasks.await
import javax.inject.Inject

import com.google.firebase.firestore.FirebaseFirestore
import com.projectlab.core.database.dto.FirestoreUserDTO
import com.projectlab.core.domain.model.EntityId
import com.projectlab.core.domain.entity.UserEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * FirestoreUserImpl is a concrete implementation of the FirestoreUser interface.
 * It uses Firebase Firestore to perform CRUD operations on user data.
 *
 * @param firestore The FirebaseFirestore instance used to interact with Firestore.
 */

class FirestoreUserImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : FirestoreUser {

    private var userCol = firestore.collection("Users")

    override suspend fun createUser(user: UserEntity): Result<EntityId> = runCatching {
        // Convert domain to DTO:
        val dto = FirestoreUserDTO.fromDomain(user)
        // add to firestore:
        val docRef = userCol.document()
        docRef.set(dto).await()
        // return the id:
        EntityId(docRef.id)
    }

    override suspend fun getUserById(id: String): Result<UserEntity?> = runCatching {
        // Reference user document by ID:
        val snap = userCol.document(id).get().await()
        if (snap.exists()) {
            val dto = snap.toObject(FirestoreUserDTO::class.java)
            (dto?.toDomain(snap.id))
        } else {
            (null)
        }
    }

    override suspend fun getAllUsers(): Result<List<UserEntity>> = runCatching {
        // Fetch all users from the collection
        val snap = userCol.get().await()
        val list = snap.documents.mapNotNull { doc ->
            doc.toObject(FirestoreUserDTO::class.java)
                ?.toDomain(doc.id)
        }
        (list)
    }

    override suspend fun updateUser(user: UserEntity): Result<Unit> = runCatching {
        // convert updated domain to DTO:
        val dto = FirestoreUserDTO.fromDomain(user)
        // Overwrite the user document:
        userCol.document(user.id).set(dto).await()
    }

    override suspend fun deleteUser(id: String): Result<Unit> = runCatching {
        // Delete the user document by ID:
        userCol.document(id).delete().await()

    }
}