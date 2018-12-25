package com.wangsun.android.qaapp.ui.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.wangsun.android.qaapp.R
import com.wangsun.android.qaapp.sqlite.tables.Member
import com.wangsun.android.qaapp.utilities.UtilFile
import kotlinx.android.synthetic.main.item_member.view.*



class AdapterMemberList: androidx.recyclerview.widget.RecyclerView.Adapter<AdapterMemberList.MemberHolder>() {

    var mData: MutableList<Member> = arrayListOf()
    var mListener: OnItemClickListener? = null

    lateinit var mContext: Context

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MemberHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.item_member,p0,false)
        mContext = p0.context
        return MemberHolder(view)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(p0: MemberHolder, p1: Int) {
        p0.bind(mData[p1],mContext)

        p0.itemView.setOnClickListener {
            mListener?.onItemClick(mData[p1])
        }
    }


    fun setData(pData: MutableList<Member>){
        mData = pData
        notifyDataSetChanged()
    }


    class MemberHolder(item: View): androidx.recyclerview.widget.RecyclerView.ViewHolder(item){
        fun bind(member: Member,context: Context){

            val imageUri = member.email
            Glide.with(itemView.id_image)
                .load(UtilFile.getProfileImagePath()+"/"+imageUri+".jpg")
                .into(itemView.id_image)


            itemView.id_name.text = member.name
            itemView.id_score.text = member.score.toString()


        }
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.mListener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(member: Member)
    }


}