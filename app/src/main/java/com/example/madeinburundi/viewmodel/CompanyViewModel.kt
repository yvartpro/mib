package com.example.madeinburundi.viewmodel


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.madeinburundi.data.model.Company
import com.example.madeinburundi.data.repository.CompanyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompanyViewModel @Inject constructor(
  private val companyRepository: CompanyRepository
) : ViewModel() {

  var companies by mutableStateOf<List<Company>>(emptyList())
    private set

  var isLoading by mutableStateOf(false)
    private set

  fun loadCompanies() {
    viewModelScope.launch {
      isLoading = true
      val result = companyRepository.getCompanies()
      if (result.isNotEmpty()) {
        companies = result
        isLoading = false
      }
    }
  }
}
