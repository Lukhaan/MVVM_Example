package com.example.mvvm_example.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.mvvm_example.R
import com.example.mvvm_example.viewmodel.PostEvent
import com.example.mvvm_example.viewmodel.PostViewModel
import com.example.mvvm_example.viewmodel.PostsActivityViewModel


class PostsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel: PostsActivityViewModel by viewModels()

        viewModel.posts.observe(this, { posts ->
            val mListView = findViewById<ListView>(R.id.post_list_view)
            mListView.adapter = PostAdapter(this, posts)
            mListView.onItemClickListener = OnItemClickListener { _, _, _, id ->
                viewModel.handleEvent(PostEvent.GetComments(id.toInt()))
            }
        })

        viewModel.detailedPost.observe(this, { post ->
            val postPopupModal = PostPopupModal(post.title, post.content)
            postPopupModal.show(supportFragmentManager, PostPopupModal.TAG)
        })

        setContentView(R.layout.activity_main)
        viewModel.handleEvent(PostEvent.GetPosts)
    }

    class PostAdapter(context: Context, private val dataSource: List<PostViewModel>) : BaseAdapter() {
        data class ViewHolder(
            val titleTextView: TextView
        )

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
    }
}