package id.awankkaley.capstoneprojectrx.search

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import id.awankkaley.capstoneproject.R
import id.awankkaley.core.domain.model.Popular
import id.awankkaley.core.ui.PopularAdapter
import id.awankkaley.capstoneproject.databinding.FragmentSearchBinding
import id.awankkaley.capstoneproject.util.gone
import id.awankkaley.capstoneproject.util.visible
import id.awankkaley.core.data.Resource
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel


class SearchFragment : Fragment() {

    private val homeViewModel: SearchViewModel by viewModel()

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var searchAdapter: PopularAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    @ExperimentalCoroutinesApi
    @FlowPreview
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (activity != null) {
            searchAdapter = PopularAdapter()
            searchAdapter.onItemClick = { it ->
                val bundle = bundleOf("popular" to it)
                view.findNavController().navigate(R.id.detailFragment, bundle)
            }

            with(binding.rvMovies)
            {
                layoutManager = GridLayoutManager(context, 2)
                adapter = searchAdapter
            }
        }

        searchView.queryHint = "Search Movie"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                observeApiData(query.toString())
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                return false
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun observeApiData(query: String) {
        homeViewModel.searchResult(query)
            .observe(viewLifecycleOwner) { popular ->
                if (popular != null) {
                    when (popular) {
                        is Resource.Loading -> {
                            binding.tvEmptySearch.visible()
                        }
                        is Resource.Success -> {
                            binding.tvEmptySearch.gone()
                            searchAdapter.notifyDataSetChanged()
                            searchAdapter.setData(popular.data)
                        }
                        is Resource.Error -> {
                            binding.tvEmptySearch.visible()
                        }
                        else -> {
                        }
                    }
                }


            }
    }

}
