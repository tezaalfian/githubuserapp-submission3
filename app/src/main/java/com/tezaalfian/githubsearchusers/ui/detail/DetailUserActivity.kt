package com.tezaalfian.githubsearchusers.ui.detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.tezaalfian.githubsearchusers.R
import com.tezaalfian.githubsearchusers.data.Result
import com.tezaalfian.githubsearchusers.data.local.entity.UsersEntity
import com.tezaalfian.githubsearchusers.databinding.ActivityDetailUserBinding
import com.tezaalfian.githubsearchusers.ui.favourite.FavouriteViewModel

class DetailUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var username: String
    private lateinit var favouriteViewModel : FavouriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        this.title = resources.getString(R.string.app_name2)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        favouriteViewModel = ViewModelProvider(this, factory)[FavouriteViewModel::class.java]

        username = intent.getStringExtra(EXTRA_USER) as String

        favouriteViewModel.getUser(username)
        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.username = username

        binding.viewPager.adapter = sectionsPagerAdapter
        supportActionBar?.elevation = 0f

        favouriteViewModel.getUser(username).observe(this){ result ->
            if (result != null){
                when(result) {
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                        val user = result.data
                        setUserData(user)
                    }
                    is Result.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(
                            this,
                            "Terjadi kesalahan" + result.error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun setUserData(user: UsersEntity) {
        binding.apply {
            tvName.text = user.name
            tvUsername.text = user.username
            tvCompany.text = user.company
            tvLocation.text = user.location
            tvRepository.text = resources.getString(R.string.repository, user.repository)
            ivFavourite.setImageDrawable(
                ContextCompat.getDrawable(ivFavourite.context,
                    if (user.isFavourite) R.drawable.ic_purple_favorite
                    else R.drawable.ic_purple_favorite_border
                )
            )
            ivFavourite.setOnClickListener {
                if (user.isFavourite){
                    favouriteViewModel.deleteFavourite(user)
                } else {
                    favouriteViewModel.saveFavourite(user)
                }
            }
        }
        Glide.with(this)
            .load(user.avatar)
            .circleCrop()
            .into(binding.imgAvatar)
        val countFollow = arrayOf(user.following, user.followers)
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position], countFollow[position])
        }.attach()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.detail_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.share -> {
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.share, username))
                    type = "text/plain"
                }
                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(shareIntent)
                true
            }
            else -> true
        }
    }

    companion object {
        const val EXTRA_USER = "extra_user"
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }
}