package com.tezaalfian.githubsearchusers.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.tezaalfian.githubsearchusers.data.local.entity.UsersEntity
import com.tezaalfian.githubsearchusers.data.local.room.UsersDao
import com.tezaalfian.githubsearchusers.data.remote.retrofit.ApiService

class UsersRepository(
    private val apiService: ApiService,
    private val usersDao: UsersDao
) {

    fun getFavouriteUsers() : LiveData<List<UsersEntity>>{
        return usersDao.getFavouriteUsers()
    }

    suspend fun deleteAll() {
        usersDao.deleteAll()
    }

    suspend fun setUsersFavourite(user: UsersEntity, favouriteState: Boolean){
        user.isFavourite = favouriteState
        usersDao.updateUser(user)
    }

    fun getUser(username: String): LiveData<Result<UsersEntity>> = liveData{
        emit(Result.Loading)
        try {
            val user = apiService.getUserDetail(username)
            val isFavourite = usersDao.isUsersFavourite(user.username)
            usersDao.delete(user.username)
            usersDao.insertUser(UsersEntity(
                user.username,
                user.company,
                user.repository,
                user.followers,
                user.avatar,
                user.following,
                user.name,
                user.location,
                isFavourite
            ))
        }catch (e : Exception){
            Log.d("UsersRepository", "getUser: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
        val localData : LiveData<Result<UsersEntity>> = usersDao.getUser(username).map { Result.Success(it) }
        emitSource(localData)
    }

    companion object {
        @Volatile
        private var instance: UsersRepository? = null
        fun getInstance(
            apiService: ApiService,
            usersDao: UsersDao
        ): UsersRepository =
            instance ?: synchronized(this) {
                instance ?: UsersRepository(apiService, usersDao)
            }.also { instance = it }
    }
}