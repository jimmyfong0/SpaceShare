package com.example.spaceshare.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.spaceshare.R
import com.example.spaceshare.databinding.FragmentListingsBinding
import com.example.spaceshare.models.User
import com.example.spaceshare.ui.viewmodel.CreateListingViewModel
import com.example.spaceshare.ui.viewmodel.ListingViewModel
import com.example.spaceshare.adapters.ListingAdapter
import com.example.spaceshare.models.Listing
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ListingsFragment : Fragment() {

    private lateinit var binding: FragmentListingsBinding
    private lateinit var navController: NavController
    @Inject
    lateinit var listingViewModel: ListingViewModel
    @Inject
    lateinit var createListingViewModel: CreateListingViewModel
    private lateinit var adapter: ListingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_listings, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = requireActivity().findNavController(R.id.main_nav_host_fragment)

        configureRecyclerView()
        configureButtons()
        configureObservers()
    }

    private fun configureRecyclerView() {
        adapter = ListingAdapter()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun configureButtons() {
        binding.btnAddListing.setOnClickListener {
            navController.navigate(R.id.action_listingFragment_to_createListingFragment)
        }
    }

    private fun configureObservers() {
        listingViewModel.listingsLiveData.observe(viewLifecycleOwner) { listings ->
            adapter.submitList(listings)
        }
        listingViewModel.fetchListings(User(FirebaseAuth.getInstance().currentUser?.uid!!))

        createListingViewModel.listingPublished.observe(viewLifecycleOwner) { published ->
            if (published) {
                Toast.makeText(requireContext(), "Listing successfully published", Toast.LENGTH_SHORT)
            } else {
                Toast.makeText(requireContext(), "Error publishing listing. Please try again later", Toast.LENGTH_SHORT)
            }
        }
    }
}