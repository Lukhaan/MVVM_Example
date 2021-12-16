package com.example.mvvm_example.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.mvvm_example.R
import com.example.mvvm_example.viewmodel.PostEvent
import com.example.mvvm_example.viewmodel.PostViewModel
import com.example.mvvm_example.viewmodel.PostsActivityViewModel
import java.util.*


class PostsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel: PostsActivityViewModel by viewModels()

        viewModel.posts.observe(this, { posts ->
            val mListView = findViewById<ListView>(R.id.post_list_view)
            val mSearchTextView = findViewById<EditText>(R.id.post_list_search)
            mListView.adapter = PostAdapter(this, posts)
            mListView.onItemClickListener = OnItemClickListener { _, _, _, id ->
                viewModel.handleEvent(PostEvent.GetComments(id.toInt()))
            }
            mSearchTextView.addTextChangedListener {
                (mListView.adapter as PostAdapter).filter.filter(it)
            }
        })

        viewModel.detailedPost.observe(this, { post ->
            val postPopupModal = PostPopupModal(post)
            postPopupModal.show(supportFragmentManager, PostPopupModal.TAG)
        })

        setContentView(R.layout.activity_posts)

        viewModel.handleEvent(PostEvent.GetPosts)
    }

    private class PostAdapter(context: Context, private var dataSource: List<PostViewModel>) : BaseAdapter(), Filterable {
        data class ViewHolder(
            val titleTextView: TextView
        )

        private val initialData = dataSource
        private val inflater = context.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
            //If we have already constructed view then reuse, otherwise inflate new
            val viewHolder: ViewHolder
            var view = convertView
            if (convertView == null) {
                view = inflater.inflate(R.layout.list_item_post, parent, false)
                viewHolder = ViewHolder(
                    view.findViewById(R.id.list_item_title)
                )
                view.tag = viewHolder
            } else {
                viewHolder = convertView.tag as ViewHolder
            }

            //Set content from viewModel
            val post = dataSource[position]
            viewHolder.titleTextView.text = post.title
            return view
        }

        override fun getCount(): Int {
            return dataSource.count()
        }

        override fun getItem(position: Int): Any {
            return dataSource[position]
        }

        override fun getItemId(position: Int): Long {
            return dataSource[position].id.toLong()
        }

        override fun getFilter(): Filter {
            return object : Filter() {
                override fun performFiltering(constraint: CharSequence?): FilterResults {
                    val results = FilterResults()

                    if(constraint.isNullOrEmpty()) {
                        results.count = initialData.size
                        results.values = initialData
                        return results
                    } else {
                        val filteredResults: ArrayList<PostViewModel> = ArrayList()

                        for (i in initialData.indices) {
                            if (initialData[i].title.contains(constraint, true)) {
                                filteredResults.add(initialData[i])
                            }
                        }
                        results.count = filteredResults.size
                        results.values = filteredResults
                    }

                    return results
                }

                override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                    dataSource = results?.values as List<PostViewModel>
                    notifyDataSetChanged()
                }
            }
        }
    }
}