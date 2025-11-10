package bi.vovota.madeinburundi.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlin.fold

fun <T> ViewModel.launchWithState(
  stateFlow: MutableStateFlow<UiState<T>>,
  block: suspend () -> Result<T>,
  onSuccess: (T) -> Unit = {},
  onFailure: (Throwable) -> Unit = {}
) {
  stateFlow.value = stateFlow.value.copy(isLoading = true, error = null)
  viewModelScope.launch {
    block().fold(
      onSuccess = { data->
        stateFlow.value = UiState(success = true, data = data)
        onSuccess(data)
      },
      onFailure = { e->
        stateFlow.value = UiState(error = e.message)
        onFailure(e)
      }
    )
  }
}

data class UiState<T>(
  val isLoading: Boolean = false,
  val success: Boolean = false,
  val error: String? = null,
  val data: T? = null
)