package com.example.newsapp.ui.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.adapters.NewsAdapter
import com.example.newsapp.databinding.FragmentHeadlinesBinding
import com.example.newsapp.ui.NewsViewModel
import com.example.newsapp.util.Constants

class HeadlinesFragment : Fragment() {

    lateinit var newsViewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter
    lateinit var retryButton: Button
    lateinit var errorText: TextView
    lateinit var itemHeadlinesError: CardView
    lateinit var binding:FragmentHeadlinesBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding= FragmentHeadlinesBinding.bind(view)

        itemHeadlinesError=view.findViewById(R.id.itemHeadlinesError)

        val inflater=requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view:View=inflater.inflate(R.layout.item_error,null)
    }

    var isError=false
    var isLoading=false
    var isLastPage=false
    var isScrolling=false

    private fun hideProgressBar(){
        binding.paginationProgressBar.visibility=View.INVISIBLE
        isLoading=false
    }
    private fun showProgressBar(){
        binding.paginationProgressBar.visibility=View.VISIBLE
        isLoading=true
    }
    private fun hideErrorMessage(){
        itemHeadlinesError.visibility=View.INVISIBLE
        isError=false
    }
    private fun showErrorMessage(message:String){
        itemHeadlinesError.visibility=View.VISIBLE
        errorText.text=message
        isError=true
    }
    //this is to load more items when scrolling
    val scrollListener=object :RecyclerView.OnScrollListener(){
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager=recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition=layoutManager.findLastVisibleItemPosition()
            val visibleItemCount=layoutManager.childCount
            val totalItemCount=layoutManager.itemCount

            val isNoErrors= !isError
            val isNotLoadingAndNotLastPage= !isLastPage && !isLoading
            val isAtLastItem=firstVisibleItemPosition+visibleItemCount>=totalItemCount
            val isNotAtBeginning=firstVisibleItemPosition>=0
            val isTotalMoreThanVisible=totalItemCount>= Constants.QUERY_PAGE_SIZE
            val shouldPaginate=isNoErrors && isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible && isScrolling
            if(shouldPaginate){
                newsViewModel.getHeadlines("in")
                isScrolling=false
            }
        }

        //if user is currently dragging the screen
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)

            if(newState==AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isScrolling=true
            }
        }
    }
    //initialising recycler view and adding onScroll listener
    private fun setUpHeadLinesRecycler(){
        newsAdapter=NewsAdapter()
        binding.recyclerHeadlines.apply {
            adapter=newsAdapter
            layoutManager=LinearLayoutManager(activity)
            addOnScrollListener(this@HeadlinesFragment.scrollListener)
        }
    }

}