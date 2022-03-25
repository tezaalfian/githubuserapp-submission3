package com.tezaalfian.githubsearchusers.data.remote.retrofit

import com.tezaalfian.githubsearchusers.BuildConfig
import com.tezaalfian.githubsearchusers.data.remote.response.UserDetailResponse
import com.tezaalfian.githubsearchusers.data.remote.response.UserListItem
import com.tezaalfian.githubsearchusers.data.remote.response.UserResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @Headers("Authorization: token ${BuildConfig.GITHUB_TOKEN}")
    @GET("search/users")
    fun getUsers(
        @Query("q") username: String
    ): Call<UserResponse>

    @Headers("Authorization: token ${BuildConfig.GITHUB_TOKEN}")
    @GET("users/{username}")
    suspend fun getUserDetail(
        @Path("username") username: String
    ): UserDetailResponse

    @Headers("Authorization: token ${BuildConfig.GITHUB_TOKEN}")
    @GET("users/{username}/followers")
    fun getFollowers(
        @Path("username") username: String?
    ): Call<List<UserListItem>>

    @Headers("Authorization: token ${BuildConfig.GITHUB_TOKEN}")
    @GET("users/{username}/following")
    fun getFollowing(
        @Path("username") username: String?
    ): Call<List<UserListItem>>
}