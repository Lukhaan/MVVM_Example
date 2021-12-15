package com.example.mvvm_example.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.mvvm_example.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.w3c.dom.Text

class PostPopupModal(
    val title: String,
    val content: String
) : BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.post_popup_modal, container, false)
        view.findViewById<TextView>(R.id.post_popup_title).text = title
        view.findViewById<TextView>(R.id.post_popup_content).text = content
        return view
    }

    companion object {
        const val TAG = "PostPopupModal"
    }
}