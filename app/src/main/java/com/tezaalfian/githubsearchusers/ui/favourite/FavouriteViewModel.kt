package com.tezaalfian.githubsearchusers.ui.favourite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tezaalfian.githubsearchusers.data.UsersRepository
import com.tezaalfian.githubsearchusers.data.local.entity.UsersEntity
import kotlinx.coroutines.launch

class FavouriteViewModel(private val usersRepository: UsersRepository) : ViewModel() {

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
}