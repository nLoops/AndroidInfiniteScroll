package co.eware.infinitescroll.model


sealed class MainActivityEvents {
    object RequestMoreData : MainActivityEvents()
}