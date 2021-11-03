package com.books.app.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.books.app.R
import com.books.app.databinding.FragmentLoadingBinding
import com.books.app.util.Resource
import com.books.app.util.hideStatusBar
import com.books.app.viewmodel.LoadingViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoadingFragment : Fragment() {

    private lateinit var loadingViewModel: LoadingViewModel

    private var _binding: FragmentLoadingBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideStatusBar(requireActivity())
        loadingViewModel = ViewModelProvider(requireActivity()).get(LoadingViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoadingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadingViewModel.resource.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(2000)
                        findNavController().navigate(R.id.action_loadingFragment_to_libraryFragment)
                    }
                }
                is Resource.Error -> {
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(2000)
                        findNavController().navigate(R.id.action_loadingFragment_to_libraryFragment)
                        Toast.makeText(context, resource.message, Toast.LENGTH_SHORT).show()
                    }
                }

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}