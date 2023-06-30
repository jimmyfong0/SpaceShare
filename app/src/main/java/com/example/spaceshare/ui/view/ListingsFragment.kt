package com.example.spaceshare.ui.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Filter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.spaceshare.R
import com.example.spaceshare.adapters.ListingAdapter
import com.example.spaceshare.adapters.ListingAdapter.ItemClickListener
import com.example.spaceshare.databinding.FragmentListingsBinding
import com.example.spaceshare.models.FilterCriteria
import com.example.spaceshare.models.Listing
import com.example.spaceshare.models.User
import com.example.spaceshare.ui.viewmodel.CreateListingViewModel
import com.example.spaceshare.ui.viewmodel.ListingViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import java.util.Objects
import javax.inject.Inject
import com.example.spaceshare.ui.viewmodel.CreateListingViewModel.CreateListingDialogListener

@AndroidEntryPoint
class ListingsFragment : Fragment(), CreateListingDialogListener {

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
        adapter = ListingAdapter(object : ItemClickListener {
            override fun onItemClick(listing: Listing) {
                val hostListingDialogFragment = HostListingDialogFragment(listing, listingViewModel)
                hostListingDialogFragment.show(Objects.requireNonNull(childFragmentManager),
                    "hostListingDialog")
            }
        })
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun configureButtons() {
        binding.btnAddListing.setOnClickListener {
            val createListingDialogFragment = CreateListingDialogFragment()
            createListingDialogFragment.show(Objects.requireNonNull(childFragmentManager), "createListingDialog")
        }

        binding.btnFilter.setOnClickListener {
            val filterDialogFragment = FilterDialogFragment(listingViewModel)
            filterDialogFragment.show(Objects.requireNonNull(childFragmentManager), "filterDialog")
        }
    }

    private fun configureObservers() {
        listingViewModel.listingsLiveData.observe(viewLifecycleOwner) { listings ->
            binding.btnFilter.isEnabled = listings.isNotEmpty()
        }
        listingViewModel.filteredListingsLiveData.observe(viewLifecycleOwner) { listings ->
            adapter.submitList(listings)
        }
        listingViewModel.fetchListings(User(FirebaseAuth.getInstance().currentUser?.uid!!))

        binding.searchTextView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                return
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                return
            }

            override fun afterTextChanged(s: Editable?) {
                listingViewModel.filterListings(s.toString(), listingViewModel.getCriteria())
            }
        })
    }

    override fun onListingCreated(listing: Listing?) {
        if (listing != null) {
            listingViewModel.addItem(listing)
            listingViewModel.filterListings(binding.searchTextView.text.toString(), listingViewModel.getCriteria())
            Toast.makeText(requireContext(), "Listing successfully published", Toast.LENGTH_SHORT).show()
            binding.recyclerView.scrollToPosition(0)
        } else {
            Toast.makeText(requireContext(), "Error publishing listing. Please try again later", Toast.LENGTH_SHORT).show()
        }
    }
}