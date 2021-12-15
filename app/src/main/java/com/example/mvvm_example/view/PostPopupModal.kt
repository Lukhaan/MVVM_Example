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
        val view = inflater.inflate(R.layout.post_popup_modal, container, false)
        view.findViewById<TextView>(R.id.post_popup_title).text = post.title
        view.findViewById<TextView>(R.id.post_popup_content).text = post.content
        view.findViewById<ListView>(R.id.post_popup_list_view).adapter = CommentAdapter(view.context, post.comments)
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
                view = inflater.inflate(R.layout.list_item_popup_post, parent, false)
                viewHolder = ViewHolder(
                    view.findViewById(R.id.list_item_title),
                    view.findViewById(R.id.list_item_content)
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