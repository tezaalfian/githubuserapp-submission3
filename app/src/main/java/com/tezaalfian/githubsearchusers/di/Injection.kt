package com.tezaalfian.githubsearchusers.di

import android.content.Context
import com.tezaalfian.githubsearchusers.data.UsersRepository
import com.tezaalfian.githubsearchusers.data.local.room.UsersDatabase
import com.tezaalfian.githubsearchusers.data.remote.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): UsersRepository {
        val apiService = ApiConfig.getApiService()
        val database = UsersDatabase.getInstance(context)
        val dao = database.usersDao()
        return UsersRepository.getInstance(apiService, dao)
    }
}