package com.example.kidstrackingparent.adapter



import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kidstrackingparent.Activity.DetailActivity
import com.example.kidstrackingparent.R
import com.example.kidstrackingparent.dataClass.Childs

class ChildAdapter(var c: Context, private val childList : ArrayList<Childs>) : RecyclerView.Adapter<ChildAdapter.MyViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.child_list,
            parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentitem = childList[position]

        holder.name.text = currentitem.name
        Glide.with(holder.itemView)
            .load(currentitem.photoUrl)
            .centerCrop()
            .into(holder.ivchild)

        holder.itemView.setOnClickListener {
            val mIntent = Intent(c, DetailActivity::class.java)
            mIntent.putExtra("img", currentitem.photoUrl)
            mIntent.putExtra("name", currentitem.name)
            c.startActivity(mIntent)
        }

    }

    override fun getItemCount(): Int {
        return childList.size
    }

    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val name : TextView = itemView.findViewById(R.id.tvChild)
        val ivchild : ImageView = itemView.findViewById(R.id.iVChild)

    }


}