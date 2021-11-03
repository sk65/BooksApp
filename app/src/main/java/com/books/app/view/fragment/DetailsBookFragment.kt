package com.books.app.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.books.app.R
import com.books.app.databinding.FragmentDetailsBookBinding
import com.books.app.model.response.Book
import com.books.app.util.*
import com.books.app.view.fragment.adapter.ViewPagerAdapter
import com.books.app.view.fragment.adapter.YouWillLIkeAdapter
import com.books.app.viewmodel.DetailsBookViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DetailsBookFragment : Fragment(), (Long) -> Unit {
    @Inject
    lateinit var youWillLIkeListAdapter: YouWillLIkeAdapter

    @Inject
    lateinit var viewPagerAdapter: ViewPagerAdapter

    private val  detailsBookViewModel: DetailsBookViewModel by viewModels()

    private val args: DetailsBookFragmentArgs by navArgs()

    private var _binding: FragmentDetailsBookBinding? = null
    private val binding
        get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBookBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupViewPager()
        setupAppBar()

        detailsBookViewModel.apply {
            //Log.i("dev ", "pase value ${args.bookGenre}")
            fetchBookById(args.bookId)
        }
        detailsBookViewModel.youWillLikeResource.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    youWillLIkeListAdapter.differ.submitList(
                        resource.data!!
                    )
                }
            }
        }
        detailsBookViewModel.bookResource.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    setupUI(resource.data!!)
                    detailsBookViewModel.fetchGenreList(resource.data.genre)
                }
            }
        }
        detailsBookViewModel.booksResource.observe(viewLifecycleOwner) { resource ->
            Log.i("dev", "observ!!!")
            when (resource) {

                is Resource.Success -> {
                    Log.i("dev", "10000000   " + resource.data?.get(0)!!.genre)
                    val preparedList = resource.data.prepareForTwoWayPaging()
                    preparedList.forEach {
                        Log.i(
                            "dev",
                            "PreperedList id ${it.id} genre ${it.genre} pos ${
                                preparedList.indexOf(it)
                            }"
                        )
                    }
                    viewPagerAdapter.differ.submitList(preparedList)

                    viewPagerAdapter.differ.currentList.forEach {
                        Log.i(
                            "dev",
                            "Differ id ${it.id} genre ${it.genre} pos ${
                                viewPagerAdapter.differ.currentList.indexOf(it)
                            }"
                        )
                    }
                    Log.i("dev", "!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
                    preparedList.forEach {
                        if (it.id == args.bookId) {
                            binding.vpImageSlider.setCurrentItem(
                                viewPagerAdapter.differ.currentList.indexOf(
                                    it
                                ), false
                            )
                            Log.i(
                                "dev",
                                "id ${args.bookId} genre ${it.genre} pos ${
                                    viewPagerAdapter.differ.currentList.indexOf(it)
                                }"
                            )
                            Log.i("dev", "*******************************")
                            return@observe
                        }
                    }

                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupUI(book: Book) {
        binding.apply {
            tvQuotes.text = book.quotes
            tvReaders.text = book.views
            tvLikes.text = book.likes
            tvGenre.text = book.genre
            tvSummary.text = book.summary
        }
    }

    private fun setupRecyclerView() {
        binding.rvLikeSection.apply {
            adapter =
                youWillLIkeListAdapter.apply { onItemClickListener = this@DetailsBookFragment }
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun setupAppBar() {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        val toolbar = binding.toolbar
        toolbar.setupWithNavController(navController, appBarConfiguration)
    }

    private fun setupViewPager() {
        binding.vpImageSlider.apply {
            adapter = viewPagerAdapter.apply { onItemClickListener = this@DetailsBookFragment }
            clipChildren = false
            clipToPadding = false
            offscreenPageLimit = 3
            getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            setPageTransformer(getCompositePageTransformer())
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                    if (positionOffsetPixels != 0) {
                        return
                    }
                    when (position) {
                        0 -> this@apply.setCurrentItem(viewPagerAdapter.itemCount - 2, false)
                        viewPagerAdapter.itemCount - 1 -> this@apply.setCurrentItem(1, false)
                    }
                }
            })
        }
    }

    override fun invoke(bookId: Long) {
        val bundle = Bundle().apply {
            putLong(BOOK_ID_KEY, bookId)
        }
        findNavController().navigate(R.id.action_detailsBookFragment_self, bundle)
    }
}