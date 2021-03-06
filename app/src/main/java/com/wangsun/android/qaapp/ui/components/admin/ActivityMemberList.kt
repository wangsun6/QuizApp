package com.wangsun.android.qaapp.ui.components.admin

import android.app.Activity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.MenuItem
import com.wangsun.android.qaapp.R
import com.wangsun.android.qaapp.sqlite.tables.Member
import com.wangsun.android.qaapp.sqlite.tables.MemberViewModel
import com.wangsun.android.qaapp.ui.adapter.AdapterMemberList
import com.wangsun.android.qaapp.ui.components.ConstantUi
import kotlinx.android.synthetic.main.activity_member_list.*
import java.util.*


class ActivityMemberList : AppCompatActivity(), AdapterMemberList.OnItemClickListener {


    var mAdapter = AdapterMemberList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_member_list)

        initAdapter()
    }

    override fun onItemClick(member: Member) {
        val tempIntent = Intent(this, ActivityEditProfile::class.java)
        tempIntent.putExtra(Member.ARG_BUNDLE,member)
        startActivityForResult(tempIntent,ConstantUi.Intent.ACTIVITY_EDIT_PROFILE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == ConstantUi.Intent.ACTIVITY_EDIT_PROFILE && resultCode==Activity.RESULT_OK){
            //refresh the list// lets check first whether livedata works
        }
        else
            super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return true
    }

    private fun initAdapter() {
        id_recycler.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        id_recycler.adapter = mAdapter
        mAdapter.setOnItemClickListener(this)
        setAdapterData()
    }

    private fun setAdapterData() {
        ViewModelProviders.of(this).get(MemberViewModel::class.java).getAllMembers()
            .observe(this, Observer<List<Member>>{
                it?.let {t1->
                    val t2 = t1.toMutableList()
                    t2.sortWith(Comparator { v1, v2 -> v1.score.compareTo(v2.score) })
                    mAdapter.setData(t2.asReversed())
                }
            })
    }
}
