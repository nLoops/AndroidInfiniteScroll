package co.eware.infinitescroll

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.eware.infinitescroll.model.MainActivityEvents
import co.eware.infinitescroll.model.MainActivityViewState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class MainViewModel : ViewModel() {

    val state: StateFlow<MainActivityViewState> get() = _state
    var isLoadingMoreData:Boolean = false
    var isLastPage = false

    private var dataSet = List(5000) { index -> Person(index, "Person $index") }
    private val _state = MutableStateFlow(MainActivityViewState())
    private var currentPage = 1

    fun onEvent(event:MainActivityEvents){
        when(event){
            is MainActivityEvents.RequestMoreData -> loadMoreData()
        }
    }

    private fun loadMoreData() {
        isLoadingMoreData = true
        viewModelScope.launch {
            _state.value = state.value.copy(loading = true)
            delay(2000)
            val currentList = state.value.persons
            val newDataSet = dataSet.take(getItemsToLoad()).subtract(currentList)
            val updatedList = currentList + newDataSet
            _state.value = state.value.copy(loading = false, persons = updatedList)
            currentPage++
        }
        isLoadingMoreData = false
    }

    private fun getItemsToLoad() = currentPage * DEFAULT_PAGE_SIZE

    companion object{
        const val DEFAULT_PAGE_SIZE = 20
    }
}