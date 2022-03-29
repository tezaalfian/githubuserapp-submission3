package com.tezaalfian.githubsearchusers.ui.favourite

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tezaalfian.githubsearchusers.R
import com.tezaalfian.githubsearchusers.data.remote.response.UserListItem
import com.tezaalfian.githubsearchusers.databinding.ActivityFavouriteBinding
import com.tezaalfian.githubsearchusers.ui.ListUserAdapter
import com.tezaalfian.githubsearchusers.ui.detail.DetailUserActivity
import com.tezaalfian.githubsearchusers.ui.detail.ViewModelFactory

class FavouriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavouriteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavouriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        this.title = resources.getString(R.string.app_name3)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        val viewModel: FavouriteViewModel by viewModels {
            factory
        }
        viewModel.getFavouriteUsers().observe(this) { user ->
            binding.progressBar.visibility = View.GONE
            val userList = user.map {
                UserListItem(it.username, it.avatar)
            }
            val userAdapter = ListUserAdapter(userList as ArrayList<UserListItem>)
            binding.rvUsers.adapter = userAdapter
            userAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
                override fun onItemClicked(data: UserListItem) {
                    showSelectedUser(data)
                }
            })
        }
        if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.rvUsers.layoutManager = GridLayoutManager(this, 2)
        } else {
            binding.rvUsers.layoutManager = LinearLayoutManager(this)
        }
        binding.rvUsers.setHasFixedSize(true)
    }

    private fun showSelectedUser(user: UserListItem) {
        val detailUserIntent = Intent(this, DetailUserActivity::class.java)
        detailUserIntent.putExtra(DetailUserActivity.EXTRA_USER, user.username)
        startActivity(detailUserIntent)
    }
}