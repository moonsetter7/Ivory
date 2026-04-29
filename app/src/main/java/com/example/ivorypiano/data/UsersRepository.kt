package com.example.ivorypiano.data

import kotlinx.coroutines.flow.Flow

/**
 * Repository that provides insert, update, delete, and retrieve of a user from a given data source.
 */
interface UsersRepository {
    /**
     * Retrieve all users from the given data source.
     */
    fun getAllUsersStream(): Flow<List<User>>

    /**
     * Retrieve a user from the given data source based on their ID.
     */
    fun getUserStream(id: Int): Flow<User?>

    /**
     * Insert user into the data source.
     */
    suspend fun insertUser(user: User)

    /**
     * Delete user from the data source.
     */
    suspend fun deleteUser(user: User)

    /**
     * Update user in the data source.
     */
    suspend fun updateUser(user: User)
}
