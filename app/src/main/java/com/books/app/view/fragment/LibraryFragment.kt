package com.books.app.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.books.app.R
import com.books.app.databinding.FragmentLibraryBinding
import com.books.app.util.BOOK_ID_KEY
import com.books.app.util.Resource
import com.books.app.util.hideStatusBar
import com.books.app.view.fragment.adapter.ParentAdapter
import com.books.app.view.fragment.adapter.SliderAdapter
import com.books.app.viewmodel.LibraryViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class LibraryFragment : Fragment(), (Long) -> Unit {
    @Inject
    lateinit var parentAdapter: ParentAdapter

    private var _binding: FragmentLibraryBinding? = null
    private val binding
        get() = _binding!!

    private val libraryViewModel: LibraryViewModel by viewModels()
    private var _sliderAdapter: SliderAdapter? = null

    private val sliderAdapter
        get() = _sliderAdapter!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideStatusBar(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLibraryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTopSlider()
        setupGenreList()
        libraryViewModel.genreListsResource.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
//                    resource.data!!.forEach {
//                        Log.i("dev", "***************")
//                        Log.i("dev", "key ${it.genre}")
//                        it.books.forEach { book ->
//                            Log.i("dev", "${book.name} ${book.genre}")
//                        }
//                    }
                    parentAdapter.differ.submitList(resource.data!!)
                }
            }
        }

        libraryViewModel.topBannerSlideResource.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    sliderAdapter.setTopBannerSlides(
                        resource.data!!.toMutableList()
                    )
                }
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        _sliderAdapter = null
        super.onDestroyView()
    }

    override fun invoke(bookId: Long) {
        val bundle = Bundle().apply {
            putLong(BOOK_ID_KEY, bookId)
        }
        findNavController().navigate(R.id.action_libraryFragment_to_detailsBookFragment, bundle)
    }

    private fun setupGenreList() {
        binding.rvParentList.apply {
            adapter = parentAdapter.apply { onItemClickListener = this@LibraryFragment }
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun setupTopSlider() {
        _sliderAdapter = SliderAdapter().apply { onItemClickListener = this@LibraryFragment }
        binding.imageSlider.apply {
            setSliderAdapter(this@LibraryFragment.sliderAdapter)
            startAutoCycle()
        }
    }

}