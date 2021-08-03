package co.eware.infinitescroll

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.eware.infinitescroll.MainViewModel.Companion.DEFAULT_PAGE_SIZE
import co.eware.infinitescroll.databinding.ActivityMainBinding
import co.eware.infinitescroll.model.MainActivityEvents
import co.eware.infinitescroll.model.MainActivityViewState
import kotlinx.coroutines.flow.collect

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel>()

    private var _adapter : DataModelAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()
        setupRecyclerView()
        requestInitialData()
        observeUI()
    }

    private fun initUI() {
        _adapter = DataModelAdapter()
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            adapter = _adapter
            setHasFixedSize(true)
            addOnScrollListener(createInfiniteScrollListener(layoutManager as LinearLayoutManager))
        }
    }

    private fun requestInitialData() {
        viewModel.onEvent(MainActivityEvents.RequestMoreData)
    }

    private fun observeUI() {
        lifecycleScope.launchWhenStarted {
            viewModel.state.collect { handleState(it) }
        }
    }

    private fun handleState(viewState: MainActivityViewState) {
        val (loading, dataList, noMoreData) = viewState
        handleLoading(loading)
        renderDataList(dataList)
    }

    private fun renderDataList(dataList: List<Person>) {
        if (dataList.isEmpty()) return
        _adapter?.submitList(dataList)
    }

    private fun handleLoading(loading: Boolean) {
        binding.progressBar.isVisible = loading
    }

    private fun createInfiniteScrollListener(layoutManager: LinearLayoutManager): RecyclerView.OnScrollListener {
        return object : InfiniteScrollListener(
            layoutManager,
            DEFAULT_PAGE_SIZE
        ) {
            override fun loadMoreItems() {
                requestMoreData()
            }

            override fun isLoading(): Boolean = viewModel.isLoadingMoreData
            override fun isLastPage(): Boolean = viewModel.isLastPage
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _adapter = null
    }

    private fun requestMoreData() {
        viewModel.onEvent(MainActivityEvents.RequestMoreData)
    }

}