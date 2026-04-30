package com.example.ivorypiano.data

import kotlinx.coroutines.flow.Flow

/**
 * Repository that provides insert, update, delete, and retrieve of a user from a given data source.
 */
interface UsersRepository {
    fun getAllUsersStream(): Flow<List<User>>
    fun getUserStream(id: Int): Flow<User?>
    suspend fun getUserByUsername(username: String): User?
    suspend fun getUserByEmail(email: String): User?
    suspend fun insertUser(user: User)
    suspend fun deleteUser(user: User)
    suspend fun updateUser(user: User)
}
