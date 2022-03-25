package com.tezaalfian.githubsearchusers.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.tezaalfian.githubsearchusers.data.local.entity.UsersEntity

@Dao
interface UsersDao {
    @Query("SELECT * FROM users where favourite = 1 order by username")
    fun getFavouriteUsers(): LiveData<List<UsersEntity>>

    @Query("SELECT EXISTS(SELECT * FROM users WHERE username = :username AND favourite = 1)")
    suspend fun isUsersFavourite(username: String?): Boolean

    @Update
    suspend fun updateUser(users: UsersEntity)

    @Query("DELETE FROM users WHERE username = :username")
    suspend fun delete(username: String?)

    @Query("DELETE FROM users WHERE favourite = 0")
    suspend fun deleteAll()

    @Query("SELECT * FROM users where username = :username")
    fun getUser(username: String): LiveData<UsersEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(user: UsersEntity)
}