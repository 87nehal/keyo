package com.koara.keyo.Fragments

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.koara.keyo.R
import com.koara.keyo.RecyclerView.Adapter
import com.koara.keyo.ViewModel.ViewModel
import com.koara.keyo.databinding.FragmentFeedBinding

class FeedFragment : Fragment(), SearchView.OnQueryTextListener {
    private lateinit var binding : FragmentFeedBinding
    private lateinit var tempViewModel : ViewModel
    private lateinit var credantialViewModel : ViewModel
    private lateinit var adapter: Adapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFeedBinding.inflate(layoutInflater)
        //Setting RecyclerView from room data
        tempViewModel = ViewModelProvider(this)[ViewModel::class.java]
        adapter = Adapter()
        val recyclerView = binding.savedFeed
        recyclerView.adapter = adapter
        recyclerView.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)

        credantialViewModel = ViewModelProvider(this)[ViewModel::class.java]
        credantialViewModel.readAll.observe(viewLifecycleOwner, Observer { entity ->
            adapter.setData(entity)
            if (adapter.itemCount != 0){
                binding.emptyNotifier.isVisible = false
            }
        })
        setHasOptionsMenu(true)
        return binding.root
    }
    //Setting Search
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu,menu)

        val search = menu.findItem(R.id.search)
        val searchView = search.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.search){
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if(query!=null){
            searchDatabase(query)
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if(newText!=null){
            searchDatabase(newText)
        }
        return true
    }
    private fun searchDatabase(query : String){
        val searchQuery = "%$query%"
        tempViewModel.getCredentials(searchQuery).observe(this, Observer{ list ->
            list?.let {
                adapter.setData(it)
            }
        })
    }

}