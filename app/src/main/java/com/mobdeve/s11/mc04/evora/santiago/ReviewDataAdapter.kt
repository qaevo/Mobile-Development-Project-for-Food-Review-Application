package com.mobdeve.s11.mc04.evora.santiago

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ReviewDataAdapter(private var activity: Activity): RecyclerView.Adapter<ReviewDataAdapter.PostViewHolder>() {
    var rmList: ArrayList<ReviewModel> = ArrayList()
    private var onClickItem: ((ReviewModel) -> Unit)? = null
    private var onClickDeleteItem: ((ReviewModel) -> Unit)? = null

    fun addItems(items:ArrayList<ReviewModel>) {
        this.rmList = items
        notifyDataSetChanged()
    }

    fun removePost(position: Int) {
        val db = SQLHelper(activity.applicationContext)
        val post = rmList[position]
        db.deletePostById(position)

        rmList.removeAt(position)
        notifyItemRemoved(position)
    }

    fun setOnClickItem(callback: (ReviewModel) -> Unit) {
        this.onClickItem = callback
    }

    fun setOnClickDeleteItem(callback: (ReviewModel) -> Unit) {
        this.onClickDeleteItem = callback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =  PostViewHolder (
        LayoutInflater.from(parent.context).inflate(R.layout.review_layout, parent, false)
    )

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val std = rmList[position]
        holder.bindView(std)
        holder.itemView.setOnClickListener { onClickItem?.invoke(std) }
        holder.btnDelete.setOnClickListener { onClickDeleteItem?.invoke(std) }
    }

    override fun getItemCount(): Int {
        return rmList.size
    }
    fun setItems(items: ArrayList<ReviewModel>) {
        this.rmList = items
        notifyDataSetChanged()
    }
    class PostViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private var id = view.findViewById<TextView>(R.id.tvId)
        private var title = view.findViewById<TextView>(R.id.tvName)
        private var body = view.findViewById<TextView>(R.id.tvEmail)
        var btnDelete = view.findViewById<Button>(R.id.btnDelete)

        fun bindView(rm:ReviewModel) {
            id.text = rm.id.toString()
            title.text = rm.title
            body.text = rm.body
        }
    }
}