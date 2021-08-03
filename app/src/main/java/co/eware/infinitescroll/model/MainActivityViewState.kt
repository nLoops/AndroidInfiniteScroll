package co.eware.infinitescroll.model

import co.eware.infinitescroll.Person


data class MainActivityViewState(
    val loading: Boolean = true,
    val persons: List<Person> = emptyList(),
    val noMoreData: Boolean = false,
)