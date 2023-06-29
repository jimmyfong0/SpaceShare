package com.example.spaceshare.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spaceshare.data.repository.FirebaseStorageRepository
import com.example.spaceshare.data.repository.ListingRepository
import com.example.spaceshare.models.Listing
import com.example.spaceshare.models.User
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

class ListingViewModel @Inject constructor(
    private val repo: ListingRepository,
    private val firebaseStorageRepo: FirebaseStorageRepository
): ViewModel() {

    private val _listingsLiveData: MutableLiveData<List<Listing>> = MutableLiveData()
    val listingsLiveData: LiveData<List<Listing>> = _listingsLiveData

    fun fetchListings(user: User) {
        viewModelScope.launch {
            val listings = repo.getUserListings(user)
            _listingsLiveData.value = listings
        }
    }

    fun addItem(listing: Listing) {
        val currentList = _listingsLiveData.value.orEmpty().toMutableList()
        currentList.add(0, listing)
        Log.i("tag", "Added $listing to currentList")
        for (list in currentList) {
            Log.i("tag", "$list")
        }
        _listingsLiveData.value = currentList
    }

     fun removeItem(listing: Listing) {
         val updatedList = _listingsLiveData.value?.toMutableList()
         updatedList?.remove(listing)
         _listingsLiveData.value = updatedList ?: emptyList()
         viewModelScope.launch {
             // Delete listing
             repo.deleteListing(listing.id!!)

             // Delete corresponding images
             for (filePath in listing.photos) {
                 viewModelScope.async {
                     try {
                         firebaseStorageRepo.deleteFile("spaces", filePath)
                     } catch (e: Exception) {
                         Log.e("ListingViewModel", "Error deleting image: ${e.message}", e)
                     }
                 }
             }
         }
     }
}