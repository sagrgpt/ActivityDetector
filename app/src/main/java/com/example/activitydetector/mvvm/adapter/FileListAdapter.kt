package com.example.activitydetector.mvvm.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.example.activitydetector.R

class FileListAdapter(
    private val fileClickListener: (String) -> Unit
) : RecyclerView.Adapter<FileListViewHolder>() {

    private var fileList: List<String> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileListViewHolder {
        return FileListViewHolder(parent.inflate(R.layout.row_layout))
    }

    override fun onBindViewHolder(holder: FileListViewHolder, position: Int) {
        holder.bind(fileList[position], fileClickListener)
    }

    override fun getItemCount(): Int {
        return fileList.size
    }

    fun setData(dataSet: List<String>) {
        fileList = dataSet
        notifyDataSetChanged()
    }
}

class FileListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val fileName: TextView = itemView.findViewById(R.id.file_name)

    fun bind(item: String, fileClickListener: (String) -> Unit) {
        fileName.text = item
        fileName.setOnClickListener {
            fileClickListener.invoke(item)
        }
    }
}


/**
 * Usage:
 *      `val view = container?.inflate(R.layout.activity)`
 *      `inflater?.inflate(R.layout.fragment_dialog_standard, c)!!`
 */
private fun ViewGroup.inflate(
    @LayoutRes layoutRes: Int,
    attachToRoot: Boolean = false
): View = LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)