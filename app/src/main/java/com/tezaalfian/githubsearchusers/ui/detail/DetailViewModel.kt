package com.tezaalfian.githubsearchusers.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tezaalfian.githubsearchusers.data.UsersRepository
import com.tezaalfian.githubsearchusers.data.local.entity.UsersEntity
import com.tezaalfian.githubsearchusers.data.remote.response.UserListItem
import com.tezaalfian.githubsearchusers.utils.Event
import com.tezaalfian.githubsearchusers.data.remote.retrofit.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(private val usersRepository: UsersRepository) : ViewModel() {

    private val _followers = MutableLiveData<List<UserListItem>>()
    val followers: LiveData<List<UserListItem>> = _followers

    private val _following = MutableLiveData<List<UserListItem>>()
    val following: LiveData<List<UserListItem>> = _following

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _toastText = MutableLiveData<Event<String>>()
    val toastText: LiveData<Event<String>> = _toastText

    fun getFollowers(username: String?) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowers(username)
        client.enqueue(object : Callback<List<UserListItem>> {
            override fun onResponse(
                call: Call<List<UserListItem>>,
                response: Response<List<UserListItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _followers.value = response.body()
                } else {
                    _toastText.value = Event("Tidak ada data yang ditampilkan!")
                }
            }
            override fun onFailure(call: Call<List<UserListItem>>, t: Throwable) {
                _isLoading.value = false
                _toastText.value = Event("onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getFollowing(username: String?) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowing(username)
        client.enqueue(object : Callback<List<UserListItem>> {
            override fun onResponse(
                call: Call<List<UserListItem>>,
                response: Response<List<UserListItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _following.value = response.body()
                } else {
                    _toastText.value = Event("Tidak ada data yang ditampilkan!")
                }
            }
            override fun onFailure(call: Call<List<UserListItem>>, t: Throwable) {
                _isLoading.value = false
                _toastText.value = Event("onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getUser(username: String) = usersRepository.getUser(username)

    fun saveFavourite(user: UsersEntity) {
        viewModelScope.launch {
            usersRepository.setUsersFavourite(user, true)
        }
    }

    fun deleteFavourite(user: UsersEntity) {
        viewModelScope.launch {
            usersRepository.setUsersFavourite(user, false)
        }
    }

    fun getFavouriteUsers() = usersRepository.getFavouriteUsers()

    fun deleteAll(){
        viewModelScope.launch {
            usersRepository.deleteAll()
        }
    }
}