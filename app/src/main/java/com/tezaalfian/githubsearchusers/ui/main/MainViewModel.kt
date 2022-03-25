package com.tezaalfian.githubsearchusers.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tezaalfian.githubsearchusers.data.remote.response.UserListItem
import com.tezaalfian.githubsearchusers.data.remote.response.UserResponse
import com.tezaalfian.githubsearchusers.utils.Event
import com.tezaalfian.githubsearchusers.data.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    private val _userList = MutableLiveData<List<UserListItem>>()
    val userList: LiveData<List<UserListItem>> = _userList

    private val _userCount = MutableLiveData<Int>()
    val userCount: LiveData<Int> = _userCount

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _toastText = MutableLiveData<Event<String>>()
    val toastText: LiveData<Event<String>> = _toastText

    fun findUser(username : String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUsers(username)
        client.enqueue(object : Callback<UserResponse> {
            override fun onResponse(
                call: Call<UserResponse>,
                response: Response<UserResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _userList.value = response.body()?.userList as List<UserListItem>
                    _userCount.value = response.body()?.totalCount
                } else {
                    _toastText.value = Event("Tidak ada data yang ditampilkan!")
                }
            }
            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                _isLoading.value = false
                _toastText.value = Event("onFailure: ${t.message.toString()}")
            }
        })
    }

    companion object{
        private const val TAG = "MainViewModel"
    }
}