package com.tezaalfian.githubsearchusers.ui.detail.follows

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.tezaalfian.githubsearchusers.data.remote.response.UserListItem
import com.tezaalfian.githubsearchusers.databinding.FragmentFollowsBinding
import com.tezaalfian.githubsearchusers.ui.ListUserAdapter
import com.tezaalfian.githubsearchusers.ui.detail.DetailUserActivity

class FollowsFragment : Fragment() {

    private var _binding: FragmentFollowsBinding? = null
    private val binding get() = _binding

    private val followViewModel: FollowViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FrameLayout? {
        _binding = FragmentFollowsBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        when(arguments?.getInt(ARG_SECTION_NUMBER, 0)){
            1 -> {
                followViewModel.getFollowing(arguments?.getString(USERNAME))
                followViewModel.following.observe(requireActivity()) { users ->
                    setUserData(users)
                }
            }
            2 -> {
                followViewModel.getFollowers(arguments?.getString(USERNAME))
                followViewModel.followers.observe(requireActivity()) { users ->
                    setUserData(users)
                }
            }
        }
        binding?.rvFollows?.setHasFixedSize(true)
        followViewModel.isLoading.observe(requireActivity()) {
            showLoading(it)
        }

        followViewModel.toastText.observe(requireActivity()) {
            it.getContentIfNotHandled()?.let { toastText ->
                Toast.makeText(requireActivity(), toastText, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setUserData(users: List<UserListItem>?) {
        val listUserAdapter = ListUserAdapter(users as ArrayList<UserListItem>)
        binding?.rvFollows?.adapter = listUserAdapter

        listUserAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UserListItem) {
                showSelectedUser(data)
            }
        })
    }

    private fun showSelectedUser(user: UserListItem) {
        val detailUserIntent = Intent(activity, DetailUserActivity::class.java)
        detailUserIntent.putExtra(DetailUserActivity.EXTRA_USER, user.username)
        startActivity(detailUserIntent)
    }

    private fun showLoading(isLoading: Boolean) {
        binding?.progressBar?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val ARG_SECTION_NUMBER = "section_number"
        const val USERNAME = "username"
    }
}