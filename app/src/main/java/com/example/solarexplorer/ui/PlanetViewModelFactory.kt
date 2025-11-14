package com.example.solarexplorer.ui
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.solarexplorer.data.datastore.DataStoreManager
import com.example.solarexplorer.viewmodel.PlanetViewModel

class PlanetViewModelFactory(private val dataStoreManager: DataStoreManager) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlanetViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PlanetViewModel(dataStoreManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
