package com.example.mvvm_example.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.mvvm_example.R
import com.example.mvvm_example.viewmodel.CommentViewModel
import com.example.mvvm_example.viewmodel.PostViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class PostPopupModal(
    private val post: PostViewModel
) : BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.modal_popup_post, container, false)
        val headerView = inflater.inflate(R.layout.modal_popup_header, container, false)
        headerView.findViewById<TextView>(R.id.post_popup_title).text = post.title
        headerView.findViewById<TextView>(R.id.post_popup_content).text = post.content
        val listView = view.findViewById<ListView>(R.id.list_item_comment)
        listView.addHeaderView(headerView)
        listView.adapter = CommentAdapter(view.context, post.comments)
        listView.isNestedScrollingEnabled = true
        return view
    }

    companion object {
        const val TAG = "PostPopupModal"
    }

    class CommentAdapter(context: Context, private val dataSource: List<CommentViewModel>) : BaseAdapter() {
        data class ViewHolder(
            val titleTextView: TextView,
            val contentTextView: TextView,
        )

        private val inflater = context.getSystemService(AppCompatActivity.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
            //If we have already constructed view then reuse, otherwise inflate new
            val viewHolder: ViewHolder
            var view = convertView
            if (convertView == null) {
                view = inflater.inflate(R.layout.list_item_comment, parent, false)
                viewHolder = ViewHolder(
                    view.findViewById(R.id.list_item_title),
                    view.findViewById(R.id.list_item_content)
                )
                view.tag = viewHolder
            } else {
                viewHolder = convertView.tag as ViewHolder
            }

            //Set content from viewModel
            val comment = dataSource[position]
            viewHolder.titleTextView.text = comment.title
            viewHolder.contentTextView.text = comment.content
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